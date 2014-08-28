package com.twi.awayday2014.utils;

import android.graphics.Color;
import com.twi.awayday2014.R;

import java.util.Random;

public class RandomColorSelector {

    public static int[] backgroundResources = {
            Color.parseColor("#aaf36c60"), Color.parseColor("#aaf06292"),
            Color.parseColor("#aaba68c8"), Color.parseColor("#aa9575cd"),
            Color.parseColor("#aa7986cb"), Color.parseColor("#aa91a7ff"),
            Color.parseColor("#aa4fc3f7"), Color.parseColor("#aa4dd0e1"),
            Color.parseColor("#aa4db6ac"), Color.parseColor("#aa42bd41"),
            Color.parseColor("#aadce775"), Color.parseColor("#aaaed581")
    };

    public int next() {
        return backgroundResources[getRandomColorIndex()];
    }

    private int getRandomColorIndex() {
        return randInt(0, backgroundResources.length - 1);
    }

    private int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

}
