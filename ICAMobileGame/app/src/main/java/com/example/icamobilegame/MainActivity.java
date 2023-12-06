package com.example.icamobilegame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button startButton;
    GameView gameView;
    boolean StartButtonPressed=false;
    DisplayMetrics display= new DisplayMetrics();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindowManager().getDefaultDisplay().getMetrics(display);
        gameView= new GameView(this, display);
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
    protected void onRestart() {

        super.onRestart();


    }
    @Override
    protected void onStop() {
        super.onStop();

        gameView.stop();


    }
}