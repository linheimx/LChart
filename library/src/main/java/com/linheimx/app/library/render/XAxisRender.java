package com.linheimx.app.library.render;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.linheimx.app.library.adapter.IValueAdapter;
import com.linheimx.app.library.manager.TransformManager;
import com.linheimx.app.library.manager.ViewPortManager;
import com.linheimx.app.library.DataProvider.Axis;
import com.linheimx.app.library.utils.Single_XY;
import com.linheimx.app.library.utils.Utils;

/**
 * Created by LJIAN on 2016/11/14.
 */

public class XAxisRender extends AxisRender {

    public XAxisRender(ViewPortManager _ViewPortManager, TransformManager _TransformManager, Axis axis) {
        super(_ViewPortManager, _TransformManager, axis);
    }

    @Override
    public void renderAxisLine(Canvas canvas) {
        super.renderAxisLine(canvas);

        float startX = _ViewPortManager.contentLeft();
        float startY = _ViewPortManager.contentBottom();
        float stopX = _ViewPortManager.contentRight();
        float stopY = _ViewPortManager.contentBottom();

        canvas.drawLine(startX, startY, stopX, stopY, _PaintAxis);
    }


    @Override
    public void renderGridline(Canvas canvas) {
        super.renderGridline(canvas);

        float[] values = _Axis.getLabelValues();

        float x = 0;

        float top = _ViewPortManager.contentTop();
        float bottom = _ViewPortManager.contentBottom();

        for (int i = 0; i < _Axis.getLabelCount(); i++) {
            float value = values[i];

            Single_XY xy = _TransformManager.getPxByValue(value, 0);
            x = xy.getX();

            // grid line
            canvas.drawLine(x, bottom, x, top, _PaintGridline);
        }
    }

    @Override
    public void renderLabels(Canvas canvas) {
        super.renderLabels(canvas);

        IValueAdapter adapter = _Axis.get_ValueAdapter();
        float[] values = _Axis.getLabelValues();
        float indicator = _Axis.getLeg();

        float x = 0;

        float bottom = _ViewPortManager.contentBottom();

        for (int i = 0; i < _Axis.getLabelCount(); i++) {
            float value = values[i];
            String label = adapter.value2String(value);

            Single_XY xy = _TransformManager.getPxByValue(value, 0);
            x = xy.getX();

            // check
            if (x < _ViewPortManager.contentLeft() || x > _ViewPortManager.contentRight()) {
                continue;
            }

            // indicator
            canvas.drawLine(x, bottom, x, bottom + indicator, _PaintLittle);

            // label
            float labelX = x - Utils.textWidth(_PaintLabel, label) / 2;
            float labelY = bottom + _Axis.getArea_Label();
            canvas.drawText(label, labelX, labelY, _PaintLabel);
        }
    }


    @Override
    public void renderUnit(Canvas canvas) {
        super.renderUnit(canvas);

        String unit = _Axis.get_unit();
        Paint paintUnit = _PaintUnit;

        float bottom = _ViewPortManager.contentBottom();
        float labelX = _ViewPortManager.getContentRect().centerX() - Utils.textWidth(paintUnit, unit) / 2;
        float labelY = bottom + _Axis.offsetBottom();

        canvas.drawText(unit, labelX, labelY, _PaintUnit);
    }

}
