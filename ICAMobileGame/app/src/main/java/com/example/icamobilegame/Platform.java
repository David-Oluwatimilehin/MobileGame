package com.example.icamobilegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.Random;

public class Platform {

    private Bitmap bitmap;
    private int x, y;
    private Rect rect;
    public Platform(Context context, int x, int y) {
        this.x = x;
        this.y = y;

        int rand= (int) (Math.random() * 3) +1;
        switch(rand){
            case 1:
                this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.greenplatform);
                break;
            case 2:
                this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.bluelatform);
                break;
            default:
                this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.redplatform);
                break;
        }
        System.out.println("Random Check: "+ rand);

    }

    public void Set(int valX,int valY){
        this.x=valX;
        this.y=valY;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, x, y, null);
    }
}
