package com.twi.awayday2014.utils;

import android.graphics.Color;

import java.util.Random;

public class RandomColorSelector {

    public static int[] backgroundResources = {
            Color.parseColor("#aae51c23"), Color.parseColor("#aae91e63"),
            Color.parseColor("#aa9c27b0"), Color.parseColor("#aa673ab7"),
            Color.parseColor("#aa3f51b5"), Color.parseColor("#aa5677fc"),
            Color.parseColor("#aa03a9f4"), Color.parseColor("#aa00bcd4"),
            Color.parseColor("#aa009688"), Color.parseColor("#aa259b24"),
            Color.parseColor("#aa8bc34a"), Color.parseColor("#aacddc39"),
            Color.parseColor("#aaffeb3b"), Color.parseColor("#aaffc107")
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