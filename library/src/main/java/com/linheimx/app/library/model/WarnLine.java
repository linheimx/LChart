package com.linheimx.app.library.model;

import android.graphics.Color;

import com.linheimx.app.library.utils.Utils;

/**
 * Created by lijian on 2017/2/18.
 */

public class WarnLine {

    public static final float D_WARN_WIDTH = 2;
    public static final float D_WARN_TXT_SIZE = 15;

    int warnColor = Color.RED;
    float warnLineWidth;
    float txtSize ;

    boolean enable = true;
    double value;//预警数值


    public WarnLine(double value) {
        this.value = value;

        warnLineWidth = Utils.dp2px(D_WARN_WIDTH);
        txtSize = Utils.dp2px(D_WARN_TXT_SIZE);
    }

    public WarnLine(double value, int warnColor) {
        this.value = value;
        this.warnColor = warnColor;

        warnLineWidth = Utils.dp2px(D_WARN_WIDTH);
        txtSize = Utils.dp2px(D_WARN_TXT_SIZE);
    }

    public int getWarnColor() {
        return warnColor;
    }

    public void setWarnColor(int warnColor) {
        this.warnColor = warnColor;
    }

    public float getWarnLineWidth() {
        return warnLineWidth;
    }

    public void setWarnLineWidth(float warnLineWidth) {
        this.warnLineWidth = warnLineWidth;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public float getTxtSize() {
        return txtSize;
    }

    public void setTxtSize(float txtSize) {
        this.txtSize = txtSize;
    }
}
