package com.linheimx.app.library.render;

import android.graphics.Canvas;

import com.linheimx.app.library.adapter.IValueAdapter;
import com.linheimx.app.library.manager.TransformManager;
import com.linheimx.app.library.manager.ViewPortManager;
import com.linheimx.app.library.parts.Axis;
import com.linheimx.app.library.utils.U_XY;
import com.linheimx.app.library.utils.Utils;

/**
 * Created by LJIAN on 2016/11/14.
 */

public class YAxisRender extends AxisRender {

    public YAxisRender(ViewPortManager _ViewPortManager, TransformManager _TransformManager, Axis _Axis) {
        super(_ViewPortManager, _TransformManager, _Axis);
    }

    @Override
    public void renderAxisLine(Canvas canvas) {
        super.renderAxisLine(canvas);

        float startX = _ViewPortManager.contentLeft();
        float startY = _ViewPortManager.contentBottom();
        float stopX = _ViewPortManager.contentLeft();
        float stopY = _ViewPortManager.contentTop();

        canvas.drawLine(startX, startY, stopX, stopY, _PaintAxis);
    }

    @Override
    public void renderLabels_Gridline(Canvas canvas) {
        super.renderLabels_Gridline(canvas);

        IValueAdapter adapter = _Axis.get_ValueAdapter();
        float[] values = _Axis.getStepValues();
        float y = 0;

        float left = _ViewPortManager.contentLeft();
        float right = _ViewPortManager.contentRight();

        int txtHeight = Utils.textHeightAsc(_PaintLabel);

        for (int i = 0; i < _Axis.getLabelCount(); i++) {
            float value = values[i];
            String label = adapter.value2String(value);

            U_XY xy = _TransformManager.getPxByValue(0, value);
            y = xy.getY();

            // grid line
            canvas.drawLine(left, y, right, y, _PaintGridline);

            // little
            float little = Utils.dp2px(5);
            canvas.drawLine(left, y, left - little, y, _PaintLittle);

            // label
            float labelX = left - Utils.textWidth(_PaintLabel, label) * 1.5f;
            float labelY = y + txtHeight / 2;
            canvas.drawText(label, labelX, labelY, _PaintLabel);
        }
    }
}
