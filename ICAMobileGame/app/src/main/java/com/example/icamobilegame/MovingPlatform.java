package com.example.icamobilegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

public class MovingPlatform extends Platform{

    private float speed=10.0f;
    private float range= platWidth*3;
    private float direction=1;
    private float start= pos.x;;
    public MovingPlatform(Context context, int x, int y) {
        super(context, x, y);

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.bluelatform);
        bitmap = Bitmap.createScaledBitmap(bitmap,platWidth,platHeight,false);

        rect = new Rect(0,0,bitmap.getWidth(), bitmap.getHeight());
        rect.offsetTo((int)this.pos.x,(int)this.pos.y);
    }



    @Override
    public void Update() {
        super.Update();

        pos.x += speed * direction;

        if(pos.x<start-range|| pos.x>start+range){
            direction*=-1;
        }

    }
}

