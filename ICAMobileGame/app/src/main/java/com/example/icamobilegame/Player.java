package com.example.icamobilegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Player {
    private Bitmap playerBitmap;

    private int x, y;
    private Rect playerRect;
    private Paint playerPaint;
    private int speedY;

    public Player(Context context, int x, int y) {
        this.x = x;
        this.y = y;

        playerPaint= new Paint(Paint.ANTI_ALIAS_FLAG);
        playerPaint.setColor(Color.CYAN);
        this.speedY = 5; // Adjust player speed as needed

        this.playerBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.run);
        this.playerBitmap = Bitmap.createScaledBitmap(playerBitmap,117*4,135,false);

        playerRect= new Rect(0,0,playerBitmap.getWidth(),playerBitmap.getHeight());
        playerRect.offsetTo(x,y);
    }

    public void update() {

        y += speedY;
    }

    public void draw(Canvas canvas) {

        canvas.drawRect(playerRect, playerPaint);

        canvas.drawBitmap(playerBitmap, x, y, null);


    }

    public Rect getPlayerRect() {
        return playerRect;
    }
}
