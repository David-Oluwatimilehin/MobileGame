package com.example.icamobilegame;

import static android.content.Intent.getIntent;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static androidx.core.app.ActivityCompat.recreate;
import static androidx.core.content.ContextCompat.startActivity;

import static java.lang.Thread.sleep;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import android.graphics.Typeface;
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
import android.view.View;
import android.widget.Button;


public class GameView extends SurfaceView implements Runnable
{
    private SurfaceHolder surfaceHolder;

    private GameActivity activity;
    private Bitmap background;
    private Bitmap menuBackground;
    private Bitmap menuImage;
    private Canvas canvas;
    private Thread gameThread;


    private Button playButton;

    private Context privContext;
    private MediaPlayer mediaPlayer;
    private MediaPlayer soundEffect;
    private DisplayMetrics display=new DisplayMetrics();

    int screenHeight;
    int screenWidth;
    float canvasTranslateY;



    private Vector2D playerSpawnPoint= new Vector2D(400,450);




    private PlatformManager platformManager;
    private AccelerometerHandler accelManager;

    private BitmapFactory.Options options;
    private Player player;
    private double delta;


    private int currentFrame;
    private static int finalScore;
    private Paint scoreTextColour;

    private long lastFrameChangeTime=0;


    private boolean isMoving=true;
    private boolean gameStarted;
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


        }

    }




    public GameView(Context context, DisplayMetrics dis,GameActivity gameActivity)
    {
        super(context);
        activity=gameActivity;

        privContext =context.getApplicationContext();
        surfaceHolder = getHolder();

        mediaPlayer = MediaPlayer.create(privContext, R.raw.gametheme);
        mediaPlayer.setLooping(true);
        soundEffect= MediaPlayer.create(privContext,R.raw.jumpsound);

        gameOver = false;
        gameStarted=true;
        finalScore = 0;


        scoreTextColour= new Paint(Paint.ANTI_ALIAS_FLAG);
        scoreTextColour.setTextAlign(Paint.Align.CENTER);

        scoreTextColour.setColor(Color.WHITE);
        scoreTextColour.setTextSize(60.0f);


        canvasTranslateY=0;

        accelManager=new AccelerometerHandler(privContext);

        options=new BitmapFactory.Options();
        options.inScaled=false;

        screenHeight=dis.heightPixels;
        screenWidth=dis.widthPixels;

        platformManager= new PlatformManager(10,screenWidth,screenHeight);
        platformManager.SetPlatforms(context);

        player= new Player(context,playerSpawnPoint.x, playerSpawnPoint.y+150);

        menuBackground= BitmapFactory.decodeResource(getResources(),R.drawable.mainmenu);
        menuBackground= Bitmap.createBitmap(menuBackground,0,0,menuBackground.getWidth(),menuBackground.getHeight());

        menuImage = BitmapFactory.decodeResource(getResources(),R.drawable.title);
        menuImage = Bitmap.createScaledBitmap(menuImage, 500,200,false);

        background = BitmapFactory.decodeResource(getResources(),R.drawable.img_background,options);
        background = Bitmap.createBitmap(background,0,0,background.getWidth(),background.getHeight());

    }

    public void Reset(){

    }


    public void stop()
    {


        mediaPlayer.release();
        soundEffect.release();
    }



    public void draw() {
        if(surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();

            canvas.drawColor(Color.BLACK);

            // DRAWS Background
            int gridWidth = background.getWidth();
            int gridHeight = background.getHeight();

            for (int i = 0; i < screenHeight / gridHeight; i++) {
                for (int j = 0; j < screenWidth / gridWidth; j++) {

                    canvas.drawBitmap(background, j * gridWidth+20, i * gridHeight, null);
                }
            }

            if (gameOver == false) {

                canvas.drawText("Score: " + player.getScore(), screenWidth / 2, 64, scoreTextColour);

                if (player.position.y < screenHeight) {
                    player.draw(canvas);
                    platformManager.DrawPlatforms(canvas);

                }else{
                    finalScore=player.getScore();
                    gameOver=true;
                }
            } else {


                canvas.save();

                canvas.drawText("GAME OVER!", screenWidth / 2, screenHeight / 2-250, scoreTextColour);
                canvas.restore();
                canvas.drawText("YOU SCORED " + finalScore + " POINTS! ", screenWidth / 2, screenHeight / 2 + 25, scoreTextColour);

                canvas.drawText("TAP SCREEN TO PLAY AGAIN", screenWidth / 2, screenHeight / 2 + 250, scoreTextColour);


            }
        surfaceHolder.unlockCanvasAndPost(canvas);
        }

    }





    private void update(double dt) {

        if (isMoving) {
            //platformManager.ResetPlatforms(player, 950);
            //platformManager.UpdatePlatforms(player);

            if(playing) {

                if (accelManager.getAccelX() > 3) {
                    player.position = player.moveLeft(accelManager.linear_acceleration[0]);
                }
                if (accelManager.getAccelX() < -3) {
                    player.position = player.moveRight(-accelManager.linear_acceleration[0]);
                }
            }

            player.update(screenWidth);

            if(platformManager.PlatformCollisionCheck(player, (float) dt)){
                soundEffect.start();

            }
            platformManager.UpdatePlatforms(player,privContext);

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

                if(gameOver){
                    activity.recreate();
                }

        }

        return true;
    }



}
