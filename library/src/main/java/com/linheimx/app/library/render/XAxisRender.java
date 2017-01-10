package com.linheimx.app.library.render;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.linheimx.app.library.adapter.IValueAdapter;
import com.linheimx.app.library.manager.TransformManager;
import com.linheimx.app.library.manager.FrameManager;
import com.linheimx.app.library.dataprovider.Axis;
import com.linheimx.app.library.utils.Single_XY;
import com.linheimx.app.library.utils.Utils;

/**
 * Created by LJIAN on 2016/11/14.
 */

public class XAxisRender extends AxisRender {

    public XAxisRender(FrameManager _FrameManager, TransformManager _TransformManager, Axis axis) {
        super(_FrameManager, _TransformManager, axis);
    }

    @Override
    public void renderAxisLine(Canvas canvas) {
        super.renderAxisLine(canvas);

        float startX = _FrameManager.frameLeft();
        float startY = _FrameManager.frameBottom();
        float stopX = _FrameManager.frameRight();
        float stopY = _FrameManager.frameBottom();

        canvas.drawLine(startX, startY, stopX, stopY, _PaintAxis);
    }


    @Override
    public void renderGridline(Canvas canvas) {
        super.renderGridline(canvas);

        float[] values = _Axis.getLabelValues();

        float x = 0;

        float top = _FrameManager.frameTop();
        float bottom = _FrameManager.frameBottom();

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

        float bottom = _FrameManager.frameBottom();

        for (int i = 0; i < _Axis.getLabelCount(); i++) {
            float value = values[i];
            String label = adapter.value2String(value);

            Single_XY xy = _TransformManager.getPxByValue(value, 0);
            x = xy.getX();

            // check
            if (x < _FrameManager.frameLeft() || x > _FrameManager.frameRight()) {
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

        float bottom = _FrameManager.frameBottom();
        float labelX = _FrameManager.getFrameRect().centerX() - Utils.textWidth(paintUnit, unit) / 2;
        float labelY = bottom + _Axis.offsetBottom();

        canvas.drawText(unit, labelX, labelY, _PaintUnit);
    }

}
