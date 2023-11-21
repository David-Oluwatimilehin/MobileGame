package com.example.icamobilegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Player {
    private Bitmap playerBitmap;

    private int x, y;
    private Rect playerRect;
    private int speedY;

    public Player(Context context, int x, int y) {
        this.x = x;
        this.y = y;
        this.speedY = 5; // Adjust player speed as needed
        this.playerBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.run);
    }

    public void update() {

        y += speedY;
    }

    public void draw(Canvas canvas) {

        canvas.drawBitmap(playerBitmap, x, y, null);

    }

    public Rect getPlayerRect() {
        return playerRect;
    }
}
