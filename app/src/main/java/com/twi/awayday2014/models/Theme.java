package com.twi.awayday2014.models;

import java.util.List;

public class Theme {
    private String mainText;
    private String headerText;
    private String footerText;
    private List<String> images;

    public Theme(String mainText, String headerText, String footerText, List<String> images) {
        this.mainText = mainText;
        this.headerText = headerText;
        this.footerText = footerText;
        this.images = images;
    }

    public String getMainText() {
        return mainText;
    }

    public String getHeaderText() {
        return headerText;
    }

    public String getFooterText() {
        return footerText;
    }

    public List<String> getImages() {
        return images;
    }
}
