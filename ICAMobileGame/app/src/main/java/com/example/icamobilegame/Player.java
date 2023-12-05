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

    private final int jumpHeight;

    public float jumpForce;
    public Vector2D position=new Vector2D();
    public Vector2D velocity = new Vector2D();
    public Vector2D gravity=new Vector2D();
    private final int frameW=117;
    public final int frameH=135;

    private final Rect playerRect;
    private final RectF dstRect;

    private final Paint playerPaint;
    public boolean isJumping;
    public boolean onPlatform;

    public Player(Context context, float x, float y) {
        onPlatform=false;

        this.position.x = x;
        this.position.y = y;

        this.velocity.y = -75.0f;
        this.gravity.y = -12.5f;

        playerPaint= new Paint(Paint.ANTI_ALIAS_FLAG);
        playerPaint.setColor(Color.GREEN);
        //this.speedY = 5; // Adjust player speed as needed

        jumpForce = (velocity.y * 4);
        this.jumpHeight=200;
        this.isJumping=true;

        //SetupPlayer(context,4);
        this.playerBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.run);
        this.playerBitmap = Bitmap.createScaledBitmap(playerBitmap,frameW,
                frameH,false);
        dstRect= new RectF(new RectF(this.position.x, this.position.y,
                this.position.x*frameW,0));

        playerRect= new Rect(0,0,frameW,frameH);
        //playerRect.offsetTo((int)this.position.x,(int)this.position.y);
    }

    public void Jump(){

        position=Vector2D.add(position,jumpForce);

        this.isJumping=false;
    }
    public void Animate(int currFrame){
        // TODO: Fix This
        playerRect.left= currFrame * frameW;
        playerRect.right= playerRect.left + frameW;
    }

    private void ApplyGravity(){

        if(!onPlatform)
        {
            position=Vector2D.subtract(position,gravity);
        }


    }


    public void update() {
        //System.out.println("X: "+position.x+"Y: "+position.y);
        if(!isJumping){

            //position=Vector2D.add(position,Vector2D.scalar(velocity,dt));
            ApplyGravity();
            //Animate();
        }

        playerRect.offsetTo((int)this.position.x,(int)this.position.y);
    }

    public void draw(Canvas canvas)
    {
        //playerPaint.setColor(Color.GREEN);
        canvas.drawRect(playerRect, playerPaint);
        canvas.drawBitmap(playerBitmap,this.position.x,this.position.y,null);
        // TODO: Fix Animation
        //canvas.drawBitmap(playerBitmap, playerRect, dstRect,null);

    }

    public Rect getPlayerRect() {
        return playerRect;
    }
}
