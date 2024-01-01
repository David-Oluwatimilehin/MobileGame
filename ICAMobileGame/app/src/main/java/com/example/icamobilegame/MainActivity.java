package com.example.icamobilegame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button startButton;
    Button exitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        startButton= findViewById(R.id.startbutton);

        exitButton= findViewById(R.id.exitbutton);
        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent intent = new Intent(v.getContext(), GameActivity.class);
                startActivity(intent);
                //gameView= new GameView(, display);
                //setContentView(gameView);
            }
        });


        exitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MainActivity.this.finish();
                System.exit(0);

            }
        });
    }


    @Override
    protected void onResume(){
        super.onResume();

        if(startButton.isPressed()){
            //gameView.resume();
        }


    }
    protected void onPause(){
        super.onPause();

        if(startButton.isPressed()){
            //gameView.pause();
        }


    }
    @Override
    protected void onStop() {
        super.onStop();
        if(startButton.isPressed()){
            //gameView.stop();
        }
    }
}