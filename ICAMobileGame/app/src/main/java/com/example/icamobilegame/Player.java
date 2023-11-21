package com.example.icamobilegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

public class Player {
    private Bitmap playerBitmap;

    private int x, y;
    private int frameW=117,frameH=135;
    private Rect playerRect;
    private RectF dstRect;


    private Paint playerPaint;
    private int speedY;


    public void SetupPlayer(Context context, int frameCount){



    }
    public Player(Context context, int x, int y) {
        this.x = x;
        this.y = y;

        playerPaint= new Paint(Paint.ANTI_ALIAS_FLAG);
        playerPaint.setColor(Color.CYAN);
        this.speedY = 5; // Adjust player speed as needed

        //SetupPlayer(context,4);
        this.playerBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.run);
        this.playerBitmap = Bitmap.createScaledBitmap(playerBitmap,frameW,
                frameH,false);
        dstRect= new RectF(new RectF(x, y, x*frameW,0));

        playerRect= new Rect(0,0,frameW,frameH);
        playerRect.offsetTo(x,y);
    }

    public void Animate(int currFrame){
        // TODO: Fix This
        playerRect.left= currFrame * frameW;
        playerRect.right= playerRect.left + frameW;
    }

    public void update() {

        y += speedY;
        playerRect.offsetTo(x,y);
    }

    public void draw(Canvas canvas) {

        canvas.drawRect(playerRect, playerPaint);
        //canvas.drawBitmap(playerBitmap,x,y,null);
        // TODO: Fix Animation
        //canvas.drawBitmap(playerBitmap, playerRect, dstRect,null);

    }

    public Rect getPlayerRect() {
        return playerRect;
    }
}
