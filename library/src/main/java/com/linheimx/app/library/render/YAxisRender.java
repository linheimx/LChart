package com.linheimx.app.library.render;

import android.graphics.Canvas;
import android.graphics.RectF;

import com.linheimx.app.library.adapter.IValueAdapter;
import com.linheimx.app.library.manager.MappingManager;
import com.linheimx.app.library.model.Axis;
import com.linheimx.app.library.model.WarnLine;
import com.linheimx.app.library.utils.LogUtil;
import com.linheimx.app.library.utils.SingleF_XY;
import com.linheimx.app.library.utils.Utils;

import java.util.List;

/**
 * Created by lijian on 2016/11/14.
 */

public class YAxisRender extends AxisRender {


    public YAxisRender(RectF _FrameManager, MappingManager _MappingManager, Axis axis) {
        super(_FrameManager, _MappingManager, axis);
    }

    @Override
    public void renderAxisLine(Canvas canvas) {
        super.renderAxisLine(canvas);

        float startX = _rectMain.left;
        float startY = _rectMain.bottom;
        float stopX = _rectMain.left;
        float stopY = _rectMain.top;

        canvas.drawLine(startX, startY, stopX, stopY, _PaintAxis);
    }

    @Override
    public void renderGridline(Canvas canvas) {
        super.renderGridline(canvas);

        canvas.save();
        canvas.clipRect(_rectMain);// 限制绘制区域

        double[] values = _Axis.getLabelValues();
        int labelCount = _Axis.getLabelCount();

        float y = 0;

        float left = _rectMain.left;
        float right = _rectMain.right;

        _PathGrid.reset();

        for (int i = 0; i < labelCount; i++) {
            if (values.length < (i + 1)) {
                break;
            }
            double value = values[i];

            SingleF_XY xy = _MappingManager.getPxByValue(0, value);
            y = xy.getY();

            _PathGrid.moveTo(left, y);
            _PathGrid.lineTo(right, y);
        }

        // grid line
        canvas.drawPath(_PathGrid, _PaintGridline);

        canvas.restore();
    }


    @Override
    public void renderLabels(Canvas canvas) {
        super.renderLabels(canvas);

        IValueAdapter adapter = _Axis.get_ValueAdapter();

        double[] values = _Axis.getLabelValues();
        int labelCount = _Axis.getLabelCount();
        float indicator = _Axis.getLeg();
        float y = 0;

        float left = _rectMain.left;

        int txtHeight = Utils.textHeightAsc(_PaintLabel);

        for (int i = 0; i < labelCount; i++) {
            if (values.length < (i + 1)) {
                break;
            }
            double value = values[i];
            String label = adapter.value2String(value);

            SingleF_XY xy = _MappingManager.getPxByValue(0, value);
            y = xy.getY();

            if (y < _rectMain.top || y > _rectMain.bottom) {
                continue;
            }

            if (label == null) {
                continue;
            }

            // indicator
            canvas.drawLine(left, y, left - indicator, y, _PaintLittle);

            // label
            float labelX = left - _Axis.getLabelDimen() - _Axis.getLeg() * 1.5f;
            float labelY = y + txtHeight / 2;
            canvas.drawText(label, labelX, labelY, _PaintLabel);
        }
    }

    @Override
    public void renderUnit(Canvas canvas) {
        super.renderUnit(canvas);

        float px = _Axis.getUnitDimen();
        float py = _rectMain.centerY() + _Axis.getUnitDimen() / 2;
        canvas.save();
        canvas.rotate(-90, px, py);
        canvas.drawText(_Axis.get_unit(), px, py, _PaintUnit);
        canvas.restore();
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

                SingleF_XY xy = _MappingManager.getPxByValue(0, value);
                float y = xy.getY();

                if (y < _rectMain.top || y > _rectMain.bottom) {
                    continue;
                }

                _PaintWarnText.setColor(warnLine.getWarnColor());
                _PaintWarnText.setStrokeWidth(warnLine.getWarnLineWidth());
                _PaintWarnText.setTextSize(warnLine.getTxtSize());

                _PaintWarnPath.setColor(warnLine.getWarnColor());
                _PaintWarnPath.setStrokeWidth(warnLine.getWarnLineWidth());

                _PathWarn.reset();
                _PathWarn.moveTo(_rectMain.left, y);
                _PathWarn.lineTo(_rectMain.right, y);

                canvas.drawPath(_PathWarn, _PaintWarnPath);

                float txtHeight = Utils.textHeight(_PaintWarnText);
                canvas.drawText(value + "", _rectMain.left + 10, y - txtHeight, _PaintWarnText);
            }
        }

        canvas.restore();
    }
}
