package com.example.icamobilegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import org.jetbrains.annotations.Contract;

import java.util.Random;

public class Platform {

    protected Bitmap bitmap;
    protected int platWidth=140, platHeight=40;
    protected Rect rect;
    protected RectF hitBox;
    protected Paint myPaint;
    protected boolean remove;
    public Vector2D pos=new Vector2D();


    Paint collisionColour = new Paint();

    public Platform(Context context, int x, int y)
    {

        this.pos.x = x;
        this.pos.y = y;

        myPaint= new Paint(Paint.ANTI_ALIAS_FLAG);
        myPaint.setColor(Color.BLUE);

        collisionColour.setColor(Color.RED);


        this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.greenplatform);
        this.bitmap = Bitmap.createScaledBitmap(bitmap,platWidth,platHeight,false);

        hitBox= new RectF();
        hitBox.set(pos.x,pos.y,pos.x+platWidth,pos.y+platHeight);

        rect = new Rect(0,0,bitmap.getWidth(), bitmap.getHeight());
        rect.offsetTo((int)this.pos.x,(int)this.pos.y);
        //rect.set(x,y,0,0);
        //System.out.println("Rect: "+ rect);

    }
    public void moveUp(Vector2D other){
        this.pos.x+= other.x;
        this.pos.y+= other.y;
        rect.offsetTo((int)this.pos.x,(int)this.pos.y);
    }

    public void Update(){

    }



    public void CollisionCheck(Player player, float dt){

        if(player.isFalling && player.position.y < pos.y
           && player.hitBox.intersect(hitBox)){
            player.Jump(dt);
        }
        /*if(rect.left <= player.getPlayerRect().right && player.getPlayerRect().left <= rect.right
                && rect.top <= player.getPlayerRect().bottom && player.getPlayerRect().top <= rect.bottom)
        //if(Rect.intersects(player.getPlayerRect(),this.rect))
        {

            //System.out.println("Collision Happened");

            //player.onPlatform=true;
            //player.isJumping=false;

        }else{

            //System.out.println("Collision Not Happened");
        }*/

    }


    public void draw(Canvas canvas) {

        hitBox.offsetTo((int)this.pos.x,(int)this.pos.y);
        canvas.drawRect(hitBox,myPaint);
        canvas.drawBitmap(bitmap, this.pos.x, this.pos.y, null);


    }
}
