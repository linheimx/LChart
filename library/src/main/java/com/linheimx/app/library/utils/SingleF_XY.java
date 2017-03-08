package com.linheimx.app.library.utils;

/**
 * 考虑到性能的原因，采用单例模式
 * --------------------
 * 尽量少的分配内存
 * <p>
 * <p>
 * Created by lijian on 2016/11/14.
 */

public class SingleF_XY {
    private float x;
    private float y;

    private static SingleF_XY value;

    private SingleF_XY() {

    }

    public synchronized static SingleF_XY getInstance() {
        if (value == null) {
            value = new SingleF_XY();
        }
        return value;
    }

    public float getX() {
        return x;
    }

    public SingleF_XY setX(float x) {
        this.x = x;
        return this;
    }

    public float getY() {
        return y;
    }

    public SingleF_XY setY(float y) {
        this.y = y;
        return this;
    }

    @Override
    public String toString() {
        return "U_XY  x: " + x + " y:" + y;
    }
}
