package com.example.icamobilegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.Display;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.core.math.MathUtils;

public class GameView extends SurfaceView implements Runnable
{
    private SurfaceHolder surfaceHolder;
    private Bitmap bitmap;
    private Bitmap background;
    private Canvas canvas;
    private Thread gameThread;
    private Context privContext;
    private MediaPlayer mediaPlayer;
    private DisplayMetrics display=new DisplayMetrics();

    int screenHeight;
    int screenWidth;

    private int frameLengthInMS=100;
    private int frameW = 115, frameH = 137;

    private Vector2f playerPos= new Vector2f(500,900);

    private float jumpTime=1.0f;
    float jumpHeight = 10.0f;
    private float jumpForce=(jumpHeight*2.0f) / (jumpTime/2.0f);
    private double Gravity=(-2*jumpHeight)/ Math.pow(jumpTime/2.0f,2);

    private Vector2f playerVel = new Vector2f(0,0);



    private Platform platform;
    private float velocity=250;
    private int frameCount=4;
    private int currentFrame;


    private long fps;
    private long timeThisFrame=100;
    private long lastFrameChangeTime=0;

    private boolean jumpPressed=false;
    private boolean isJumping;
    private boolean  isMoving=true;
    private volatile boolean playing;

    private Rect frameToDraw =
            new Rect(0,0,frameW,frameH);
    // The place where it is going to be displayed
    private RectF whereToDraw =
            new RectF((float)playerPos.x, (float)playerPos.y, (float) playerPos.x+ frameW, frameH);


    private RectF whereToDrawBackgorund;

    public void pause()
    {
        playing=false;
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
            timeThisFrame= System.currentTimeMillis()-startFrameTime;
            if(timeThisFrame >= 1)
            {
                fps= 1000 / timeThisFrame;
            }
        }

    }

    public void HandleInput()
    {
        GroundMovement();
    }

    public GameView(Context context, DisplayMetrics dis)
    {
        super(context);
        privContext=context.getApplicationContext();
        mediaPlayer= MediaPlayer.create(privContext, R.raw.badtheme);

        screenHeight=dis.heightPixels;
        screenWidth=dis.widthPixels;

        platform=new Platform(context,600,900);
        whereToDrawBackgorund= new RectF(0,0, (float)screenHeight, (float)screenWidth);

        surfaceHolder = getHolder();
        background = BitmapFactory.decodeResource(getResources(),R.drawable.background);
        background= Bitmap.createScaledBitmap(background,screenWidth,
                screenHeight,false);

        bitmap= BitmapFactory.decodeResource(getResources(), R.drawable.run);
        bitmap= Bitmap.createScaledBitmap(bitmap,frameW*frameCount,
                frameH,false);
    }
    public void PrintMsg()
    {
        System.out.println("POSITION: " + (float)playerPos.x+" "+
                (float)playerPos.y);
    }



    public void stop()
    {
        mediaPlayer.release();
    }




    private void ApplyGravity()
    {
        boolean falling = playerVel.y < 0;
        float multiplier = falling? 2: 1;

        playerVel.y += Gravity * multiplier* fps;
        playerVel.y = playerVel.Max(playerVel.y,Gravity/2);

    }

    private void GroundMovement()
    {
        playerVel.y = playerVel.Max(playerVel.y,0);
        isJumping = playerVel.y > 0;

        if (jumpPressed)
        {
            //System.out.println("Jump set to true");
            playerVel.y+=jumpForce;
            isJumping = true;
        }
    }

    public void draw()
    {
        if(surfaceHolder.getSurface().isValid())
        {
            canvas= surfaceHolder.lockCanvas();
            canvas.drawColor(Color.BLACK);
            //canvas.setBitmap();
            whereToDraw.set((float)playerPos.x,(float)playerPos.y,
                    (float)playerPos.x+frameW, (float)playerPos.y+frameH);
            manageCurrentFrame();



            canvas.drawBitmap(background,frameToDraw,whereToDrawBackgorund,null);
            drawEndlessBackground(canvas,whereToDrawBackgorund.right,whereToDrawBackgorund.top);
            
            canvas.drawBitmap(bitmap,frameToDraw,
                    whereToDraw,null);
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }
    private void drawEndlessBackground(Canvas canvas, float left, float top) {

        float modLeft = left % screenHeight;

        canvas.drawBitmap(background, modLeft, top, null);

        /*if (left < 0) {

            canvas.drawBitmap(background, modLeft + screenWidth, top, null);

        } else {

            canvas.drawBitmap(background, modLeft - screenWidth, top, null);

        }*/

    }
    private void animateDot()
    {



    }
    private void update()
    {

        if(isMoving)
        {

            HandleInput();
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
        frameToDraw.left= currentFrame*frameW;
        frameToDraw.right=frameToDraw.left+frameW;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch(event.getAction() & MotionEvent.ACTION_MASK)
        {


            case MotionEvent.ACTION_DOWN:
                jumpPressed =!jumpPressed;
                //System.out.println(jumpPressed);
        }
        return true;
    }

}
