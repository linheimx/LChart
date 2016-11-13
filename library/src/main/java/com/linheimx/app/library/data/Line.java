package com.linheimx.app.library.data;

import java.util.List;

/**
 * Created by Administrator on 2016/11/13.
 */

public class Line {

    private List<Entry> entries;


    protected float mYMax = -Float.MAX_VALUE;
    protected float mYMin = Float.MAX_VALUE;
    protected float mXMax = -Float.MAX_VALUE;
    protected float mXMin = Float.MAX_VALUE;


    public Line(List<Entry> entries) {
        setEntries(entries);
    }

    private void calMinMax(List<Entry> entries) {
        for (Entry entry : entries) {

            if (entry.getX() < mXMin) {
                mXMin = entry.getX();
            }
            if (entry.getX() > mXMax) {
                mXMax = entry.getX();
            }

            if (entry.getY() < mYMin) {
                mYMin = entry.getX();
            }
            if (entry.getY() > mYMax) {
                mYMax = entry.getX();
            }
        }
    }


    public List<Entry> getEntries() {
        return entries;
    }

    public void setEntries(List<Entry> entries) {
        // check
        if (entries == null) {
            throw new IllegalArgumentException("entries must be no null");
        }

        this.entries = entries;

        calMinMax(entries);
    }

    public float getmXMax() {
        return mXMax;
    }

    public float getmXMin() {
        return mXMin;
    }

    public float getmYMax() {
        return mYMax;
    }

    public float getmYMin() {
        return mYMin;
    }
}
