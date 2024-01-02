package com.example.icamobilegame;


import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {

    GameView gameView;

    DisplayMetrics display= new DisplayMetrics();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        getWindowManager().getDefaultDisplay().getMetrics(display);

        //startActivity(intent);
        gameView= new GameView(this, display,this);
        setContentView(gameView);

    }


    @Override
    protected void onResume(){
        super.onResume();
        gameView.resume();

    }
    protected void onPause(){
        super.onPause();
        gameView.pause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        gameView.stop();

    }
}
