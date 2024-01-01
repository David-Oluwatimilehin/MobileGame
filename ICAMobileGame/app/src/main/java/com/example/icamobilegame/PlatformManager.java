package com.example.icamobilegame;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class PlatformManager {

    private List<Platform> platformsList;
    private Random rand;
    private Platform tempPlat;
    private int barCount;
    private int screenHeight,screenWidth;
    private boolean generateAgain;
    private boolean collisionResult;
    int SpawnX;
    int SpawnY;
    float lastPlayerY;
    float deltaY;

    int platformGenX=0;
    int platformGenY=0;
    int lastPlatformX=0;
    int lastPlatformY=0;
    float screenTopOffsetY=-100.0f;
    public PlatformManager(int numOfPlatform, int sx,int sy){
        platformsList= new ArrayList<>();
        rand= new Random(90868);
        barCount=numOfPlatform;

        generateAgain=false;
        screenWidth=sx;
        screenHeight=sy;

        platformGenX = screenWidth / 4;
        platformGenY = screenHeight / 10;

        lastPlatformX = 0;
        lastPlatformY = screenHeight - 40;
    }
    public int getRandX(){
        SpawnX= rand.nextInt((screenWidth-140));
        return SpawnX;
    }
    private int getRandY(int playerY) {
        SpawnY =  playerY - rand.nextInt(platformGenY / 10 * platformGenY);
        return SpawnY;
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

            int randNum;
            randNum=rand.nextInt(2+1);
            System.out.println("rand num generated: "+randNum);
            switch(randNum){
                case 0:
                    MovingPlatform mp_platform= new MovingPlatform(context,SpawnX,SpawnY);
                    platformsList.add(mp_platform);
                    break;
                case 1:
                    Platform platform= new Platform(context,SpawnX,SpawnY);
                    platformsList.add(platform);
                    break;
                case 2:
                    BreakingPlatform bp_platform=new BreakingPlatform(context,SpawnX,SpawnY);
                    platformsList.add(bp_platform);
                    break;

            }

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

    boolean PlatformCollisionCheck(Player playerRef,float deltaTime){
        for(Platform p: this.platformsList)
        {
            collisionResult=p.CollisionCheck(playerRef,deltaTime);

            if(collisionResult){
                playerRef.Jump(deltaTime);
                playerRef.ChangeColour(collisionResult);
                playerRef.isJumping=false;

            }else{
                playerRef.ChangeColour(true);
                // Do Nothing....

            }
        }
        return collisionResult;
    }
    public void UpdatePlatforms(Player player,Context context){

        for(Platform p: this.platformsList)
        {

            p.Update();
            //p.Move(0,2 - player.position.y/screenWidth );

        }

        float playerY = player.position.y;

        /*if (player.position.y > platformsList.get(platformsList.size()-1).pos.y + screenHeight) {
            platformsList.add(new Platform(context, getRandX(),/*getRandY((int)player.position.y(int) ((int)platformsList.get(platformsList.size()-1).pos.y-100)));
        }*/
        //Check if the player has moved upwards
        if (playerY < lastPlayerY) {
             deltaY = lastPlayerY - playerY;

            // Move existing platforms upward
            for (Platform platform : platformsList) {
                platform.moveUp(new Vector2D(0,deltaY));
            }

            // Generate new platforms at the bottom

            generateAgain=true;
            generatePlatformsAboveScreen(deltaY,context);
            generateAgain=false;
        }

        lastPlayerY = playerY;

        // Remove platforms that are offscreen
        Log.d(TAG, "platform num : "+ platformsList.size());
        //removeOffscreenPlatforms();
        platformsList.removeIf(platform -> platform.pos.y + platform.platHeight < 0);

    }

    private void removeOffscreenPlatforms() {
        // Remove platforms that are offscreen
        Iterator<Platform> iterator = platformsList.iterator();
        while (iterator.hasNext()) {
            Platform platform = iterator.next();
            if (platform.pos.y +40 < 0) {
                iterator.remove();
            }
        }
    }

    void generatePlatformsAboveScreen(float deltaY,Context context){

        for (int i = 0; i < 3; i++) {

            float x = rand.nextInt(screenWidth - 140);
            float y = rand.nextInt(200);

            int randNum;
            randNum=rand.nextInt(9+1);
            System.out.println("rand num generated: "+randNum);
            switch(randNum){
                case 0:
                case 9:
                    platformsList.add(new MovingPlatform(context,(int)x,(int)y));
                    break;
                case 1:
                case 5:
                case 8:
                case 6:
                case 4:
                case 7:
                case 10:
                    platformsList.add(new Platform(context,(int)x,(int)y));
                    break;
                case 2:
                    platformsList.add(new BreakingPlatform(context,(int)x,(int)y));
                    break;
                case 3:
                    break;


            }



        }
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
