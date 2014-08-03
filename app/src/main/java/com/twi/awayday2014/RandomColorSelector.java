package com.twi.awayday2014;

import java.util.Random;

public class RandomColorSelector {

    public static int[] backgroundResources = {
            R.drawable.purple_gradient_background,
            R.drawable.red_gradient_background,
            R.drawable.pink_gradient_background,
            R.drawable.green_gradient_background,
            R.drawable.blue_gradient_background,
            R.drawable.light_green_gradient_background
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
