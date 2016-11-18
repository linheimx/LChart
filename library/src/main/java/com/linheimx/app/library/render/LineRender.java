package com.linheimx.app.library.render;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.linheimx.app.library.data.Entry;
import com.linheimx.app.library.data.Line;
import com.linheimx.app.library.data.Lines;
import com.linheimx.app.library.manager.TransformManager;
import com.linheimx.app.library.manager.ViewPortManager;
import com.linheimx.app.library.parts.XAxis;
import com.linheimx.app.library.utils.Single_XY;
import com.linheimx.app.library.utils.Utils;

import java.util.List;

/**
 * Created by LJIAN on 2016/11/17.
 */

public class LineRender extends BaseRender {

    XAxis _XAxis;
    Lines _lines;
    Paint _PaintLine;
    Paint _PaintCircle;

    public LineRender(ViewPortManager _ViewPortManager, TransformManager _TransformManager, Lines _lines, XAxis _XAxis) {
        super(_ViewPortManager, _TransformManager);
        this._lines = _lines;
        this._XAxis = _XAxis;

        _PaintLine = new Paint(Paint.ANTI_ALIAS_FLAG);
        _PaintCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    public void render(Canvas canvas) {

        if (_lines == null) {
            return;
        }

        canvas.save();
        // render line
        for (Line line : _lines.getLines()) {
            drawLine_Circle(canvas, line);
        }
        canvas.restore();
    }

    private float[] _LineBuffer = new float[4];

    private void drawLine_Circle(Canvas canvas, Line line) {

        _PaintLine.setStrokeWidth(Utils.dp2px(line.getLineWidth()));
        _PaintLine.setColor(line.getLineColor());

        _PaintCircle.setStrokeWidth(Utils.dp2px(line.getLineWidth()));
        _PaintCircle.setColor(line.getLineColor());

        List<Entry> list = line.getEntries();

        // check
        if (list.size() <= 1) {
            return;
        }

        float xMin = _XAxis.getVisiableMin();
        float xMax = _XAxis.getVisiableMax();

        int minIndex = Line.getEntryIndex(list, xMin, Line.Rounding.DOWN);
        int maxIndex = Line.getEntryIndex(list, xMax, Line.Rounding.UP);

        if (Math.abs(maxIndex - minIndex) == 0) {
            return;
        }

        int lineCount = maxIndex - minIndex;

        if (_LineBuffer.length < lineCount * 4) {
            _LineBuffer = new float[lineCount * 4];
        }


        int index = 0;
        for (int i = minIndex; i < maxIndex; i++) {
            Entry start = list.get(i);
            Entry end = list.get(i + 1);

            _LineBuffer[index++] = start.getX();
            _LineBuffer[index++] = start.getY();
            _LineBuffer[index++] = end.getX();
            _LineBuffer[index++] = end.getY();


            if (line.isDrawCircle()) {
                Single_XY xy = _TransformManager.getPxByEntry(start);
                canvas.drawCircle(xy.getX(), xy.getY(), Utils.dp2px(line.getCircleR()), _PaintCircle);

                // 把最后点一个绘制出来
                if (i == maxIndex - 1) {
                    Entry last = list.get(maxIndex);
                    xy = _TransformManager.getPxByEntry(last);
                    canvas.drawCircle(xy.getX(), xy.getY(), Utils.dp2px(line.getCircleR()), _PaintCircle);
                }
            }
        }

        _TransformManager.value2Px(_LineBuffer);

        canvas.drawLines(_LineBuffer, _PaintLine);
    }


    public void notifyDataChanged(Lines lines) {
        _lines = lines;
    }

}
