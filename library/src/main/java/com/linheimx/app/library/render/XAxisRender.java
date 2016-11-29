package com.linheimx.app.library.render;

import android.graphics.Canvas;
import android.graphics.PointF;

import com.linheimx.app.library.adapter.IValueAdapter;
import com.linheimx.app.library.manager.TransformManager;
import com.linheimx.app.library.manager.ViewPortManager;
import com.linheimx.app.library.utils.Single_XY;
import com.linheimx.app.library.utils.Utils;

/**
 * Created by LJIAN on 2016/11/14.
 */

public class XAxisRender extends AxisRender {

    public XAxisRender(ViewPortManager _ViewPortManager, TransformManager _TransformManager) {
        super(_ViewPortManager, _TransformManager);
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

        float[] values = labelValues;

        float x = 0;

        float top = _ViewPortManager.contentTop();
        float bottom = _ViewPortManager.contentBottom();

        for (int i = 0; i < labelCount; i++) {
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

        IValueAdapter adapter = _ValueAdapter;
        float[] values = labelValues;

        float x = 0;

        float top = _ViewPortManager.contentTop();
        float bottom = _ViewPortManager.contentBottom();

        int txtHeight = Utils.textHeight(_PaintLabel);

        for (int i = 0; i < labelCount; i++) {
            float value = values[i];
            String label = adapter.value2String(value);

            Single_XY xy = _TransformManager.getPxByValue(value, 0);
            x = xy.getX();

            // check
            if (x < _ViewPortManager.contentLeft() || x > _ViewPortManager.contentRight()) {
                continue;
            }

            // little
            float little = Utils.dp2px(5);
            canvas.drawLine(x, bottom, x, bottom + little, _PaintLittle);

            // label
            float labelX = x - Utils.textWidth(_PaintLabel, label) / 2;
            float labelY = bottom + txtHeight * 1.5f;
            canvas.drawText(label, labelX, labelY, _PaintLabel);
        }
    }

    public float offsetBottom() {

        _PaintLabel.setColor(labelColor);
        _PaintLabel.setTextSize(Utils.dp2px(labelSize));
        _PaintLittle.setColor(axisColor);
        _PaintLittle.setStrokeWidth(Utils.dp2px(axisWidth));

        float labelHeight = Utils.textHeight(_PaintLabel);
        float little = Utils.dp2px(5);
        return labelHeight * 1.5f + little;
    }

    @Override
    public float getVisiableMin() {
        float px = _ViewPortManager.contentLeft();
        Single_XY xy = _TransformManager.getValueByPx(px, 0);
        return xy.getX();
    }

    @Override
    public float getVisiableMax() {
        float px = _ViewPortManager.contentRight();
        Single_XY xy = _TransformManager.getValueByPx(px, 0);
        return xy.getX();
    }
}
