package com.twi.awayday2014.models;

public class ImageSize {

    private final int width;
    private final int height;


    public ImageSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public float getWidthToHeightRatio(){
        return (float)width/height;
    }

    public int getNumberOfPixels() {
        return height*width;
    }

    public float getPredictedImageSizeInMb() {
        return (height*width*4)/1000000f;
    }


}
