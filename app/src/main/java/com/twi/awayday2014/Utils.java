package com.twi.awayday2014;

import android.content.Context;
import android.content.Intent;
import com.twi.awayday2014.ui.PhotoViewerActivity;


public class Utils {

    public static String EXTRA_URL = "photo_url";

    public static void launchPhotoViewer(Context context, String url) {
        context.startActivity(new Intent(context, PhotoViewerActivity.class).putExtra(EXTRA_URL, url));
    }
}
