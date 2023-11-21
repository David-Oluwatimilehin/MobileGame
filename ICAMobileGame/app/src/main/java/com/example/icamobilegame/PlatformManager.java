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
    void SetPlatforms(Context context)
    {

        for(int i = 0; i < 20; i++){
            SpawnX= rand.nextInt(950);
            SpawnY= rand.nextInt(1950);

            tempPlat =new Platform(context,SpawnX,SpawnY,1950,950);
            this.platformsList.add(tempPlat);
            //this.platformsList.get(i).Set(SpawnX,SpawnY);
        }
    }
    void DrawPlatforms(Canvas canvas)
    {
        for (Platform p : this.platformsList) {
            p.draw(canvas);
        }
    }

}
