package com.linheimx.app.library.render;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.linheimx.app.library.manager.TransformManager;
import com.linheimx.app.library.manager.ViewPortManager;
import com.linheimx.app.library.parts.Axis;
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

    public AxisRender(ViewPortManager _ViewPortManager, TransformManager _TransformManager, Axis _Axis) {
        super(_ViewPortManager, _TransformManager);
        this._Axis = _Axis;
        _PaintAxis = new Paint(Paint.ANTI_ALIAS_FLAG);
        _PaintLabel = new Paint(Paint.ANTI_ALIAS_FLAG);
        _PaintLittle = new Paint(Paint.ANTI_ALIAS_FLAG);
        _PaintGridline = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    public void render(Canvas canvas) {
        // 考虑到外观的原因，绘制的次序由 lineChart 中的draw来控制。
    }

    public void renderAxisLine(Canvas canvas) {
        _PaintAxis.setColor(_Axis.getAxisColor());
        _PaintAxis.setStrokeWidth(Utils.dp2px(_Axis.getPaintWidth()));
    }

    public void renderGridline(Canvas canvas) {
        _PaintLabel.setColor(_Axis.getTextColor());
        _PaintLabel.setTextSize(Utils.dp2px(_Axis.getTextSize()));

        _PaintLittle.setColor(_Axis.getTextColor());
        _PaintLittle.setStrokeWidth(Utils.dp2px(1));

        _PaintGridline.setColor(Color.GRAY);
        _PaintGridline.setStrokeWidth(Utils.dp2px(1));
    }

    public void renderLabels(Canvas canvas) {

    }
}
