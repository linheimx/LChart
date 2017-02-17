package com.linheimx.app.library.render;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

import com.linheimx.app.library.manager.MappingManager;
import com.linheimx.app.library.model.Axis;
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

    Path _PathGrid;

    public AxisRender(RectF rectMain, MappingManager _MappingManager, Axis axis) {
        super(rectMain, _MappingManager);

        this._Axis = axis;

        _PaintAxis = new Paint(Paint.ANTI_ALIAS_FLAG);
        _PaintLabel = new Paint(Paint.ANTI_ALIAS_FLAG);
        _PaintLittle = new Paint(Paint.ANTI_ALIAS_FLAG);
        _PaintGridline = new Paint(Paint.ANTI_ALIAS_FLAG);
        _PaintUnit = new Paint(Paint.ANTI_ALIAS_FLAG);

        // 虚线效果
        _PaintGridline.setStyle(Paint.Style.STROKE);
        _PaintGridline.setPathEffect(new DashPathEffect(new float[]{3, 2}, 0));

        _PathGrid =new Path();
    }

    public void renderAxisLine(Canvas canvas) {
        _PaintAxis.setColor(_Axis.getAxisColor());
        _PaintAxis.setStrokeWidth(_Axis.getAxisWidth());
    }

    public void renderGridline(Canvas canvas) {
        _PaintGridline.setColor(Color.parseColor("#CCCCCC"));
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
