package com.linheimx.app.library.render;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.linheimx.app.library.charts.LineChart;
import com.linheimx.app.library.data.Entry;
import com.linheimx.app.library.data.Line;
import com.linheimx.app.library.data.Lines;
import com.linheimx.app.library.manager.TransformManager;
import com.linheimx.app.library.manager.ViewPortManager;
import com.linheimx.app.library.utils.Single_XY;
import com.linheimx.app.library.utils.Utils;

import java.util.List;

/**
 * Created by LJIAN on 2016/11/17.
 */

public class LineRender extends BaseRender {

    LineChart lineChart;

    Lines _lines;
    Paint _PaintLine;
    Paint _PaintCircle;
    Paint _PaintHighLight;


    public LineRender(ViewPortManager _ViewPortManager, TransformManager _TransformManager, Lines _lines, LineChart lineChart) {
        super(_ViewPortManager, _TransformManager);
        this._lines = _lines;
        this.lineChart = lineChart;

        _PaintLine = new Paint(Paint.ANTI_ALIAS_FLAG);
        _PaintCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        _PaintHighLight = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public void render(Canvas canvas) {

        if (_lines == null) {
            return;
        }

        canvas.save();
        canvas.clipRect(_ViewPortManager.getContentRect());
        // render line
        for (Line line : _lines.getLines()) {
            drawLine_Circle(canvas, line);
        }
        // render highLight
        drawHighLight(canvas);
        canvas.restore();
    }

    private float[] _LineBuffer = new float[4];

    private void drawLine_Circle(Canvas canvas, Line line) {

        _PaintLine.setStrokeWidth(line.getLineWidth());
        _PaintLine.setColor(line.getLineColor());

        _PaintCircle.setStrokeWidth(line.getLineWidth());
        _PaintCircle.setColor(line.getLineColor());

        List<Entry> list = line.getEntries();

        // check
        if (list.size() <= 1) {
            return;
        }


        float xMin_Visiable = lineChart.getVisiableMinX();
        float xMax_Visiable = lineChart.getVisiableMaxX();

        int minIndex = Line.getEntryIndex(list, xMin_Visiable, Line.Rounding.DOWN);
        int maxIndex = Line.getEntryIndex(list, xMax_Visiable, Line.Rounding.UP);

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

    private void drawHighLight(Canvas canvas) {

        _PaintHighLight.setStrokeWidth(2);
        _PaintHighLight.setColor(Color.BLUE);

        // check
        if (hightX == Float.MIN_VALUE) {
            return;
        }

        if (_lines.getLines().size() == 0) {
            return;
        }

        float disX = Float.MAX_VALUE;
        float disY = Float.MAX_VALUE;
        Entry hitEntry = null;

        for (Line line : _lines.getLines()) {

            int index = Line.getEntryIndex(line.getEntries(), hightX, Line.Rounding.CLOSEST);
            Entry entry = line.getEntries().get(index);

            float dx = Math.abs(entry.getX() - hightX);

            float dy = 0;
            if (hightY != Float.MIN_VALUE) {
                dy = Math.abs(entry.getY() - hightY);
            }


            // 先考虑 x
            if (dx <= disX) {
                disX = dx;

                // 再考虑 y
                if (hightY != Float.MIN_VALUE) {
                    if (dy <= disY) {
                        hitEntry = entry;
                    }
                } else {
                    hitEntry = entry;
                }
            }
        }


        Single_XY xy = _TransformManager.getPxByEntry(hitEntry);

        canvas.drawLine(_ViewPortManager.contentLeft(), xy.getY(), _ViewPortManager.contentRight(), xy.getY(), _PaintHighLight);
        canvas.drawLine(xy.getX(), _ViewPortManager.contentTop(), xy.getX(), _ViewPortManager.contentBottom(), _PaintHighLight);

    }


    public void notifyDataChanged(Lines lines) {
        _lines = lines;
    }


    float hightX = Float.MIN_VALUE;
    float hightY = Float.MIN_VALUE;

    public void highLight_ValueXY(float x, float y) {
        hightX = x;
        hightY = y;
    }

    public void highLightLeft() {

    }

    public void highLightRight() {

    }

}
