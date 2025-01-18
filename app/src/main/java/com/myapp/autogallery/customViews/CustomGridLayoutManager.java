package com.myapp.autogallery.customViews;


import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class CustomGridLayoutManager extends StaggeredGridLayoutManager {
    private boolean isScrollEnabled = true;

    public CustomGridLayoutManager(int spanCount, int orientation) {
        super(spanCount, orientation);
    }

    public void setScrollEnable(boolean flag) {
        isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        return isScrollEnabled && super.canScrollVertically();
    }

    @Override
    public boolean canScrollHorizontally() {
        return isScrollEnabled && super.canScrollHorizontally();
    }
}
