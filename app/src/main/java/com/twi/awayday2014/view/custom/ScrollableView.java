package com.twi.awayday2014.view.custom;

public interface ScrollableView {
    void setActive(boolean isActive);
    boolean getActive();
    void setScrollListener(ScrollListener listener);
    void scrollTo(float y);
}
