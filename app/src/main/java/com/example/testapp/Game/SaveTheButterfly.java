package com.example.testapp.Game;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.example.testapp.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;

public class SaveTheButterfly extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_the_butterfly);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
    public void startGame(View view) {
        GameView gameView = new GameView(this);
        setContentView(gameView);
    }

}