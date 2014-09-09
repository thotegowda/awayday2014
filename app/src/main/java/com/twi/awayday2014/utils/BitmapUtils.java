package com.twi.awayday2014.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.twi.awayday2014.models.ImageSize;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class BitmapUtils {
    private static final String TAG = "BitmapUtils";

    public static ImageSize decodeBitmapSizeFromUristring(String targetUri) {
        FileInputStream is = null;
        try {
            is = new FileInputStream(targetUri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return decodeBitmapSizeFromStream(is);
    }

    public static ImageSize decodeBitmapSizeFromStream(InputStream stream) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        try {
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(stream, null, options);
        }finally {
            if(stream != null) try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ImageSize(options.outWidth, options.outHeight);
    }

    public static Bitmap decodeSampledBitmapFromUristring(String targetUri, int imageSampleSize) {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(targetUri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return decodeSampledBitmapFromStream(fileInputStream, imageSampleSize);
    }

    public static Bitmap decodeSampledBitmapFromStream(InputStream stream, int imageSampleSize) {
        Bitmap bmp = null;
        final BitmapFactory.Options options = new BitmapFactory.Options();

        // Calculate inSampleSize
        options.inSampleSize = imageSampleSize;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        try {
            bmp = BitmapFactory.decodeStream(stream, null, options);
        }finally {
            if (stream != null)
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        return bmp;
    }

    public static String sampleImage(String imagePath, int sizeInMb) throws IOException {
        ImageSize originalImageSize = decodeBitmapSizeFromUristring(imagePath);
        if(originalImageSize.getPredictedImageSizeInMb() <= sizeInMb){
            Log.d(TAG, "No sampling required. Current image size is " + originalImageSize.getPredictedImageSizeInMb() + " mb"
                    + ". Required is " + sizeInMb + " mb");
            return imagePath;
        }

        int sampleSize = (int) (originalImageSize.getPredictedImageSizeInMb()/sizeInMb);
        Log.d(TAG, "Sampling required. Current image size is " + originalImageSize.getPredictedImageSizeInMb() + " mb"
                + ". Required is " + sizeInMb + " mb. Sampling with factor " + sampleSize);
        
        Bitmap result = decodeSampledBitmapFromUristring(imagePath, sampleSize);
        ImageSize imageSize = new ImageSize(result.getWidth(), result.getHeight());

        while (imageSize.getPredictedImageSizeInMb() >= sizeInMb){
            result = decodeSampledBitmapFromUristring(imagePath, 2);
            imageSize = new ImageSize(result.getWidth(), result.getHeight());
        }

        Log.d(TAG, "Image sampled to " + imageSize.getPredictedImageSizeInMb() + " mb");

        File file = new File(imagePath + " _sampled");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        result.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
        fileOutputStream.flush();
        fileOutputStream.close();
        return file.getAbsolutePath();
    }
}
