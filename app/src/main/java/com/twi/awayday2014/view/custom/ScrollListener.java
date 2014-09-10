package com.twi.awayday2014.view.custom;

public interface ScrollListener {
    void onScroll(ScrollableView scrollableView, float y);
    void addParallelScrollableChild(ScrollableView scrollableView, int position);
    void removeParallelScrollableChild(ScrollableView scrollableView);
}
