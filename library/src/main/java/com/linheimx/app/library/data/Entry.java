package com.linheimx.app.library.data;

import com.linheimx.app.library.adapter.DefaultHighLightValueAdapter;
import com.linheimx.app.library.adapter.IValueAdapter;

/**
 * Created by lijian on 2016/11/13.
 */

public class Entry {

    protected double x;
    protected double y;

    public Entry(double x, double y) {
        this.x = x;
        this.y = y;
    }


    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }


    @Override
    public String toString() {
        return "entry x:" + x + " y:" + y;
    }
}
