package com.example.icamobilegame;

import android.content.Context;
import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlatformManager {

    private List<Platform> platformsList;
    private Random rand;
    private Platform tempPlat;
    private int barCount;

    int SpawnX;
    int SpawnY;

    public PlatformManager(int numOfPlatform){
        platformsList= new ArrayList<>();
        rand= new Random(90869);
        barCount=numOfPlatform;
    }
    void SetPlatforms(Context context)
    {

        for(int i = 0; i < barCount; i++){
            SpawnX= rand.nextInt(950);
            SpawnY= rand.nextInt(1950);

            tempPlat =new Platform(context,SpawnX,SpawnY,1950,950);
            this.platformsList.add(tempPlat);

        }
    }

    void PlatformCollisionCheck(Player playerRef){
        for(Platform p: this.platformsList)
        {
            p.CollisionCheck(playerRef);
        }
    }
    void ResetPlatforms(Player playerRef, int height)
    {
        int limit = height/3;

        if(playerRef.position.y<limit){
            for(int i=0; i<barCount;++i){
                playerRef.position.y= limit;
                platformsList.get(i).pos.y-=playerRef.position.y;

                if(platformsList.get(i).pos.y>height+10){
                    platformsList.get(i).pos.y= rand.nextInt(1950/3+100);
                    platformsList.get(i).pos.x=rand.nextInt(950);
                }
            }


        }
    }

    void DrawPlatforms(Canvas canvas)
    {
        for (Platform p : this.platformsList)
        {
            p.draw(canvas);
        }
    }

}
