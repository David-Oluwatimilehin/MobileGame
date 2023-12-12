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
    private int screenHeight,screenWidth;

    int SpawnX;
    int SpawnY;
    float lastPlayerY;

    int platformGenX=0;
    int platformGenY=0;
    int lastPlatformX=0;
    int lastPlatformY=0;
    float screenTopOffsetY=-100.0f;
    public PlatformManager(int numOfPlatform, int sx,int sy){
        platformsList= new ArrayList<>();
        rand= new Random(90868);
        barCount=numOfPlatform;

        screenWidth=sx;
        screenHeight=sy;

        platformGenX = screenWidth / 4;
        platformGenY = screenHeight / 10;

        lastPlatformX = 0;
        lastPlatformY = screenHeight - 40;
    }
    void SetPlatforms(Context context)
    {

        while (platformsList.size() < barCount){
            SpawnX= rand.nextInt((screenWidth-140));

            if(lastPlatformY-platformGenY / 2 < screenTopOffsetY)
                break;

            SpawnY= lastPlatformY-rand.nextInt(platformGenY / 2 + platformGenY);

            if(SpawnY<screenTopOffsetY)
                continue;

            if(SpawnX < lastPlatformX + platformGenX &&
                    (lastPlatformX!=0 && SpawnX > lastPlatformX-platformGenX)) {
                continue;
            }

            Platform platform = new Platform(context,SpawnX,SpawnY);

            platformsList.add(platform);

            lastPlatformX=SpawnX;
            lastPlatformY=SpawnY;




        }

        /*for(int i = 0; i < barCount; i++){
            //SpawnX= rand.nextInt(950);
            //SpawnY= rand.nextInt(1950+50);

            tempPlat =new Platform(context,SpawnX,SpawnY,1950,950);
            this.platformsList.add(tempPlat);

        }*/
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
