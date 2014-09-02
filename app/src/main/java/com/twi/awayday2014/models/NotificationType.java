package com.twi.awayday2014.models;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

import com.twi.awayday2014.R;

public enum NotificationType {
    TRAVEL(R.drawable.notifications_image_travel, Color.parseColor("#25688d"), "TRAVEL"),
    FOOD(R.drawable.notifications_image_food, Color.parseColor("#007f64"), "FOOD"),
    SESSIONS(R.drawable.notifications_image_sessions, Color.parseColor("#ad512f"), "SESSIONS"),
    APP_UPDATE(R.drawable.notifications_image_travel, Color.parseColor("#ff4e50"), "APP UPDATE"),
    INFO(R.drawable.notifications_image_travel, Color.parseColor("#e6ac27"), "INFO");

    private int backgroundResourceId;
    private int tagColor;
    private Drawable backgroundBitmap;
    private String displayText;

    NotificationType(int backgroundResourceId, int tagColor, String displayText) {
        this.backgroundResourceId = backgroundResourceId;
        this.tagColor = tagColor;
        this.displayText = displayText;
    }

    public int getBackgroundResourceId() {
        return backgroundResourceId;
    }

    public int getTagColor() {
        return tagColor;
    }

    public Drawable getBackgroundBitmap() {
        return backgroundBitmap;
    }

    public void setBackgroundBitmap(Drawable backgroundBitmap) {
        this.backgroundBitmap = backgroundBitmap;
    }

    public String getDisplayText() {
        return displayText;
    }

    public static NotificationType fromDisplayText(String displayText){
        for (NotificationType notificationType : values()) {
            if(notificationType.displayText.toLowerCase().equals(displayText.toLowerCase())){
                return notificationType;
            }
        }
        return null;
    }
}
