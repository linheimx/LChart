package com.linheimx.app.library.render;

import android.graphics.Canvas;

import com.linheimx.app.library.adapter.IValueAdapter;
import com.linheimx.app.library.manager.MappingManager;
import com.linheimx.app.library.manager.FrameManager;
import com.linheimx.app.library.dataprovider.Axis;
import com.linheimx.app.library.utils.Single_XY;
import com.linheimx.app.library.utils.Utils;

/**
 * Created by LJIAN on 2016/11/14.
 */

public class YAxisRender extends AxisRender {


    public YAxisRender(FrameManager _FrameManager, MappingManager _MappingManager, Axis axis) {
        super(_FrameManager, _MappingManager, axis);
    }

    @Override
    public void renderAxisLine(Canvas canvas) {
        super.renderAxisLine(canvas);

        float startX = _FrameManager.frameLeft();
        float startY = _FrameManager.frameBottom();
        float stopX = _FrameManager.frameLeft();
        float stopY = _FrameManager.frameTop();

        canvas.drawLine(startX, startY, stopX, stopY, _PaintAxis);
    }

    @Override
    public void renderGridline(Canvas canvas) {
        super.renderGridline(canvas);

        float[] values = _Axis.getLabelValues();
        float y = 0;

        float left = _FrameManager.frameLeft();
        float right = _FrameManager.frameRight();

        for (int i = 0; i < _Axis.getLabelCount(); i++) {
            float value = values[i];

            Single_XY xy = _MappingManager.getPxByValue(0, value);
            y = xy.getY();

            // grid line
            canvas.drawLine(left, y, right, y, _PaintGridline);
        }
    }


    @Override
    public void renderLabels(Canvas canvas) {
        super.renderLabels(canvas);

        IValueAdapter adapter = _Axis.get_ValueAdapter();
        float[] values = _Axis.getLabelValues();

        float indicator = _Axis.getLeg();
        float y = 0;

        float left = _FrameManager.frameLeft();

        int txtHeight = Utils.textHeightAsc(_PaintLabel);

        for (int i = 0; i < _Axis.getLabelCount(); i++) {
            float value = values[i];
            String label = adapter.value2String(value);

            Single_XY xy = _MappingManager.getPxByValue(0, value);
            y = xy.getY();

            if (y < _FrameManager.frameTop() || y > _FrameManager.frameBottom()) {
                continue;
            }

            // indicator
            canvas.drawLine(left, y, left - indicator, y, _PaintLittle);

            // label
            float labelX = left - _Axis.getArea_Label();
            float labelY = y + txtHeight / 2;
            canvas.drawText(label, labelX, labelY, _PaintLabel);
        }
    }

    @Override
    public void renderUnit(Canvas canvas) {
        // unit
        _PaintUnit.setColor(_Axis.getUnitColor());
        _PaintUnit.setTextSize(_Axis.getUnitTxtSize());

        float left = _FrameManager.frameLeft();
        float px = left - _Axis.getArea_Label() - _Axis.getArea_Unit();
        float py = _FrameManager.getFrameRect().centerY() + _Axis.getArea_Unit() / 2;

        canvas.save();
        canvas.rotate(-90, px, py);
        canvas.drawText(_Axis.get_unit(), px, py, _PaintUnit);
        canvas.restore();
    }

}
