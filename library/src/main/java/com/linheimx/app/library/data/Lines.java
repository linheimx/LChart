package com.linheimx.app.library.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/13.
 */

public class Lines {

    private List<Line> lines;

    protected double mYMax = -Double.MAX_VALUE;
    protected double mYMin = Double.MAX_VALUE;
    protected double mXMax = -Double.MAX_VALUE;
    protected double mXMin = Double.MAX_VALUE;

    public Lines() {
    }

    public Lines(List<Line> lines) {
        setLines(lines);
    }

    public List<Line> getLines() {
        return lines;
    }

    public void setLines(List<Line> lines) {
        this.lines = lines;
        calMinMax(lines);
    }

    public void addLine(Line line) {
        if (lines == null) {
            lines = new ArrayList<>();
        }

        lines.add(line);
        calMinMax(lines);
    }

    private void calMinMax(List<Line> lines) {

        mYMax = -Double.MAX_VALUE;
        mYMin = Double.MAX_VALUE;
        mXMax = -Double.MAX_VALUE;
        mXMin = Double.MAX_VALUE;

        for (Line line : lines) {
            if (line.getmXMin() < mXMin) {
                mXMin = line.getmXMin();
            }
            if (line.getmXMax() > mXMax) {
                mXMax = line.getmXMax();
            }

            if (line.getmYMin() < mYMin) {
                mYMin = line.getmYMin();
            }
            if (line.getmYMax() > mYMax) {
                mYMax = line.getmYMax();
            }
        }
    }

    public void calMinMax() {

        if (lines != null) {
            calMinMax(lines);
        }
    }


    public double getmYMax() {
        return mYMax;
    }

    public double getmYMin() {
        return mYMin;
    }

    public double getmXMax() {
        return mXMax;
    }

    public double getmXMin() {
        return mXMin;
    }
}
