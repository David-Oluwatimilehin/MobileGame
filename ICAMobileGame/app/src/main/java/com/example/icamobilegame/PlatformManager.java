package com.example.icamobilegame;

import android.content.Context;
import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlatformManager {

    private List<Platform> platformsList;
    private Random rand;
    Platform tempPlat;

    int SpawnX;
    int SpawnY;

    public PlatformManager(){
        platformsList= new ArrayList<>();
        rand= new Random(90869);
    }
    void SetPlatforms(Context context, int numOfPlatforms)
    {

        for(int i=0;i<numOfPlatforms; i++){
            SpawnX= rand.nextInt(925);
            SpawnY= rand.nextInt(1950);

            tempPlat =new Platform(context,80,90);
            this.platformsList.add(tempPlat);
            this.platformsList.get(i).Set(SpawnX,SpawnY);
        }
    }
    void DrawPlatforms(Canvas canvas)
    {
        for (Platform p : this.platformsList) {
            p.draw(canvas);
        }
    }

}
