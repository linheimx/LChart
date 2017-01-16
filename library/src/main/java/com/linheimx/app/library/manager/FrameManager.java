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

    public FrameManager() {
        mFrameRect = new RectF();
    }


    public void setFrame(RectF rectF) {
        mFrameRect.set(rectF);
    }

    public float offsetLeft() {
        return mFrameRect.left;
    }

    public float offsetTop() {
        return mFrameRect.top;
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

    public Single_XY getFrameCenter() {
        Single_XY xy = Single_XY.getInstance().setX(mFrameRect.centerX()).setY(mFrameRect.centerY());
        return xy;
    }
}
