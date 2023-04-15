package com.example.testapp.Game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.testapp.R;

import java.util.Random;

public class Spike {
    Bitmap spike[] = new Bitmap[3];
    int spikeFrame = 0;
    int spikeX;
    int spikeY;
    int spikeVelocity;
    Random random;
    public Spike(Context context) {
        spike[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.up1);
        spike[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.up1);
        spike[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.up1);
        random = new Random();
        resetPosition();
    }

    public Bitmap getSpike(int spikeFrame){
        return spike[spikeFrame];
    }
    public int getSpikeWidth(){
        return spike[0].getWidth();
    }

    public int getSpikeHeight(){
        return spike[0].getHeight();
    }

    public void resetPosition() {
        spikeX = random.nextInt(GameView.dWidth - getSpikeWidth());
        spikeY = -200 + random.nextInt(600) * -1;
        spikeVelocity = 35 + random.nextInt(16);
    }
}
