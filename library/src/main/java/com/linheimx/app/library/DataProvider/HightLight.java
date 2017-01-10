package com.linheimx.app.library.dataprovider;

import android.graphics.Color;

import com.linheimx.app.library.utils.Utils;

/**
 * Created by LJIAN on 2016/12/15.
 */

public class HightLight {

    public static final float D_HIGHLIGHT_WIDTH = 2;
    public static final float D_HINT_TEXT_SIZE = 15;

    //////////////////////// high light ////////////////////////
    int highLightColor = Color.BLUE;
    float highLightWidth;

    //////////////////////// hint ////////////////////////
    int hintColor = Color.BLACK;
    float hintTextSize;


    public HightLight() {

        highLightWidth = Utils.dp2px(D_HIGHLIGHT_WIDTH);
        hintTextSize = Utils.dp2px(D_HINT_TEXT_SIZE);
    }


    public int getHighLightColor() {
        return highLightColor;
    }

    public void setHighLightColor(int highLightColor) {
        this.highLightColor = highLightColor;
    }

    public float getHighLightWidth() {
        return highLightWidth;
    }

    public void setHighLightWidth(float highLightWidth) {
        this.highLightWidth = highLightWidth;
    }

    public int getHintColor() {
        return hintColor;
    }

    public void setHintColor(int hintColor) {
        this.hintColor = hintColor;
    }

    public float getHintTextSize() {
        return hintTextSize;
    }

    public void setHintTextSize(float hintTextSize) {
        this.hintTextSize = hintTextSize;
    }
}
