package com.twi.awayday2014.models;

import android.util.Log;

import com.twi.awayday2014.R;

import java.util.Comparator;

public class Presenter{
    private String id;
    private String imageUrl;
    private String imageId;
    private String writeUp;
    private String awayDayWriteup;
    private String link;
    private String name;
    private boolean isGuest;
    private boolean isListable;

    public Presenter() {
    }

    public Presenter(String id, String name, String awayDayWriteup, String link, String writeUp, boolean isGuest, boolean isListable) {
        this.id = id;
        this.name = name;
        this.awayDayWriteup = awayDayWriteup;
        this.link = link;
        this.writeUp = writeUp;
        this.isGuest = isGuest;
        this.isListable = isListable;
    }

    public int profileResource() {
        return R.drawable.notifications_image_sessions;
    }

    public String getName() {
        return name;
    }

    public String getWriteUp() {
        return writeUp;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWriteUp(String writeUp) {
        this.writeUp = writeUp;
    }

    public String getId() {
        return id;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getAwayDayWriteup() {
        return awayDayWriteup;
    }

    public String getLink() {
        return link;
    }

    public boolean isGuest() {
        return isGuest;
    }

    public boolean isListable() {
        return isListable;
    }

    public enum PresenterComparator implements Comparator<Presenter>{
        TYPE_SORT {
            @Override
            public int compare(Presenter lhs, Presenter rhs) {
                if(lhs.isGuest && !rhs.isGuest){
                    return -1;
                }else if(!lhs.isGuest && rhs.isGuest){
                    return 1;
                }
                return 0;
            }
        },
        NAME_SORT {
            @Override
            public int compare(Presenter lhs, Presenter rhs) {
                return lhs.name.compareTo(rhs.name);
            }
        };

        public static Comparator<Presenter> getComparator(final PresenterComparator... multipleComparators) {
            return new Comparator<Presenter>() {
                public int compare(Presenter o1, Presenter o2) {
                    for (PresenterComparator comparator : multipleComparators) {
                        int result = comparator.compare(o1, o2);
                        if (result != 0) {
                            return result;
                        }
                    }
                    return 0;
                }
            };
        }

    }

}
