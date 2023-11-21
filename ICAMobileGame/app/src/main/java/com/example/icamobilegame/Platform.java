package com.example.icamobilegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.Random;

public class Platform {

    private Bitmap bitmap;
    private int x, y;
    private Rect rect;
    private Paint myPaint;


    public Platform(Context context, int x, int y,int screenH, int screenW) {

//rect.
        this.x = x;
        this.y = y;

        myPaint= new Paint(Paint.ANTI_ALIAS_FLAG);
        myPaint.setColor(Color.BLUE);



        int rand= (int) (Math.random() * 3) +1;
        switch(rand){
            case 1:
                this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.greenplatform);
                this.bitmap = Bitmap.createScaledBitmap(bitmap,160,40,false);
                break;

            case 2:
                this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.bluelatform);
                this.bitmap = Bitmap.createScaledBitmap(bitmap,160,40,false);
                break;

            default:
                this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.redplatform);
                this.bitmap = Bitmap.createScaledBitmap(bitmap,160,40,false);
                break;
        }

        rect = new Rect(0,0,bitmap.getWidth(), bitmap.getHeight());
        rect.offsetTo(x,y);
        //rect.set(x,y,0,0);
        System.out.println("Rect: "+ rect);

    }

    public void Set(int valX,int valY){
        this.x=valX;
        this.y=valY;
    }

    public void CollisionCheck(Player player){
        if(this.rect.intersect(player.getPlayerRect()))
        {
            System.out.println("Collision is happening");
        }
    }

    public void draw(Canvas canvas) {

        canvas.drawRect(rect,myPaint);

        canvas.drawBitmap(bitmap, x, y, null);


    }
}
