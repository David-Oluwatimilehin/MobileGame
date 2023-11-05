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
    private Context privContext;
    private MediaPlayer mediaPlayer;
    private DisplayMetrics display=new DisplayMetrics();

    int screeHeight;
    int screenWidth;

    private int frameLengthInMS=100;
    private int frameW = 115, frameH = 137;

    private Vector2f playerPos= new Vector2f(500,900);
    private Vector2f playerVel= new Vector2f(250,0);

    private float velocity=250;
    private int frameCount=4;
    private int currentFrame;


    private long fps;
    private long timeThisFrame=100;
    private long lastFrameChangeTime=0;

    private boolean  isMoving;
    private volatile boolean playing;

    private Rect frameToDraw =
            new Rect(0,0,frameW,frameH);
    // The place where it is going to be displayed
    private RectF whereToDraw =
            new RectF((float)playerPos.x, (float)playerPos.y, (float) playerPos.x+ frameW, frameH);


    private RectF whereToDrawBackgorund;



    public GameView(Context context, DisplayMetrics dis)
    {
        super(context);
        privContext=context.getApplicationContext();
        mediaPlayer= MediaPlayer.create(privContext, R.raw.badtheme);

        screeHeight=dis.heightPixels;
        screenWidth=dis.widthPixels;


        whereToDrawBackgorund= new RectF(0,0, (float)screeHeight, (float)screenWidth);

        surfaceHolder = getHolder();
        background = BitmapFactory.decodeResource(getResources(),R.drawable.background);
        background= Bitmap.createScaledBitmap(background,screenWidth,
                screeHeight,false);

        bitmap= BitmapFactory.decodeResource(getResources(), R.drawable.run);
        bitmap= Bitmap.createScaledBitmap(bitmap,frameW*frameCount,
                frameH,false);
    }
    public void PrintMsg()
    {
        System.out.println("POSITION: " + (float)playerPos.x+" "+
                (float)playerPos.y);
    }


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

    public void stop()
    {
        mediaPlayer.release();
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

    public void draw()
    {
        if(surfaceHolder.getSurface().isValid())
        {
            canvas= surfaceHolder.lockCanvas();
            //canvas.drawColor(Color.BLACK);
            //canvas.setBitmap();
            whereToDraw.set((float)playerPos.x,(float)playerPos.y,
                    (float)playerPos.x+frameW, (float)playerPos.y+frameH);
            manageCurrentFrame();

            //canvas.drawBitmap(background,frameToDraw,whereToDrawBackgorund,null);
            drawEndlessBackground(canvas,whereToDrawBackgorund.right,whereToDrawBackgorund.top);

            canvas.drawBitmap(bitmap,frameToDraw,
                    whereToDraw,null);
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }
    private void drawEndlessBackground(Canvas canvas, float left, float top) {

        float modLeft = left % screeHeight;

        canvas.drawBitmap(background, modLeft, top, null);

        if (left < 0) {

            canvas.drawBitmap(background, modLeft + screenWidth, top, null);

        } else {

            canvas.drawBitmap(background, modLeft - screenWidth, top, null);

        }

    }
    private void update()
    {
        if(isMoving)
        {
            //whereToDrawBackgorund;
            whereToDrawBackgorund.top+=1;
            whereToDrawBackgorund.bottom+=1;
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
                isMoving = !isMoving;
                break;

        }
        return true;
    }

}
