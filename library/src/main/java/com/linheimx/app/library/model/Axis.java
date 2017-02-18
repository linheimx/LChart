package com.linheimx.app.library.model;

import android.graphics.Color;

import com.linheimx.app.library.adapter.DefaultValueAdapter;
import com.linheimx.app.library.adapter.IValueAdapter;
import com.linheimx.app.library.utils.Utils;

import java.util.List;

/**
 * 轴线相关
 * -----------------------
 * 区域事先约定，优于计算！
 * <p>
 * Created by Administrator on 2016/12/5.
 */

public abstract class Axis {

    public static final float D_AXIS_WIDTH = 2;// d 代表default
    public static final float D_LEG_WIDTH = 5;
    public static final float D_LABEL_TXT = 7;
    public static final float D_UNIT_TXT = 14;

    public static final int D_LABEL_COUNT = 6;// 建议label的数量


    //////////////////////////  label 相关  /////////////////////////
    double[] labelValues = new double[]{};
    int labelCount = 6;
    int _labelCountAdvice = D_LABEL_COUNT;
    boolean isPerfectLabel = true;
    float labelArea;
    int labelColor = Color.BLUE;
    float labelTextSize;

    ///////////////////////////////  unit 相关  ////////////////////////////
    boolean _enableUnit = true;
    String _unit = "";
    float unitArea;
    float unitTxtSize;
    int unitColor = Color.RED;

    /////////////////////////////////  轴线相关  //////////////////////////////////
    int axisColor = Color.BLACK;
    float axisWidth;
    float leg;// 轴线上的小腿（多出来的小不点：叫他小腿吧)

    /////////////////////////////////  预警线相关  ////////////////////////////////
    List<WarnLine> listWarnLins;


    boolean enable = true;// axis is enable?
    IValueAdapter _ValueAdapter;

    public Axis() {

        axisWidth = Utils.dp2px(D_AXIS_WIDTH);
        labelTextSize = Utils.dp2px(D_LABEL_TXT);
        leg = Utils.dp2px(D_LEG_WIDTH);
        unitTxtSize = Utils.dp2px(D_UNIT_TXT);

        // value adapter
        _ValueAdapter = new DefaultValueAdapter(2);
    }

    /**
     * 计算与存储：可见区域内的每一步的数值
     * -----------------------------
     * 注意：可见区域！
     */
    public void calValues(double min, double max) {

        double range;
        range = max - min;

        if (Math.abs(max - min) == 0) {
            return;
        }

        if (isPerfectLabel) {

            double rawInterval = range / (_labelCountAdvice - 1);
            // 1.以最大数值为量程
            double interval = Utils.roundNumber2One(rawInterval);//314->300
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

            if (labelValues.length < labelCount) {
                labelValues = new double[labelCount];
            }

            f = first;
            for (int i = 0; i < n; f += interval, ++i) {

                if (f == 0.0) // Fix for negative zero case (Where value == -0.0, and 0.0 == -0.0)
                    f = 0.0;

                labelValues[i] = (float) f;
            }

//            //--------------------------> 修复第一个和最后一个不显示问题 <----------------------
//            // 第一个
//            if (first < min) {
//                labelValues[0] = min;
//            }
//            // 第二个
//            if (n > 1 && labelValues[n - 1] > max) {
//                labelValues[n - 1] = max;
//            }

        } else {
            if (labelValues.length < labelCount) {
                labelValues = new double[labelCount];
            }

            double v = min;
            double interval = range / (labelCount - 1);

            labelValues[0] = min;
            for (int i = 1; i < labelCount - 1; i++) {
                v = v + interval;
                labelValues[i] = v;
            }
            labelValues[labelCount - 1] = max;
        }
    }

    /**
     * 轴线左边 label和indicator的距离
     *
     * @return
     */
    public float offsetLeft() {
        float dimen;
        dimen = getArea_Label();

        if (_enableUnit) {
            dimen += getArea_Unit();
        }
        return dimen;
    }

    /**
     * 轴线底部 label和indicator的距离
     *
     * @return
     */
    public float offsetBottom() {
        float dimen;
        dimen = getArea_Label();

        if (_enableUnit) {
            dimen += getArea_Unit();
        }
        return dimen;
    }


    public float getArea_Label() {
        return labelArea;
    }

    public float getArea_Unit() {
        return unitArea;
    }

    ///////////////////////////////  get set  //////////////////////////////////////


    public IValueAdapter get_ValueAdapter() {
        return _ValueAdapter;
    }

    public void set_ValueAdapter(IValueAdapter _ValueAdapter) {
        this._ValueAdapter = _ValueAdapter;
    }

    public boolean is_enableUnit() {
        return _enableUnit;
    }

    public void set_enableUnit(boolean _enableUnit) {
        this._enableUnit = _enableUnit;
    }

    public int get_labelCountAdvice() {
        return _labelCountAdvice;
    }

    public void set_labelCountAdvice(int _labelCountAdvice) {
        this._labelCountAdvice = _labelCountAdvice;
    }

    public String get_unit() {
        return _unit;
    }

    public void set_unit(String _unit) {
        this._unit = _unit;
    }

    public float getUnitTxtSize() {
        return unitTxtSize;
    }

    public void setUnitTxtSize(float unitTxtSize) {
        this.unitTxtSize = unitTxtSize;
    }

    public int getUnitColor() {
        return unitColor;
    }

    public void setUnitColor(int unitColor) {
        this.unitColor = unitColor;
    }

    public int getAxisColor() {
        return axisColor;
    }

    public void setAxisColor(int axisColor) {
        this.axisColor = axisColor;
    }

    public float getAxisWidth() {
        return axisWidth;
    }

    public void setAxisWidth(float axisWidth) {
        this.axisWidth = axisWidth;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public float getLeg() {
        return leg;
    }

    public void setLeg(float leg) {
        this.leg = leg;
    }

    public boolean isPerfectLabel() {
        return isPerfectLabel;
    }

    public void setPerfectLabel(boolean perfectLabel) {
        isPerfectLabel = perfectLabel;
    }

    public float getLabelTextSize() {
        return labelTextSize;
    }

    public void setLabelTextSize(float labelTextSize) {
        this.labelTextSize = labelTextSize;
    }

    public int getLabelColor() {
        return labelColor;
    }

    public void setLabelColor(int labelColor) {
        this.labelColor = labelColor;
    }

    public int getLabelCount() {
        return labelCount;
    }

    public void setLabelCount(int labelCount) {
        this.labelCount = labelCount;
    }

    public double[] getLabelValues() {
        return labelValues;
    }

    public void setLabelValues(double[] labelValues) {
        this.labelValues = labelValues;
    }

    public List<WarnLine> getListWarnLins() {
        return listWarnLins;
    }

    public void setListWarnLins(List<WarnLine> listWarnLins) {
        this.listWarnLins = listWarnLins;
    }
}
