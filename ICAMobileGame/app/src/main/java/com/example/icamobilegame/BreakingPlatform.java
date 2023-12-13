package com.example.icamobilegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

public class BreakingPlatform extends Platform {
    private boolean isSolid = true;
    private float breakTime = 0f;
    private Bitmap brokenPlatform;
    private void startBreaking(){
        isSolid=false;

        breakTime = System.nanoTime() / 1e9f; // 1e9f means 1 Billion
    }

    public BreakingPlatform(Context context, int x, int y) {
        super(context, x, y);

        this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.redplatform);
        //brokenPlatform
        this.bitmap = Bitmap.createScaledBitmap(bitmap,platWidth,platHeight,false);

        hitBox= new RectF();
        hitBox.set(pos.x,pos.y,pos.x+platWidth,pos.y+platHeight);

        rect = new Rect(0,0,bitmap.getWidth(), bitmap.getHeight());
        rect.offsetTo((int)this.pos.x,(int)this.pos.y);
    }

    @Override
    public void Update() {
        super.Update();
        if(!isSolid && System.nanoTime() / 1e9f - breakTime > 2f){
            remove=true;
        }
    }

    @Override
    public void draw(Canvas canvas) {


        if(isSolid){

            canvas.drawRect(hitBox,myPaint);
            canvas.drawBitmap(bitmap, this.pos.x, this.pos.y, null);
        }else{

        }
    }
}
