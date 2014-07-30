package com.twi.awayday2014.utils;

import android.content.Context;
import android.graphics.Typeface;

public class Fonts {
    private static Typeface OPENSANS_LIGHT = null;
    private static Typeface OPENSANS_REGULAR = null;

    public static Typeface openSansLight(Context context) {
        if (OPENSANS_LIGHT == null) {
            OPENSANS_LIGHT = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Light.ttf");
        }
        return OPENSANS_LIGHT;
    }


    public static Typeface openSansRegular(Context context) {
        if (OPENSANS_REGULAR == null) {
            OPENSANS_REGULAR = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");
        }
        return OPENSANS_REGULAR;
    }

}
