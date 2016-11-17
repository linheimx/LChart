package com.linheimx.app.library.parts;

import android.graphics.Color;

import com.linheimx.app.library.adapter.DefaultValueAdapter;
import com.linheimx.app.library.adapter.IValueAdapter;
import com.linheimx.app.library.manager.TransformManager;
import com.linheimx.app.library.manager.ViewPortManager;

/**
 * Created by LJIAN on 2016/11/14.
 */

public abstract class BasePart {

    boolean isEnable = true;
    int textSize = 10;
    int textColor = Color.BLACK;
    int paintWidth = 2;

    IValueAdapter _ValueAdapter;
    ViewPortManager _ViewPortManager;
    TransformManager _TransformManager;

    public BasePart(ViewPortManager viewPortManager, TransformManager transformManager) {
        this._ViewPortManager = viewPortManager;
        this._TransformManager = transformManager;

        _ValueAdapter = new DefaultValueAdapter(2);
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getPaintWidth() {
        return paintWidth;
    }

    public void setPaintWidth(int paintWidth) {
        this.paintWidth = paintWidth;
    }

    public IValueAdapter get_ValueAdapter() {
        return _ValueAdapter;
    }
}
