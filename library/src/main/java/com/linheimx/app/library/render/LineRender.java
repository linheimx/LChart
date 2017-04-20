package com.linheimx.app.library.render;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.linheimx.app.library.charts.LineChart;
import com.linheimx.app.library.data.Entry;
import com.linheimx.app.library.data.Line;
import com.linheimx.app.library.data.Lines;
import com.linheimx.app.library.manager.MappingManager;
import com.linheimx.app.library.utils.LogUtil;
import com.linheimx.app.library.utils.SingleF_XY;
import com.linheimx.app.library.utils.Utils;

import java.util.List;

/**
 * Created by lijian on 2016/11/17.
 */

public class LineRender extends BaseRender {

    LineChart lineChart;
    Lines _lines;

    Paint _PaintLine;
    Paint _PaintCircle;
    Paint _PaintLegend;


    public LineRender(RectF rectMain, MappingManager _MappingManager, Lines _lines, LineChart lineChart) {
        super(rectMain, _MappingManager);
        this._lines = _lines;
        this.lineChart = lineChart;

        _PaintLine = new Paint(Paint.ANTI_ALIAS_FLAG);
        _PaintCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        _PaintLegend = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public void render(Canvas canvas) {

        if (_lines == null) {
            return;
        }

        canvas.save();
        canvas.clipRect(_rectMain); // 限制绘制区域

        // 绘制折线 和 折线上的圆点
        for (Line line : _lines.getLines()) {
            if (!line.isEnable()) {
                continue;
            }
            drawLine_Circle(canvas, line);
        }

        canvas.restore();

        // 绘制每条线的 legend
        for (int i = 0; i < _lines.getLines().size(); i++) {
            Line line = _lines.getLines().get(i);
            if (!line.isEnable()) {
                continue;
            }
            drawLegend(canvas, line, i);
        }
    }

    private float[] _LineBuffer = new float[4];

    private void drawLine_Circle(Canvas canvas, Line line) {

        _PaintLine.setStrokeWidth(line.getLineWidth());
        _PaintLine.setColor(line.getLineColor());

        _PaintCircle.setStrokeWidth(line.getLineWidth());
        _PaintCircle.setColor(line.getLineColor());

        List<Entry> list = line.getEntries();

        // check
        if (list == null || list.size() == 0) {
            return;
        }

        // 考虑只有一个点的情况
        if (list.size() == 1) {
            Entry hit = list.get(0);
            SingleF_XY xy = _MappingManager.getPxByEntry(hit);
            canvas.drawCircle(xy.getX(), xy.getY(), line.getCircleR(), _PaintCircle);
            return;
        }

        double xMin_Visiable = lineChart.getVisiableMinX();
        double xMax_Visiable = lineChart.getVisiableMaxX();

        int minIndex = Line.getEntryIndex(list, xMin_Visiable, Line.Rounding.DOWN);
        int maxIndex = Line.getEntryIndex(list, xMax_Visiable, Line.Rounding.UP);

        if (Math.abs(maxIndex - minIndex) == 0) {
            return;
        }

        int lineCount = maxIndex - minIndex;

        if (_LineBuffer.length < lineCount * 4) {
            _LineBuffer = new float[lineCount * 4];
        }

        int count = 0;
        for (int i = minIndex; i < maxIndex; i++) {
            Entry start = list.get(i);
            Entry end = list.get(i + 1);

//            _LineBuffer[count++] = _MappingManager.v2p_x(start.getX());
//            _LineBuffer[count++] = _MappingManager.v2p_y(start.getY());
//            _LineBuffer[count++] = _MappingManager.v2p_x(end.getX());
//            _LineBuffer[count++] = _MappingManager.v2p_y(end.getY());

            /////////////////////////////////////////////  考虑动画效果  //////////////////////////////////////////
            _LineBuffer[count++] = _MappingManager.v2p_x(start.getX());
            _LineBuffer[count++] = getAnimateY(_MappingManager.v2p_y(start.getY()));
            _LineBuffer[count++] = _MappingManager.v2p_x(end.getX());
            _LineBuffer[count++] = getAnimateY(_MappingManager.v2p_y(end.getY()));

            if (line.isDrawCircle()) {
                SingleF_XY xy = _MappingManager.getPxByEntry(start);
                canvas.drawCircle(xy.getX(), getAnimateY(xy.getY()), line.getCircleR(), _PaintCircle);

                // 把最后点一个绘制出来
                if (i == maxIndex - 1) {
                    Entry last = list.get(maxIndex);
                    xy = _MappingManager.getPxByEntry(last);
//                    canvas.drawCircle(xy.getX(), xy.getY(), line.getCircleR(), _PaintCircle);

                    /////////////////////////////////////////////  考虑动画效果  //////////////////////////////////////////
                    canvas.drawCircle(xy.getX(), getAnimateY(xy.getY()), line.getCircleR(), _PaintCircle);
                }
            }
        }

        canvas.drawLines(_LineBuffer, 0, count, _PaintLine);
    }


    RectF _RectFBuffer = new RectF();

    private void drawLegend(Canvas canvas, Line line, int order) {

        if (!line.isDrawLegend()) {
            return;
        }

        _PaintLegend.setColor(line.getLineColor());
        _PaintLegend.setTextSize(line.getLegendTextSize());

        String name = line.getName();
        float txtH = Utils.textHeight(_PaintLegend);
        float cubic = 20;

        _RectFBuffer.left = order * line.getLegendWidth();
        _RectFBuffer.top = line.getLegendHeight() - cubic - txtH / 2;
        _RectFBuffer.right = _RectFBuffer.left + cubic;
        _RectFBuffer.bottom = _RectFBuffer.top + cubic;


        canvas.drawRect(_RectFBuffer, _PaintLegend);
        canvas.drawText(name, _RectFBuffer.right + 10, _RectFBuffer.bottom, _PaintLegend);
    }

    public void onDataChanged(Lines lines) {
        _lines = lines;
    }

    private float getAnimateY(float src) {
        return lineChart.get_MainPlotRect().bottom - (lineChart.get_MainPlotRect().bottom - src) * lineChart._hitValueY;
    }
}
