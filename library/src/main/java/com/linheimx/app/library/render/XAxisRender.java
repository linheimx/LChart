package com.linheimx.app.library.render;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.linheimx.app.library.adapter.IValueAdapter;
import com.linheimx.app.library.manager.MappingManager;
import com.linheimx.app.library.model.Axis;
import com.linheimx.app.library.model.WarnLine;
import com.linheimx.app.library.utils.SingleF_XY;
import com.linheimx.app.library.utils.Utils;

import java.util.List;

/**
 * Created by lijian on 2016/11/14.
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

        canvas.save();
        canvas.clipRect(_rectMain);// 限制绘制区域

        double[] values = _Axis.getLabelValues();
        int labelCount = _Axis.getLabelCount();
        float x = 0;

        float top = _rectMain.top;
        float bottom = _rectMain.bottom;

        _PathGrid.reset();

        for (int i = 0; i < labelCount; i++) {
            if (values.length < (i + 1)) {
                break;
            }

            double value = values[i];

            SingleF_XY xy = _MappingManager.getPxByValue(value, 0);
            x = xy.getX();

            _PathGrid.moveTo(x, bottom);
            _PathGrid.lineTo(x, top);
        }

        // grid line
        canvas.drawPath(_PathGrid, _PaintGridline);

        canvas.restore();
    }

    @Override
    public void renderLabels(Canvas canvas) {
        super.renderLabels(canvas);

        double[] values = _Axis.getLabelValues();
        int labelCount = _Axis.getLabelCount();

        IValueAdapter adapter = _Axis.get_ValueAdapter();
        float indicator = _Axis.getLeg();

        float x = 0;

        float bottom = _rectMain.bottom;

        for (int i = 0; i < labelCount; i++) {
            if (values.length < (i + 1)) {
                break;
            }

            double value = values[i];
            String label = adapter.value2String(value);

            SingleF_XY xy = _MappingManager.getPxByValue(value, 0);
            x = xy.getX();

            // check
            if (x < _rectMain.left || x > _rectMain.right) {
                continue;
            }

            if (label == null) {
                continue;
            }

            // indicator
            canvas.drawLine(x, bottom, x, bottom + indicator, _PaintLittle);

            // label
            float labelX = x - Utils.textWidth(_PaintLabel, label) / 2;
            float labelY = bottom + _Axis.getLeg() + _Axis.getLabelDimen();
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
        float labelY = bottom + _Axis.getLeg() + _Axis.getLabelDimen() + _Axis.getUnitDimen();

        canvas.drawText(unit, labelX, labelY, _PaintUnit);
    }


    @Override
    public void renderWarnLine(Canvas canvas) {
        super.renderWarnLine(canvas);

        List<WarnLine> warnLines = _Axis.getListWarnLins();
        if (warnLines == null) {
            return;
        }

        canvas.save();
        canvas.clipRect(_rectMain);

        for (WarnLine warnLine : warnLines) {
            if (warnLine.isEnable()) {
                double value = warnLine.getValue();

                SingleF_XY xy = _MappingManager.getPxByValue(value, 0);
                float x = xy.getX();

                if (x < _rectMain.left || x > _rectMain.right) {
                    continue;
                }

                _PaintWarnText.setColor(warnLine.getWarnColor());
                _PaintWarnText.setStrokeWidth(warnLine.getWarnLineWidth());
                _PaintWarnText.setTextSize(warnLine.getTxtSize());

                _PaintWarnPath.setColor(warnLine.getWarnColor());
                _PaintWarnPath.setStrokeWidth(warnLine.getWarnLineWidth());

                _PathWarn.reset();
                _PathWarn.moveTo(x, _rectMain.bottom);
                _PathWarn.lineTo(x, _rectMain.top);

                canvas.drawPath(_PathWarn, _PaintWarnPath);

                float txtHeight = Utils.textHeight(_PaintWarnText);
                float txtWidth = Utils.textWidth(_PaintWarnText, "" + value);

                canvas.drawText(value + "", x - txtWidth * 1.5f, _rectMain.bottom - txtHeight * 1.5f, _PaintWarnText);
            }
        }

        canvas.restore();
    }

}
