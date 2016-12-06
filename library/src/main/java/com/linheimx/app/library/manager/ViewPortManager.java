package com.linheimx.app.library.manager;

import android.graphics.RectF;

import com.linheimx.app.library.utils.Single_XY;
import com.linheimx.app.library.utils.Utils;

/**
 * Created by Administrator on 2016/11/13.
 */

public class ViewPortManager {

    protected RectF mContentRect;
    protected float mChartWidth;
    protected float mChartHeight;

    public ViewPortManager() {
        mContentRect = new RectF();
    }


    public void setChartWH(float width, float height) {

        float offsetLeft = offsetLeft();
        float offsetTop = offsetTop();
        float offsetRight = offsetRight();
        float offsetBottom = offsetBottom();

        mChartHeight = height;
        mChartWidth = width;

        setViewPort(offsetLeft, offsetTop, offsetRight, offsetBottom);
    }

    public void setViewPort(float offsetLeft, float offsetTop,
                            float offsetRight, float offsetBottom) {

        mContentRect.set(offsetLeft, offsetTop, mChartWidth - offsetRight, mChartHeight
                - offsetBottom);
    }


    public float offsetLeft() {
        return mContentRect.left;
    }

    public float offsetRight() {
        return mChartWidth - mContentRect.right;
    }

    public float offsetTop() {
        return mContentRect.top;
    }

    public float offsetBottom() {
        return mChartHeight - mContentRect.bottom;
    }

    public float contentTop() {
        return mContentRect.top;
    }

    public float contentLeft() {
        return mContentRect.left;
    }

    public float contentRight() {
        return mContentRect.right;
    }

    public float contentBottom() {
        return mContentRect.bottom;
    }

    public float contentWidth() {
        return mContentRect.width();
    }

    public float contentHeight() {
        return mContentRect.height();
    }

    public RectF getContentRect() {
        return mContentRect;
    }

    public float getChartWidth() {
        return mChartWidth;
    }

    public float getChartHeight() {
        return mChartHeight;
    }

    public Single_XY getContentCenter() {
        Single_XY xy = Single_XY.getInstance().setX(mContentRect.centerX()).setY(mContentRect.centerY());
        return xy;
    }
}
