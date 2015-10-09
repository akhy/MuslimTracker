package com.chickenger.islam.tracker.util;

import android.view.View;

public class ViewUtils {


    public static void setVisibility(View view, boolean isVisible, int invisibleMode) {
        if (isVisible) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(invisibleMode);
        }
    }

    private ViewUtils() {
    }
}
