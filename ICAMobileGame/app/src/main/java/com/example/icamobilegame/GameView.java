package com.example.icamobilegame;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

public class GameView extends SurfaceView implements Runnable
{
    private SurfaceHolder surfaceHolder;

    private Bitmap background;
    private Canvas canvas;
    private Thread gameThread;
    private SensorManager manager;
    private float gravity[]= new float[3];
    private float[] linear_acceleration= new float[3];
    private Sensor sensor;

    private Context privContext;
    private MediaPlayer mediaPlayer;
    private DisplayMetrics display=new DisplayMetrics();

    int screenHeight;
    int screenWidth;
    float canvasTranslateY;

    private int frameLengthInMS=100;
    private int frameW = 115, frameH = 137;

    private Vector2D playerSpawnPoint= new Vector2D(400,450);

    private float jumpTime=1.0f;
    float jumpHeight = 10.0f;
    private float jumpForce=(jumpHeight*2.0f) / (jumpTime/2.0f);
    private double Gravity=(-2*jumpHeight)/ Math.pow(jumpTime/2.0f,2);


    private PlatformManager platformManager;
    private Player player;
    private double delta;


    private int currentFrame;
    private int score;
    private Paint scoreTextColour;

    private long lastFrameChangeTime=0;


    private boolean isMoving=true;
    private boolean gameOver;
    private volatile boolean playing;

    private Rect frameToDraw =
            new Rect(0,0,frameW,frameH);
    // The place where it is going to be displayed
    private RectF whereToDraw =
            new RectF((float)playerSpawnPoint.x, (float)playerSpawnPoint.y, (float) playerSpawnPoint.x+ frameW, frameH);



    private RectF whereToDrawBackgorund;
    private SensorEventListener listener= new SensorEventListener() {
    public void onSensorChanged(SensorEvent event) {
        float xAxis = event.values[0];
        float yAxis = event.values[1];
        float zAxis = event.values[2];

        final float alpha = 0.8f;

        gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
        gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];


        linear_acceleration[0] = event.values[0] - gravity[0];
        linear_acceleration[1] = event.values[1] - gravity[1];
        linear_acceleration[2] = event.values[2] - gravity[2];
        //Log.d(TAG, "onSensorChanged: X:"+linear_acceleration[0]+" Y:"+linear_acceleration[1]+" Z:"+linear_acceleration[2]);

        if(playing){

            if(xAxis>2)
            {
                player.position=player.moveLeft(linear_acceleration[0]);
            }if(xAxis<-2){
                player.position=player.moveRight(-linear_acceleration[0]);
            }


        }

    }

    public void onAccuracyChanged(Sensor s, int i) {

    }
};
    public void pause()
    {
        playing=false;

        if(sensor != null)
        {
            manager.unregisterListener(listener);
        }
        mediaPlayer.pause();

        try{
            gameThread.join();
        }catch(InterruptedException ie){
            Log.e("GameView", "INTERRUPTED: ");
        }
    }
    public void resume()
    {
        playing=true;
        mediaPlayer.start();

        if(sensor != null){
            manager.registerListener(listener,sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        gameThread= new Thread(this);
        gameThread.start();
    }
    @Override
    public void run()
    {
        long lastFPSCheck=System.currentTimeMillis();
        int fps = 0;

        long lastDelta=System.nanoTime();
        long nanoSec= 1_000_000_000;
        if(!playing){
            player.position=playerSpawnPoint;
        }
        while(playing)
        {
            long nowDelta= System.nanoTime();
            long timeSinceLastDelta= nowDelta-lastDelta;
            delta = timeSinceLastDelta / nanoSec;

            update(delta);
            draw();
            fps++;
            long now= System.currentTimeMillis();
            if(now-lastFPSCheck >= 1000){
                Log.d(TAG, "FPS: "+ fps +" "+System.currentTimeMillis());
                fps = 0;
                lastFPSCheck += 1000;
            }


        }

    }



    public void setupGravity(){
        gravity[0]=9.81f;
        gravity[1]=9.81f;
        gravity[2]=9.81f;
    }
    public GameView(Context context, DisplayMetrics dis)
    {
        super(context);
        privContext =context.getApplicationContext();
        surfaceHolder = getHolder();
        mediaPlayer = MediaPlayer.create(privContext, R.raw.badtheme);
        gameOver = false;

        score = 0;
        scoreTextColour= new Paint(Paint.ANTI_ALIAS_FLAG);
        scoreTextColour.setTextAlign(Paint.Align.CENTER);
        scoreTextColour.setColor(Color.WHITE);
        scoreTextColour.setTextSize(48.0f);

        canvasTranslateY=0;

        //listener
        //Log.d(TAG, "GameView: Sensor Manager Initialization");
        manager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        boolean hasAccel =manager.getSensorList(Sensor.TYPE_ACCELEROMETER).size()> 0;

        if(hasAccel){
            sensor = manager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
            //sensor = manager.getSensorList(Sensor.TYPE_MAGNETIC_FIELD).get(0);
            manager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
            //Log.d(TAG, "GameView: Sensor Successfully Added");
        }

        screenHeight=dis.heightPixels;
        screenWidth=dis.widthPixels;

        platformManager= new PlatformManager(10,screenWidth,screenHeight);
        platformManager.SetPlatforms(context);

        player= new Player(context,playerSpawnPoint.x, playerSpawnPoint.y);
        //player.SetupPlayer(context,4);
        //whereToDrawBackgorund= new RectF(0,0, (float)screenHeight, (float)screenWidth);


        background = BitmapFactory.decodeResource(getResources(),R.drawable.img_background);
        background = Bitmap.createScaledBitmap(background,screenWidth,
                screenHeight,false);
        /*bitmap= BitmapFactory.decodeResource(getResources(), R.drawable.run);
        bitmap= Bitmap.createScaledBitmap(bitmap,frameW*frameCount,
                frameH,false);*/
    }



    public void stop()
    {

        manager.unregisterListener(listener);
        mediaPlayer.release();
    }



    public void draw() {
        if(surfaceHolder.getSurface().isValid())
        {
            canvas= surfaceHolder.lockCanvas();
            canvas.drawColor(Color.BLACK);

            canvas.drawBitmap(background,0,0,null);
            canvas.translate(0,canvasTranslateY);



            platformManager.DrawPlatforms(canvas);
            player.draw(canvas);
            canvas.drawText("Score: "+score,screenWidth/2,64,scoreTextColour);



            if(gameOver){
                canvas.drawText("GAME OVER!",screenWidth/2,screenHeight/2,scoreTextColour);
            }
            //canvas.drawBitmap(bitmap,frameToDraw,
                    //whereToDraw,null);
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }


    private void update(double dt) {

        if (isMoving) {
            //platformManager.ResetPlatforms(player, 950);
            //platformManager.UpdatePlatforms(player);
            platformManager.PlatformCollisionCheck(player, (float) dt);

            player.update(dt, screenWidth);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch(event.getAction() & MotionEvent.ACTION_MASK)
        {
            case MotionEvent.ACTION_DOWN:
                player.isJumping=true;

                player.Jump((float)delta);
                canvasTranslateY -= player.velocity.y;

                //System.out.println(jumpPressed);
        }

        return true;
    }

}
