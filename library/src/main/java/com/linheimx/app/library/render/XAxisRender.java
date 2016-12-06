package com.linheimx.app.library.render;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextUtils;

import com.linheimx.app.library.adapter.IValueAdapter;
import com.linheimx.app.library.manager.TransformManager;
import com.linheimx.app.library.manager.ViewPortManager;
import com.linheimx.app.library.parts.Axis;
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

        float startX = _ViewPortManager.contentLeft();
        float startY = _ViewPortManager.contentBottom();
        float stopX = _ViewPortManager.contentRight();
        float stopY = _ViewPortManager.contentBottom();

        canvas.drawLine(startX, startY, stopX, stopY, _Axis.get_PaintAxis());
    }


    @Override
    public void renderGridline(Canvas canvas) {

        float[] values = _Axis.getLabelValues();

        float x = 0;

        float top = _ViewPortManager.contentTop();
        float bottom = _ViewPortManager.contentBottom();

        for (int i = 0; i < _Axis.getLabelCount(); i++) {
            float value = values[i];

            Single_XY xy = _TransformManager.getPxByValue(value, 0);
            x = xy.getX();

            // grid line
            canvas.drawLine(x, bottom, x, top, _Axis.get_PaintGridline());
        }
    }

    @Override
    public void renderLabels(Canvas canvas) {

        IValueAdapter adapter = _Axis.get_ValueAdapter();
        float[] values = _Axis.getLabelValues();
        float indicator = _Axis.getIndicator();
        Paint paintLabel=_Axis.get_PaintLabel();

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
            canvas.drawLine(x, bottom, x, bottom + indicator, _Axis.get_PaintLittle());

            // label
            float labelX = x - Utils.textWidth(paintLabel, label) / 2;
            float labelY = bottom + _Axis.getArea_LableHeight();
            canvas.drawText(label, labelX, labelY, paintLabel);
        }
    }


    @Override
    public void renderUnit(Canvas canvas) {

        String unit = _Axis.get_unit();
        Paint paintUnit = _Axis.get_PaintUnit();

        float bottom = _ViewPortManager.contentBottom();
        float labelX = _ViewPortManager.getContentRect().centerX() - Utils.textWidth(paintUnit, unit) / 2;
        float labelY = bottom + _Axis.offsetBottom();

        canvas.drawText(unit, labelX, labelY, paintUnit);
    }

}
