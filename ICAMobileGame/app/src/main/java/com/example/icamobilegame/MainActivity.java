package com.example.icamobilegame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameView= new GameView(this);
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
}