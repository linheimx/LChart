package com.linheimx.app.library.utils;

/**
 * Created by LJIAN on 2016/11/14.
 */

public class U_XY {
    private float x;
    private float y;

    private static U_XY value;

    private U_XY() {

    }

    /**
     * 尽量少的分配内存
     *
     * @return
     */
    public synchronized static U_XY getInstance() {
        if (value == null) {
            value = new U_XY();
        }
        return value;
    }

    public float getX() {
        return x;
    }

    public U_XY setX(float x) {
        this.x = x;
        return this;
    }

    public float getY() {
        return y;
    }

    public U_XY setY(float y) {
        this.y = y;
        return this;
    }
}
