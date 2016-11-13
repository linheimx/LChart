package com.linheimx.app.library.data;

/**
 * Created by Administrator on 2016/11/13.
 */

public class Entry {
    protected float x;
    protected float y;

    public Entry(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
