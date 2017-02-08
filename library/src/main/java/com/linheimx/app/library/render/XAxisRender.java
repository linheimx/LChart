package com.linheimx.app.library.render;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.linheimx.app.library.adapter.IValueAdapter;
import com.linheimx.app.library.manager.MappingManager;
import com.linheimx.app.library.model.Axis;
import com.linheimx.app.library.utils.SingleF_XY;
import com.linheimx.app.library.utils.Utils;

/**
 * Created by LJIAN on 2016/11/14.
 */

public class XAxisRender extends AxisRender {

    public XAxisRender(RectF _FrameManager, MappingManager _MappingManager, Axis axis) {
        super(_FrameManager, _MappingManager, axis);
    }

    @Override
    public void renderAxisLine(Canvas canvas) {
        super.renderAxisLine(canvas);

        float startX = _rectMain.left;
        float startY = _rectMain.bottom;
        float stopX = _rectMain.right;
        float stopY = _rectMain.bottom;

        canvas.drawLine(startX, startY, stopX, stopY, _PaintAxis);
    }


    @Override
    public void renderGridline(Canvas canvas) {
        super.renderGridline(canvas);

        double[] values = _Axis.getLabelValues();

        float x = 0;

        float top = _rectMain.top;
        float bottom = _rectMain.bottom;

        _PathGrid.reset();

        for (int i = 0; i < _Axis.getLabelCount(); i++) {
            double value = values[i];

            SingleF_XY xy = _MappingManager.getPxByValue(value, 0);
            x = xy.getX();

            _PathGrid.moveTo(x,bottom);
            _PathGrid.lineTo(x,top);
        }

        // grid line
        canvas.drawPath(_PathGrid,_PaintGridline);
    }

    @Override
    public void renderLabels(Canvas canvas) {
        super.renderLabels(canvas);

        IValueAdapter adapter = _Axis.get_ValueAdapter();
        double[] values = _Axis.getLabelValues();
        float indicator = _Axis.getLeg();

        float x = 0;

        float bottom = _rectMain.bottom;

        for (int i = 0; i < _Axis.getLabelCount(); i++) {
            double value = values[i];
            String label = adapter.value2String(value);

            SingleF_XY xy = _MappingManager.getPxByValue(value, 0);
            x = xy.getX();

            // check
            if (x < _rectMain.left || x > _rectMain.right) {
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

        float bottom = _rectMain.bottom;
        float labelX = _rectMain.centerX() - Utils.textWidth(paintUnit, unit) / 2;
        float labelY = bottom + _Axis.offsetBottom();

        canvas.drawText(unit, labelX, labelY, _PaintUnit);
    }

}
