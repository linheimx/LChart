package com.linheimx.app.library.manager;

import android.graphics.RectF;

import com.linheimx.app.library.utils.Single_XY;

/**
 * 管理着相框
 * <p>
 * Created by Administrator on 2016/11/13.
 */

public class FrameManager {

    protected RectF mFrameRect;//相框
    protected float mChartWidth;
    protected float mChartHeight;

    public FrameManager() {
        mFrameRect = new RectF();
    }


    public void setChartWH(float width, float height) {

        float offsetLeft = offsetLeft();
        float offsetTop = offsetTop();
        float offsetRight = offsetRight();
        float offsetBottom = offsetBottom();

        mChartHeight = height;
        mChartWidth = width;

        setFrame(offsetLeft, offsetTop, offsetRight, offsetBottom);
    }

    public void setFrame(float offsetLeft, float offsetTop,
                         float offsetRight, float offsetBottom) {

        mFrameRect.set(offsetLeft, offsetTop, mChartWidth - offsetRight, mChartHeight
                - offsetBottom);
    }


    public float offsetLeft() {
        return mFrameRect.left;
    }

    public float offsetRight() {
        return mChartWidth - mFrameRect.right;
    }

    public float offsetTop() {
        return mFrameRect.top;
    }

    public float offsetBottom() {
        return mChartHeight - mFrameRect.bottom;
    }

    public float frameTop() {
        return mFrameRect.top;
    }

    public float frameLeft() {
        return mFrameRect.left;
    }

    public float frameRight() {
        return mFrameRect.right;
    }

    public float frameBottom() {
        return mFrameRect.bottom;
    }

    public float frameWidth() {
        return mFrameRect.width();
    }

    public float frameHeight() {
        return mFrameRect.height();
    }

    public RectF getFrameRect() {
        return mFrameRect;
    }

    public float getChartWidth() {
        return mChartWidth;
    }

    public float getChartHeight() {
        return mChartHeight;
    }

    public Single_XY getFrameCenter() {
        Single_XY xy = Single_XY.getInstance().setX(mFrameRect.centerX()).setY(mFrameRect.centerY());
        return xy;
    }
}
