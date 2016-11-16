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

public class XAxisRender extends AxisRender {

    public XAxisRender(ViewPortManager _ViewPortManager, TransformManager _TransformManager, Axis _Axis) {
        super(_ViewPortManager, _TransformManager, _Axis);
    }

    @Override
    public void renderAxisLine(Canvas canvas) {
        super.renderAxisLine(canvas);

        float startX = _ViewPortManager.contentLeft();
        float startY = _ViewPortManager.contentBottom();
        float stopX = _ViewPortManager.contentRight()+20;//画长一点
        float stopY = _ViewPortManager.contentBottom();

        canvas.drawLine(startX, startY, stopX, stopY, _PaintAxis);
    }


    @Override
    public void renderLabels(Canvas canvas) {
        super.renderLabels(canvas);

        IValueAdapter adapter = _Axis.get_ValueAdapter();
        float[] values = _Axis.getStepValues();
        float y = _ViewPortManager.contentBottom();
        float x = 0;

        int txtHeight = Utils.textHeight(_PaintLabel);

        for (int i = 0; i < _Axis.getLabelCount(); i++) {
            float value = values[i];
            String label = adapter.value2String(value);


            U_XY xy = _TransformManager.getPxByValue(value, 0);
            x = xy.getX();

            // little
            float little = Utils.dp2px(5);
            canvas.drawLine(x, y, x, y + little, _PaintLittle);

            // label
            float labelX = x - Utils.textWidth(_PaintLabel, label) / 2;
            float labelY = y + txtHeight * 1.5f;
            canvas.drawText(label, labelX, labelY, _PaintLabel);
        }
    }
}
