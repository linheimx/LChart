package com.linheimx.app.library.render;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.linheimx.app.library.manager.TransformManager;
import com.linheimx.app.library.manager.FrameManager;
import com.linheimx.app.library.dataprovider.Axis;
import com.linheimx.app.library.utils.Utils;

/**
 * Created by LJIAN on 2016/11/14.
 */

public abstract class AxisRender extends BaseRender {

    Axis _Axis;

    Paint _PaintAxis;
    Paint _PaintGridline;
    Paint _PaintLittle;
    Paint _PaintLabel;
    Paint _PaintUnit;

    public AxisRender(FrameManager _FrameManager, TransformManager _TransformManager, Axis axis) {
        super(_FrameManager, _TransformManager);

        this._Axis = axis;

        _PaintAxis = new Paint(Paint.ANTI_ALIAS_FLAG);
        _PaintLabel = new Paint(Paint.ANTI_ALIAS_FLAG);
        _PaintLittle = new Paint(Paint.ANTI_ALIAS_FLAG);
        _PaintGridline = new Paint(Paint.ANTI_ALIAS_FLAG);
        _PaintUnit = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public void renderAxisLine(Canvas canvas) {
        _PaintAxis.setColor(_Axis.getAxisColor());
        _PaintAxis.setStrokeWidth(_Axis.getAxisWidth());
    }

    public void renderGridline(Canvas canvas) {
        _PaintGridline.setColor(Color.GRAY);
        _PaintGridline.setStrokeWidth(Utils.dp2px(1));
    }

    public void renderLabels(Canvas canvas) {
        // label
        _PaintLabel.setColor(_Axis.getLabelColor());
        _PaintLabel.setTextSize(_Axis.getLabelTextSize());
        // little
        _PaintLittle.setColor(_Axis.getAxisColor());
        _PaintLittle.setStrokeWidth(_Axis.getAxisWidth());
    }

    public void renderUnit(Canvas canvas) {
        _PaintUnit.setColor(_Axis.getUnitColor());
        _PaintUnit.setTextSize(_Axis.getUnitTxtSize());
    }

}
