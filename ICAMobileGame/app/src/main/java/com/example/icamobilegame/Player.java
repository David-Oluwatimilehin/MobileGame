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
    private Bitmap [][] sprites= new Bitmap[5][4];
    private BitmapFactory.Options options= new BitmapFactory.Options();



    public float jumpForce;
    public Vector2D position=new Vector2D();
    private float lastY;
    public Vector2D velocity = new Vector2D();
    public Vector2D gravity=new Vector2D();
    private final int frameW=128;
    public final int frameH=128;
    private int animTick;
    private int animSpeed;
    private int playerAnimX=2,playerAnimY;

    private Rect playerRect;
    public RectF hitBox;

    private Paint playerPaint;
    public boolean isJumping;
    public boolean isFalling;
    public boolean onPlatform;
    private int score;

    public int getScore() {
        return score;
    }

    public Player(Context context, float x, float y) {
        options.inScaled=false;
        onPlatform=false;

        this.position.x = x;
        this.position.y = y;
        this.lastY=position.y;

        this.score=0;
        this.animSpeed = 60;
        this.velocity.x = 50.0f;
        this.velocity.y = -50.0f;
        this.gravity.y = -9.81f;

        playerPaint= new Paint(Paint.ANTI_ALIAS_FLAG);
        playerPaint.setColor(Color.GREEN);
        //this.speedY = 5; // Adjust player speed as needed

        jumpForce = (velocity.y * 6);

        this.isJumping=true;
        this.isFalling=false;

        // TODO: Fix This
        this.playerBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.dante,options);
        for(int j=0; j<sprites.length; j++)
            for(int i=0; i<sprites[j].length; i++) {
                int currentPosX= i*128;
                int currentPosY= j*128;
                sprites[j][i]= getScaledBitmap(Bitmap.createBitmap(playerBitmap,currentPosX,currentPosY,frameW,frameH));
            }

        this.hitBox=new RectF();
        this.hitBox.set(position.x,position.y,position.x+128,position.y+128);
        //hitBox.offsetTo((int)this.position.x,(int)this.position.y);

        playerRect= new Rect(frameH,frameW,0,0);
        //playerRect.offsetTo((int)this.position.x,(int)this.position.y);
    }
    public void ChangeColour(boolean result){
        if(result){
            playerPaint.setColor(Color.GREEN);
        }else{
            playerPaint.setColor(Color.RED);
        }

    }

    public Vector2D moveLeft(float lin){
        this.position.x-= velocity.x*lin*2;
        return position;
    }
    public Vector2D moveRight(float lin){
        this.position.x += velocity.x*lin*2;
        return position;
    }
    public void Jump(float dt)
    {

        position=Vector2D.add(position,jumpForce);
        this.isJumping=false;

    }
    public void Animate() {

        animTick++;

        if (animTick >= animSpeed) {
            animTick=0;
            playerAnimY++;
            if(playerAnimY>=5) {
                playerAnimY=0;
            }
        }
    }
    private void ApplyGravity(){

        if(!onPlatform)
        {
            position.y=Vector2D.subtract(position,gravity.y*(float) 1.5);
        }
        this.isFalling=true;
    }

    private void UpdateScore(){
        if (position.y < lastY) {
            // Increase score when the player is moving upward
            score += 10; // Adjust the score increase as needed
        }
        lastY = position.y;
    }

    public void update( float screenWidth) {
        //Log.d(TAG, "update: X:"+position.x+"Y: "+position.y);
        hitBox.offsetTo(this.position.x,this.position.y);

        if(!isJumping){

            //position=Vector2D.add(position,Vector2D.scalar(velocity,dt));
            ApplyGravity();



            if (position.x < 0) {
                position.x = screenWidth; // Wrap to the right side
            } else if (position.x > screenWidth) {
                position.x = 0; // Wrap to the left side
            }
        }

        UpdateScore();

    }

    public void draw(Canvas canvas)
    {
        //playerPaint.setColor(Color.GREEN);

        //canvas.drawRect(hitBox, playerPaint);
        canvas.drawBitmap(getSprites(playerAnimY,playerAnimX), this.position.x, this.position.y,null);
        Animate();
        // TODO: Fix Animation
        //canvas.drawBitmap(playerBitmap, playerRect, dstRect,null);

    }
    private Bitmap getScaledBitmap(Bitmap bitmap){
        return Bitmap.createScaledBitmap(bitmap,bitmap.getWidth(),bitmap.getHeight(),false);
    }
    private Bitmap getSprites(int PosX,int PosY){
        return sprites[PosX][PosY];
    }

}
