package com.linheimx.app.library.render;

import android.graphics.Canvas;
import android.graphics.Color;
import android.text.TextUtils;

import com.linheimx.app.library.adapter.DefaultValueAdapter;
import com.linheimx.app.library.adapter.IValueAdapter;
import com.linheimx.app.library.manager.TransformManager;
import com.linheimx.app.library.manager.ViewPortManager;
import com.linheimx.app.library.parts.Axis;
import com.linheimx.app.library.utils.Utils;

/**
 * Created by LJIAN on 2016/11/14.
 */

public abstract class AxisRender extends BaseRender {

    Axis _Axis;


    public AxisRender(ViewPortManager _ViewPortManager, TransformManager _TransformManager,Axis axis) {
        super(_ViewPortManager, _TransformManager);

        this._Axis =axis;
    }

    @Override
    public void render(Canvas canvas) {
        // 考虑到外观的原因，绘制的次序由 lineChart 中的draw来控制。
    }

    public void renderAxisLine(Canvas canvas) {

    }

    public void renderLabels(Canvas canvas) {

    }

    public void renderGridline(Canvas canvas) {

    }

    public void renderUnit(Canvas canvas) {

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
        return indicator * 1.3f + labelWidth;
    }

    /**
     * label 区域的高
     *
     * @return
     */
    public float getArea_LableHeight() {
        return indicator * 1.3f + labelHeight;
    }

    /**
     * _unit 区域的高
     *
     * @return
     */
    public float getArea_UnitHeight() {
        return _unitHeight;
    }

    /**
     * _unit 区域的宽
     *
     * @return
     */
    public float getArea_UnitWidth() {
        return _unitWidth;
    }


}
