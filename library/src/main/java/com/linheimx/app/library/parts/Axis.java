package com.linheimx.app.library.parts;

import android.graphics.Color;
import android.util.Log;

import com.linheimx.app.library.manager.TransformManager;
import com.linheimx.app.library.manager.ViewPortManager;
import com.linheimx.app.library.utils.LogUtil;
import com.linheimx.app.library.utils.Utils;

/**
 * Created by LJIAN on 2016/11/14.
 */

public abstract class Axis extends BasePart {


    float _min, _max, _range;

    float[] stepValues = new float[]{};
    int labelCount = 5;
    boolean isPerfectLabel = true;

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

        if (Math.abs(max - min) == 0) {
            return;
        }

        if (isPerfectLabel) {

            double rawInterval = _range / (labelCount - 1);
            // 1.以最大数值为量程
            double interval = Utils.roundToNextSignificant(rawInterval);//314->300
            // 2. 量程>5，则以10为单位
            double intervalMagnitude = Math.pow(10, (int) Math.log10(interval));//100
            int intervalSigDigit = (int) (interval / intervalMagnitude);
            if (intervalSigDigit > 5) {
                interval = Math.floor(10 * intervalMagnitude);// 以10位单位 //
            }

            double first = Math.floor(min / interval) * interval;//有几个interval
            double last = Math.ceil(max / interval) * interval;

            double f;
            int n = 0;
            if (interval != 0.0) {
                for (f = first; f <= last; f += interval) {
                    ++n;
                }
            }
            labelCount = n;

            if (stepValues.length < labelCount) {
                stepValues = new float[labelCount];
            }

            f = first;
            for (int i = 0; i < n; f += interval, ++i) {

                if (f == 0.0) // Fix for negative zero case (Where value == -0.0, and 0.0 == -0.0)
                    f = 0.0;

                stepValues[i] = (float) f;
            }

            //--------------------------> 修复第一个和最后一个不显示问题 <----------------------
            // 第一个
            if (first < min) {
                stepValues[0] = min;
            }
            // 第二个
            if (n > 1 && stepValues[n - 1] > max) {
                stepValues[n - 1] = max;
            }


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
