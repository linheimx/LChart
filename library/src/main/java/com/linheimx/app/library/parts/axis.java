package com.linheimx.app.library.parts;

import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;

import com.linheimx.app.library.adapter.DefaultValueAdapter;
import com.linheimx.app.library.adapter.IValueAdapter;
import com.linheimx.app.library.utils.Utils;

/**
 * Created by Administrator on 2016/12/5.
 */

public abstract class Axis {

    public static final float D_AXIS_WIDTH = 2;
    public static final float D_LEG_WIDTH = 5;
    public static final float D_LABEL_TXT = 7;
    public static final float D_UNIT_TXT = 14;

    boolean enable = true;// axis is enable?

    //////////////////////////  label 相关  /////////////////////////
    float[] labelValues = new float[]{};
    int labelCount = 6;
    int _labelCountAdvice = labelCount;
    boolean isPerfectLabel = true;

    ///////////////////////////////  unit 相关  ////////////////////////////
    boolean _enableUnit = true;
    String _unit = "";

    //////////////////////////////  自动计算  //////////////////////////
    float unitHeight;// need cal ---> calAreaDimens
    float unitWidth;// need cal ---> calAreaDimens
    float labelWidth;// need cal ---> calAreaDimens
    float labelHeight;// need cal ---> calAreaDimens

    float ulSpace = 5;// unit与label之间的间隙

    int axisColor = Color.BLACK;
    float axisWidth;

    int labelColor = Color.BLUE;
    float labelTextSize;
    float unitTxtSize;
    int unitColor = Color.RED;

    float leg;//多出来的小不点(叫他小腿吧)

    Paint _PaintAxis;
    Paint _PaintGridline;
    Paint _PaintLittle;
    Paint _PaintLabel;
    Paint _PaintUnit;

    IValueAdapter _ValueAdapter;

    public Axis() {

        axisWidth = Utils.dp2px(D_AXIS_WIDTH);
        labelTextSize = Utils.dp2px(D_LABEL_TXT);
        leg = Utils.dp2px(D_LEG_WIDTH);
        unitTxtSize = Utils.dp2px(D_UNIT_TXT);

        internalReload();
    }

    /**
     * 内部使用
     */
    public void internalReload() {

        _PaintAxis = new Paint(Paint.ANTI_ALIAS_FLAG);
        _PaintLabel = new Paint(Paint.ANTI_ALIAS_FLAG);
        _PaintLittle = new Paint(Paint.ANTI_ALIAS_FLAG);
        _PaintGridline = new Paint(Paint.ANTI_ALIAS_FLAG);
        _PaintUnit = new Paint(Paint.ANTI_ALIAS_FLAG);

        // Axis
        _PaintAxis.setColor(axisColor);
        _PaintAxis.setStrokeWidth(axisWidth);

        // label
        _PaintLabel.setColor(labelColor);
        _PaintLabel.setTextSize(labelTextSize);

        // little
        _PaintLittle.setColor(axisColor);
        _PaintLittle.setStrokeWidth(axisWidth);

        // grid line
        _PaintGridline.setColor(Color.GRAY);
        _PaintGridline.setStrokeWidth(Utils.dp2px(1));

        // unit
        _PaintUnit.setColor(unitColor);
        _PaintUnit.setTextSize(unitTxtSize);

        // value adapter
        _ValueAdapter = new DefaultValueAdapter(2);
    }

    /**
     * 计算与存储：可见区域内的每一步的数值
     * -----------------------------
     * 注意：可见区域！
     */
    public void calValues(float min, float max) {

        float range;
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
                labelValues = new float[labelCount];
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
                labelValues = new float[labelCount];
            }

            float v = min;
            float interval = range / (labelCount - 1);

            labelValues[0] = min;
            for (int i = 1; i < labelCount - 1; i++) {
                v = v + interval;
                labelValues[i] = v;
            }
            labelValues[labelCount - 1] = max;
        }
    }

    /**
     * 计算轴线上的各部分的尺寸
     *
     * @param longestLabel
     */
    public void calDimens_Label(String longestLabel) {
        // 1. label
        _PaintLabel.setColor(labelColor);
        _PaintLabel.setTextSize(labelTextSize);

        labelWidth = Utils.textWidth(_PaintLabel, longestLabel);
        labelHeight = Utils.textHeight(_PaintLabel);
    }

    /**
     * 计算轴线上的各部分的尺寸
     */
    public void calDimens_Unit() {
        //_unit
        if (!TextUtils.isEmpty(_unit)) {
            _enableUnit = true;

            _PaintUnit.setColor(Color.RED);
            _PaintUnit.setTextSize(labelTextSize * 2f);

            unitHeight = Utils.textHeight(_PaintUnit);
            unitWidth = Utils.textWidth(_PaintUnit, _unit);
        } else {
            _enableUnit = false;
        }
    }


    /**
     * 轴线左边 label和indicator的距离
     *
     * @return
     */
    public float offsetLeft() {
        float dimen;
        dimen = getArea_LableWidth();

        if (_enableUnit) {
            dimen += ulSpace + getArea_UnitHeight();
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
        dimen = getArea_LableHeight();

        if (_enableUnit) {
            dimen += ulSpace + getArea_UnitHeight();
        }
        return dimen;
    }

    /**
     * lebel 区域的宽
     *
     * @return
     */
    public float getArea_LableWidth() {
        return leg * 1.3f + labelWidth;
    }

    /**
     * label 区域的高
     *
     * @return
     */
    public float getArea_LableHeight() {
        return leg * 1.3f + labelHeight;
    }

    /**
     * _unit 区域的高
     *
     * @return
     */
    public float getArea_UnitHeight() {
        return unitHeight;
    }

    /**
     * _unit 区域的宽
     *
     * @return
     */
    public float getArea_UnitWidth() {
        return unitWidth;
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

    public Paint get_PaintAxis() {
        return _PaintAxis;
    }

    public void set_PaintAxis(Paint _PaintAxis) {
        this._PaintAxis = _PaintAxis;
    }

    public Paint get_PaintGridline() {
        return _PaintGridline;
    }

    public void set_PaintGridline(Paint _PaintGridline) {
        this._PaintGridline = _PaintGridline;
    }

    public Paint get_PaintLabel() {
        return _PaintLabel;
    }

    public void set_PaintLabel(Paint _PaintLabel) {
        this._PaintLabel = _PaintLabel;
    }

    public Paint get_PaintLittle() {
        return _PaintLittle;
    }

    public void set_PaintLittle(Paint _PaintLittle) {
        this._PaintLittle = _PaintLittle;
    }

    public Paint get_PaintUnit() {
        return _PaintUnit;
    }

    public void set_PaintUnit(Paint _PaintUnit) {
        this._PaintUnit = _PaintUnit;
    }

    public String get_unit() {
        return _unit;
    }

    public void set_unit(String _unit) {
        this._unit = _unit;
    }

    public float getUnitHeight() {
        return unitHeight;
    }

    public void setUnitHeight(float unitHeight) {
        this.unitHeight = unitHeight;
    }

    public float getUnitWidth() {
        return unitWidth;
    }

    public void setUnitWidth(float unitWidth) {
        this.unitWidth = unitWidth;
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

    public float getLabelHeight() {
        return labelHeight;
    }

    public void setLabelHeight(float labelHeight) {
        this.labelHeight = labelHeight;
    }

    public float getLabelTextSize() {
        return labelTextSize;
    }

    public void setLabelTextSize(float labelTextSize) {
        this.labelTextSize = labelTextSize;
    }

    public float getLabelWidth() {
        return labelWidth;
    }

    public void setLabelWidth(float labelWidth) {
        this.labelWidth = labelWidth;
    }

    public float getUlSpace() {
        return ulSpace;
    }

    public void setUlSpace(float ulSpace) {
        this.ulSpace = ulSpace;
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

    public float[] getLabelValues() {
        return labelValues;
    }

    public void setLabelValues(float[] labelValues) {
        this.labelValues = labelValues;
    }
}
