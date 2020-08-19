package com.artem.nsu.redditfeed.util;

import android.graphics.Color;

public class ColorFactory {

    private static final String COLOR_PLACEHOLDER = "#7e7e7e";
    private static final String COLOR_ERROR = "#B00020";

    public static int getPlaceholderColor() {
        return Color.parseColor(COLOR_PLACEHOLDER);
    }

    public static int getErrorColor() {
        return Color.parseColor(COLOR_ERROR);
    }

}
