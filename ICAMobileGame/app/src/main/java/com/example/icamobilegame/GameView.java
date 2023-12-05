package com.example.icamobilegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.RectF;
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
    private Bitmap bitmap;
    private Bitmap background;
    private Canvas canvas;
    private Thread gameThread;
    private SensorManager manager;
    private Sensor sensor;
    private SensorEventListener listener;
    private Context privContext;
    private MediaPlayer mediaPlayer;
    private DisplayMetrics display=new DisplayMetrics();

    int screenHeight;
    int screenWidth;

    private int frameLengthInMS=100;
    private int frameW = 115, frameH = 137;

    private Vector2D playerSpawnPoint= new Vector2D(500,900);

    private float jumpTime=1.0f;
    float jumpHeight = 10.0f;
    private float jumpForce=(jumpHeight*2.0f) / (jumpTime/2.0f);
    private double Gravity=(-2*jumpHeight)/ Math.pow(jumpTime/2.0f,2);

    //private Vector2f playerVel = new Vector2f(0,0);



    private PlatformManager platformManager;
    private Player player;

    //private float velocity=250;
    private int frameCount=4;
    private int currentFrame;


    private long fps;
    private long timeThisFrame=100;
    private long lastFrameChangeTime=0;


    private float currentTime;
    private float previousTime;
    private float deltaTime;


    private boolean jumpPressed=false;
    private boolean isJumping;
    private boolean  isMoving=true;
    private volatile boolean playing;

    private Rect frameToDraw =
            new Rect(0,0,frameW,frameH);
    // The place where it is going to be displayed
    private RectF whereToDraw =
            new RectF((float)playerSpawnPoint.x, (float)playerSpawnPoint.y, (float) playerSpawnPoint.x+ frameW, frameH);


    private RectF whereToDrawBackgorund;

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
        while(playing)
        {
            long startFrameTime= System.currentTimeMillis();
            //PrintMsg();
            update();
            draw();
            GetFPS(startFrameTime);
        }

    }
    public float GetFPS(long beginFrameTime){

        timeThisFrame= System.currentTimeMillis()-beginFrameTime;

        if(timeThisFrame >= 1)
        {
            fps= 1000 / timeThisFrame;
        }
        //System.out.println(fps);
        return fps;

    }


    public GameView(Context context, DisplayMetrics dis)
    {
        super(context);
        privContext=context.getApplicationContext();
        surfaceHolder = getHolder();
        mediaPlayer= MediaPlayer.create(privContext, R.raw.badtheme);

        //listener
        manager= (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor=manager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
        boolean success;

        screenHeight=dis.heightPixels;
        screenWidth=dis.widthPixels;

        platformManager= new PlatformManager();
        platformManager.SetPlatforms(context);

        player= new Player(context,playerSpawnPoint.x, playerSpawnPoint.y);
        //player.SetupPlayer(context,4);
        //whereToDrawBackgorund= new RectF(0,0, (float)screenHeight, (float)screenWidth);


        background = BitmapFactory.decodeResource(getResources(),R.drawable.background);
        background= Bitmap.createScaledBitmap(background,screenWidth,
                screenHeight,false);

        /*bitmap= BitmapFactory.decodeResource(getResources(), R.drawable.run);
        bitmap= Bitmap.createScaledBitmap(bitmap,frameW*frameCount,
                frameH,false);*/
    }



    public void stop()
    {
        mediaPlayer.release();
    }



    public void draw() {
        if(surfaceHolder.getSurface().isValid())
        {
            canvas= surfaceHolder.lockCanvas();
            canvas.drawColor(Color.WHITE);
            //canvas.setBitmap();
            /*whereToDraw.set((float)playerPos.x,(float)playerPos.y,
                    (float)playerPos.x+frameW, (float)playerPos.y+frameH);*/

            platformManager.DrawPlatforms(canvas);
            player.draw(canvas);

            //canvas.drawText();
            manageCurrentFrame();






            //canvas.drawBitmap(bitmap,frameToDraw,
                    //whereToDraw,null);
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }


    private void update()
    {

        if(isMoving)
        {

            platformManager.PlatformCollisionCheck(player);

            player.update();

            //HandleInput();

            /*playerPos.Add(playerVel);
            //playerVel.Divide(playerVel,fps);
            playerPos.x = (float)playerPos.x + playerVel.x / fps;

            if (playerPos.x > getWidth())
            {
                playerPos.y += frameH;
                playerPos.x = 10;
            }
            if (playerPos.y + frameH > getHeight())
            {
                playerPos.y = 10;
            }*/
        }
        //previousTime=currentTime;
    }

    public void manageCurrentFrame()
    {
        long time= System.currentTimeMillis();
        if(isMoving)
        {
            if(time > lastFrameChangeTime + frameLengthInMS)
            {
                lastFrameChangeTime=time;
                currentFrame++;
            }
            if(currentFrame>=frameCount)
            {
                currentFrame=0;
            }
        }
        //player.Animate(currentFrame);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch(event.getAction() & MotionEvent.ACTION_MASK)
        {
            case MotionEvent.ACTION_DOWN:
                player.isJumping=true;
                player.Jump();

                //System.out.println(jumpPressed);
        }

        return true;
    }

}
