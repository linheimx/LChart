package com.linheimx.app.library.model;

import android.graphics.Color;

import com.linheimx.app.library.adapter.DefaultHighLightValueAdapter;
import com.linheimx.app.library.adapter.IValueAdapter;
import com.linheimx.app.library.utils.Utils;

/**
 * Created by lijian on 2016/12/15.
 */

public class HighLight {

    public static final float D_HIGHLIGHT_WIDTH = 1;
    public static final float D_HINT_TEXT_SIZE = 15;

    //////////////////////// high light ////////////////////////
    int highLightColor = Color.RED;
    float highLightWidth;

    //////////////////////// hint ////////////////////////
    int hintColor = Color.BLACK;
    float hintTextSize;

    boolean enable = false;
    boolean isDrawHighLine = true;
    boolean isDrawHint = true;

    protected IValueAdapter xValueAdapter;// 高亮时，x应该如何显示？
    protected IValueAdapter yValueAdapter;// 高亮时，y应该如何显示？


    public HighLight() {

        highLightWidth = Utils.dp2px(D_HIGHLIGHT_WIDTH);
        hintTextSize = Utils.dp2px(D_HINT_TEXT_SIZE);

        xValueAdapter = new DefaultHighLightValueAdapter();
        yValueAdapter = new DefaultHighLightValueAdapter();
    }

    public HighLight(IValueAdapter xValueAdapter, IValueAdapter yValueAdapter) {
        this.xValueAdapter = xValueAdapter;
        this.yValueAdapter = yValueAdapter;
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

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean isDrawHighLine() {
        return isDrawHighLine;
    }

    public void setDrawHighLine(boolean drawHighLine) {
        isDrawHighLine = drawHighLine;
    }

    public boolean isDrawHint() {
        return isDrawHint;
    }

    public void setDrawHint(boolean drawHint) {
        isDrawHint = drawHint;
    }

    public IValueAdapter getxValueAdapter() {
        return xValueAdapter;
    }

    public HighLight setxValueAdapter(IValueAdapter xValueAdapter) {
        this.xValueAdapter = xValueAdapter;
        return this;
    }

    public IValueAdapter getyValueAdapter() {
        return yValueAdapter;
    }

    public HighLight setyValueAdapter(IValueAdapter yValueAdapter) {
        this.yValueAdapter = yValueAdapter;
        return this;
    }
}
