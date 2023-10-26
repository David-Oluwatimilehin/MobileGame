package com.example.icamobilegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements Runnable
{
    private SurfaceHolder surfaceHolder;
    private Bitmap bitmap;
    private Canvas canvas;
    private Thread gameThread;
    private Context privContext;
    private MediaPlayer mediaPlayer;

    private Vector2f playerPos= new Vector2f(10,10);
    private Vector2f playerVel= new Vector2f(250,0);
    private float xPos=10, yPos=10;
    private float velocity=250;
    private int frameCount=4;
    private int currentFrame;
    private int frameLengthInMS=100;
    private int frameW = 115, frameH = 137;

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



    public GameView(Context context)
    {
        super(context);
        privContext=context.getApplicationContext();
        mediaPlayer= MediaPlayer.create(privContext, R.raw.badtheme);
        surfaceHolder = getHolder();
        bitmap= BitmapFactory.decodeResource(getResources(), R.drawable.run);
        bitmap= Bitmap.createScaledBitmap(bitmap,frameW*frameCount,
                frameH,false);
    }
    public void PrintMsg()
    {
        System.out.println("POSITION: " + playerPos.toString());
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
            PrintMsg();
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
            canvas.drawColor(Color.WHITE);
            whereToDraw.set((float)playerPos.x,(float)playerPos.y,
                    (float)playerPos.x+frameW, (float)playerPos.y+frameH);
            manageCurrentFrame();
            canvas.drawBitmap(bitmap,frameToDraw,
                    whereToDraw,null);
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void update()
    {
        if(isMoving)
        {
            playerPos.x = (float)playerPos.x + playerVel.x / fps;

            if (playerPos.x > getWidth())
            {
                playerPos.y += frameH;
                playerPos.x = 10;
            }
            if (playerPos.y + frameH > getHeight())
            {
                playerPos.y = 10;
            }
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
