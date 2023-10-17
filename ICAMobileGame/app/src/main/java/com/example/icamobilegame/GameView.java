package com.example.icamobilegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements Runnable {

    private SurfaceHolder surfaceHolder;
    private Bitmap bitmap;
    private Canvas canvas;
    private int frameW = 115, frameH = 137;

    private float xPos,yPos;
    private long lastFrameChangeTime;
    private int frameLengthInMS;
    private int currentFrame;
    boolean  isMoving;
    boolean playing;
    private Rect frameToDraw =
            new Rect(0,0,frameW,frameH);
    // The place where it is going to be displayed
    private RectF whereToDraw =
            new RectF(xPos, yPos, xPos + frameW, frameH);

    int frameCount;
    public GameView(Context context){
        super(context);
        surfaceHolder = getHolder();
        //bitmap= BitmapFactory.decodeResource(getResources(),/*R.drawable.run*/);
        bitmap= Bitmap.createScaledBitmap(bitmap,frameW*frameCount,
                frameH,false);
    }

    @Override
    public void run() {
        while(playing){
            long startFrameTime= System.currentTimeMillis();

            draw();
            

        }

        draw();



    }


    public void draw(){
        if(surfaceHolder.getSurface().isValid()){
            canvas= surfaceHolder.lockCanvas();
            canvas.drawColor(Color.BLACK);
            whereToDraw.set(xPos,yPos,
                    xPos+frameW, yPos+frameH);
            manageCurrentFrame();
            canvas.drawBitmap(bitmap,frameToDraw,
                    whereToDraw,null);
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    public void manageCurrentFrame()
    {
        long time= System.currentTimeMillis();
        if(isMoving){
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
}
