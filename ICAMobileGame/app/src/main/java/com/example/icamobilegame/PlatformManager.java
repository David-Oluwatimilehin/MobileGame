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
    float lastPlayerY;

    public PlatformManager(int numOfPlatform){
        platformsList= new ArrayList<>();
        rand= new Random(90868);
        barCount=numOfPlatform;
    }
    void SetPlatforms(Context context)
    {

        for(int i = 0; i < barCount; i++){
            SpawnX= rand.nextInt(950);
            SpawnY= rand.nextInt(1950+50);

            tempPlat =new Platform(context,SpawnX,SpawnY,1950,950);
            this.platformsList.add(tempPlat);

        }
    }

    void PlatformCollisionCheck(Player playerRef,float deltaTime){
        for(Platform p: this.platformsList)
        {
            p.CollisionCheck(playerRef,deltaTime);
        }
    }
    public void UpdatePlatforms(Player player){
        float playerY = player.position.y;

        // Check if the player has moved upwards
        if (playerY < lastPlayerY) {
            float deltaY = lastPlayerY - playerY;

            // Move existing platforms upward
            for (Platform platform : platformsList) {
                platform.moveUp(new Vector2D(0,-deltaY));
            }

            // Generate new platforms at the bottom

        }

        lastPlayerY = playerY;

        // Remove platforms that are offscreen
        platformsList.removeIf(platform -> platform.pos.y + platform.platHeight < 0);

    }

    void ResetPlatforms(Player playerRef, int height)
    {
        int limit = height/3;
        height=height*2;

        if(playerRef.position.y>=limit){
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
