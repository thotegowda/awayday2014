package com.twi.awayday2014;

import android.content.Context;
import android.graphics.Typeface;

public class Fonts {
    private static Typeface OPENSANS_BOLD = null;
    private static Typeface OPENSANS_ITALIC = null;
    private static Typeface OPENSANS_LIGHT = null;
    private static Typeface OPENSANS_LIGHT_ITALIC = null;
    private static Typeface OPENSANS_REGULAR = null;
    private static Typeface OPENSANS_SEMI_BOLD = null;

    public static Typeface openSansBold(Context context) {
        if (OPENSANS_BOLD == null) {
            OPENSANS_BOLD = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Bold.ttf");
        }
        return OPENSANS_BOLD;
    }

    public static Typeface openSansItalic(Context context) {
        if (OPENSANS_ITALIC == null) {
            OPENSANS_ITALIC = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Italic.ttf");
        }
        return OPENSANS_ITALIC;
    }

    public static Typeface openSansLight(Context context) {
        if (OPENSANS_LIGHT == null) {
            OPENSANS_LIGHT = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Light.ttf");
        }
        return OPENSANS_LIGHT;
    }

    public static Typeface openSansLightItalic(Context context) {
        if (OPENSANS_LIGHT_ITALIC == null) {
            OPENSANS_LIGHT_ITALIC = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-LightItalic.ttf");
        }
        return OPENSANS_LIGHT_ITALIC;
    }

    public static Typeface openSansRegular(Context context) {
        if (OPENSANS_REGULAR == null) {
            OPENSANS_REGULAR = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");
        }
        return OPENSANS_REGULAR;
    }

    public static Typeface openSansSemiBold(Context context) {
        if (OPENSANS_SEMI_BOLD == null) {
            OPENSANS_SEMI_BOLD = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Semibold.ttf");
        }
        return OPENSANS_SEMI_BOLD;
    }
}
