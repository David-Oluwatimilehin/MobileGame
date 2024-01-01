package com.example.icamobilegame;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import static java.lang.Thread.sleep;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.util.DisplayMetrics;

import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


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
    private MediaPlayer soundEffect;
    private DisplayMetrics display=new DisplayMetrics();

    int screenHeight;
    int screenWidth;
    float canvasTranslateY;



    private Vector2D playerSpawnPoint= new Vector2D(400,450);

    private float jumpTime=1.0f;
    float jumpHeight = 10.0f;
    private float jumpForce=(jumpHeight*2.0f) / (jumpTime/2.0f);
    private double Gravity=(-2*jumpHeight)/ Math.pow(jumpTime/2.0f,2);


    private PlatformManager platformManager;
    private AccelerometerHandler accelManager;

    private BitmapFactory.Options options;
    private Player player;
    private double delta;


    private int currentFrame;
    private int score;
    private Paint scoreTextColour;

    private long lastFrameChangeTime=0;


    private boolean isMoving=true;
    private boolean gameOver;
    private volatile boolean playing;


    public void pause()
    {
        playing=false;

        accelManager.UnregisterListener();
        mediaPlayer.pause();
        soundEffect.pause();
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
        soundEffect.start();

        accelManager.RegisterListener();

        gameThread= new Thread(this);
        gameThread.start();
    }
    @Override
    public void run()
    {
        long lastFPSCheck=System.currentTimeMillis();
        int fps = 0;

        final double MAX_PERIOD=60.0f;
        final double UPS_PERIOD= 1E+3/MAX_PERIOD;
        long sleepTime;
        long elapsedTimeMillis;
        long previousTimeMillis= System.currentTimeMillis();
        long lastDelta=System.nanoTime();
        long nanoSec= 1_000_000_000;

        int updateCount=0;
        int frameCount=0;

        if(!playing){
            player.position=playerSpawnPoint;
        }
        while(playing)
        {
            try{

                update(1.0f);
                draw();

            }catch(IllegalArgumentException e){
                e.printStackTrace();
            }

            updateCount++;
            frameCount++;


            /*long nowDelta= System.nanoTime();
            long timeSinceLastDelta= nowDelta-lastDelta;
            delta = timeSinceLastDelta / nanoSec;*/

            long currentTimeMillis=System.currentTimeMillis();
            elapsedTimeMillis=currentTimeMillis-previousTimeMillis;
            //previousTimeMillis=currentTimeMillis;
            sleepTime=(long)(updateCount*UPS_PERIOD - elapsedTimeMillis);
            if(sleepTime > 0){
                try{
                    sleep(sleepTime);
                }catch(InterruptedException e){
                   e.printStackTrace();
                }
                //fps++;


            }

            /*long now= System.currentTimeMillis();
            if(now-lastFPSCheck >= 1000){
                //Log.d(TAG, "FPS: "+ fps +" "+System.currentTimeMillis());
                fps = 0;
                lastFPSCheck += 1000;
            }*/


        }

    }




    public GameView(Context context, DisplayMetrics dis)
    {
        super(context);
        privContext =context.getApplicationContext();
        surfaceHolder = getHolder();

        mediaPlayer = MediaPlayer.create(privContext, R.raw.badtheme);
        mediaPlayer.setLooping(true);
        soundEffect= MediaPlayer.create(privContext,R.raw.jumpsound);

        gameOver = false;

        score = 0;
        scoreTextColour= new Paint(Paint.ANTI_ALIAS_FLAG);
        scoreTextColour.setTextAlign(Paint.Align.CENTER);
        scoreTextColour.setColor(Color.WHITE);
        scoreTextColour.setTextSize(48.0f);

        canvasTranslateY=0;

        //listener
        //Log.d(TAG, "GameView: Sensor Manager Initialization");
        accelManager=new AccelerometerHandler(privContext);

        options=new BitmapFactory.Options();
        options.inScaled=false;

        screenHeight=dis.heightPixels;
        screenWidth=dis.widthPixels;

        platformManager= new PlatformManager(10,screenWidth,screenHeight);
        platformManager.SetPlatforms(context);

        player= new Player(context,playerSpawnPoint.x, playerSpawnPoint.y+150);
        //player.SetupPlayer(context,4);
        //whereToDrawBackgorund= new RectF(0,0, (float)screenHeight, (float)screenWidth);


        background = BitmapFactory.decodeResource(getResources(),R.drawable.img_background,options);
        background = Bitmap.createBitmap(background,0,0,background.getWidth(),background.getHeight());
        //background = Bitmap.createScaledBitmap(background,screenWidth, screenHeight,false);
        /*bitmap= BitmapFactory.decodeResource(getResources(), R.drawable.run);
        bitmap= Bitmap.createScaledBitmap(bitmap,frameW*frameCount,
                frameH,false);*/
    }



    public void stop()
    {

        //manager.unregisterListener(listener);
        mediaPlayer.release();
        soundEffect.release();
    }



    public void draw() {
        if(surfaceHolder.getSurface().isValid())
        {
            canvas= surfaceHolder.lockCanvas();
            canvas.drawColor(Color.BLACK);

            int gridWidth=background.getWidth();
            int gridHeight=background.getHeight();

            for(int i=0; i<screenHeight/gridHeight; i++){
                for(int j=0; j<screenWidth/gridWidth; j++){

                    canvas.drawBitmap(background,j*gridWidth,i*gridHeight,null);
                }
            }

            //canvas.drawBitmap(background,0,0,null);

            canvas.translate(0,canvasTranslateY);

            platformManager.DrawPlatforms(canvas);

            player.draw(canvas);

            canvas.drawText("Score: "+player.getScore(),screenWidth/2,64,scoreTextColour);



            if(gameOver){
                canvas.drawText("GAME OVER!",screenWidth/2,screenHeight/2,scoreTextColour);

                if(player.position.y < screenHeight){

                }

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
            
            if(mediaPlayer.isPlaying())
            {
                Log.d(TAG, "update: Yay there is music");
            }
            if(platformManager.PlatformCollisionCheck(player, (float) dt)){
                soundEffect.start();
                soundEffect.reset();
            }
            platformManager.UpdatePlatforms(player,privContext);
            player.update(screenWidth);
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
                //canvasTranslateY -= player.velocity.y;

                //System.out.println(jumpPressed);
        }

        return true;
    }

}
