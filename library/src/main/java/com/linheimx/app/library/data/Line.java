package com.linheimx.app.library.data;

import android.graphics.Color;
import android.util.Log;

import com.linheimx.app.library.utils.LogUtil;

import java.util.List;

/**
 * Created by Administrator on 2016/11/13.
 */

public class Line {

    private List<Entry> entries;


    private float mYMax = -Float.MAX_VALUE;
    private float mYMin = Float.MAX_VALUE;
    private float mXMax = -Float.MAX_VALUE;
    private float mXMin = Float.MAX_VALUE;

    private int lineColor = Color.RED;
    private int lineWidth = 1;
    private int circleColor = Color.RED;
    private int circleR = 5;

    private boolean isDrawCircle = true;

    public Line() {
    }

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
                mYMin = entry.getY();
            }
            if (entry.getY() > mYMax) {
                mYMax = entry.getY();
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


    /**
     * 根据提供的 x数值，找出list中离它最近的数，返回其下标。
     * --------------
     * 考虑到性能的原因，采用二分查找法。(速度很快，不错！)
     *
     * @param entries
     * @param hitValue
     * @param rounding
     * @return
     */
    public static int getEntryIndex(List<Entry> entries, float hitValue, Rounding rounding) {

        if (entries == null || entries.isEmpty())
            return -1;

        int low = 0;
        int high = entries.size() - 1;
        int closet = low;

        while (low < high) {
            int m = (low + high) / 2;

            float fm = entries.get(m).getX();// middle
            float fr = entries.get(m + 1).getX();// right

            if (hitValue >= fm && hitValue <= fr) {

                // 中_右
                float d1 = Math.abs(hitValue - fm);
                float d2 = Math.abs(hitValue - fr);

                if (d1 <= d2) {
                    closet = m;
                } else {
                    closet = m + 1;
                }

                low = high = closet;
                break;

            } else if (hitValue < fm) {

                if (m >= 1) {
                    float fl = entries.get(m - 1).getX();// left

                    if (hitValue >= fl && hitValue <= fm) {
                        // 中_左
                        float d0 = Math.abs(hitValue - fl);
                        float d1 = Math.abs(hitValue - fm);

                        if (d0 <= d1) {
                            closet = m - 1;
                        } else {
                            closet = m;
                        }

                        low = high = closet;
                        break;
                    }
                }

                high = m - 1;
                closet = high;
            } else if (hitValue > fr) {
                low = m + 1;
                closet = low;
            }
        }

        high++;
        low--;

        int result = low;
        if (rounding == Rounding.UP) {
            result = Math.min(high, entries.size() - 1);//多一个
        } else if (rounding == Rounding.DOWN) {
            result = Math.max(low, 0);//少一个
        } else if (rounding == Rounding.CLOSEST) {
            result = closet;
        }
        return result;
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


    public int getLineColor() {
        return lineColor;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    public int getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }

    public boolean isDrawCircle() {
        return isDrawCircle;
    }

    public void setDrawCircle(boolean drawCircle) {
        isDrawCircle = drawCircle;
    }

    public int getCircleR() {
        return circleR;
    }

    public void setCircleR(int circleR) {
        this.circleR = circleR;
    }

    public int getCircleColor() {
        return circleColor;
    }

    public void setCircleColor(int circleColor) {
        this.circleColor = circleColor;
    }

    public enum Rounding {
        UP,
        DOWN,
        CLOSEST,
    }

}
