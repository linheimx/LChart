package com.linheimx.app.library.parts;

import android.graphics.Color;

import com.linheimx.app.library.manager.TransformManager;
import com.linheimx.app.library.manager.ViewPortManager;
import com.linheimx.app.library.utils.LogUtil;

/**
 * Created by LJIAN on 2016/11/14.
 */

public abstract class Axis extends BasePart {


    float _min, _max, _range;

    float[] stepValues = new float[]{};
    int labelCount = 5;
    boolean isPerfectLabel = false;

    int axisColor = Color.BLACK;

    public Axis(ViewPortManager _viewPortManager, TransformManager _transformManager) {
        super(_viewPortManager, _transformManager);
    }

    public void prepareData(float min, float max) {

        _min = min;
        _max = max;
        _range = max - min;
    }


    /**
     * 计算与存储：可见区域内的每一步的数值
     * -----------------------------
     * 注意：可见区域！
     */
    public void stepValues() {
        float min = getVisiableMin();
        float max = getVisiableMax();

        if (isPerfectLabel) {

        } else {
            if (stepValues.length < labelCount) {
                stepValues = new float[labelCount];
            }

            float v = min;
            float interval = _range / (labelCount - 1);

            stepValues[0] = min;
            for (int i = 1; i < labelCount - 1; i++) {
                v = v + interval;
                stepValues[i] = v;
            }
            stepValues[labelCount - 1] = max;
        }

    }


    public abstract float getVisiableMin();

    public abstract float getVisiableMax();


    public float get_min() {
        return _min;
    }

    public float get_max() {
        return _max;
    }

    public float get_range() {
        return _range;
    }

    public boolean isPerfectLabel() {
        return isPerfectLabel;
    }

    public void setPerfectLabel(boolean perfectLabel) {
        isPerfectLabel = perfectLabel;
    }

    public float[] getStepValues() {
        return stepValues;
    }

    public int getLabelCount() {
        return labelCount;
    }

    public int getAxisColor() {
        return axisColor;
    }

    public void setAxisColor(int axisColor) {
        this.axisColor = axisColor;
    }
}
