package com.linheimx.app.library.charts;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.linheimx.app.library.data.Lines;
import com.linheimx.app.library.manager.TransformManager;
import com.linheimx.app.library.manager.ViewPortManager;
import com.linheimx.app.library.parts.XAxis;
import com.linheimx.app.library.parts.YAxis;
import com.linheimx.app.library.render.LineRender;
import com.linheimx.app.library.render.NoDataRender;
import com.linheimx.app.library.render.XAxisRender;
import com.linheimx.app.library.render.YAxisRender;
import com.linheimx.app.library.utils.Utils;

/**
 * Created by Administrator on 2016/11/13.
 */

public class LineChart extends Chart {

    ViewPortManager _ViewPortManager;
    TransformManager _TransformManager;

    XAxis _XAxis;
    YAxis _YAxis;

    Lines _lines;

    //////////////////////////////////  render  /////////////////////////////
    NoDataRender _NoDataRender;
    XAxisRender _XAxisRender;
    YAxisRender _YAxisRender;
    LineRender _LineRender;

    //////////////////////////////// off //////////////////////////////////
    int offLeft = 35;//dp
    int offRight = 10;
    int offTop = 20;
    int offBottom = 25;


    public LineChart(Context context) {
        super(context);
    }

    public LineChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LineChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context) {
        super.init(context);

        _ViewPortManager = new ViewPortManager();
        _TransformManager = new TransformManager(_ViewPortManager);

        _XAxis = new XAxis(_ViewPortManager, _TransformManager);
        _YAxis = new YAxis(_ViewPortManager, _TransformManager);

        // render
        _NoDataRender = new NoDataRender(_ViewPortManager, _TransformManager);
        _XAxisRender = new XAxisRender(_ViewPortManager, _TransformManager, _XAxis);
        _YAxisRender = new YAxisRender(_ViewPortManager, _TransformManager, _YAxis);
        _LineRender = new LineRender(_ViewPortManager, _TransformManager, _lines, _XAxis);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 1. render no data
        if (_lines == null || _lines.getLines().size() == 0) {
            _NoDataRender.render(canvas);
        }

        // render lable,grid line
        _XAxisRender.renderLabels_Gridline(canvas);
        _YAxisRender.renderLabels_Gridline(canvas);

        // render line
        _LineRender.render(canvas);

        // render axis
        _XAxisRender.renderAxisLine(canvas);
        _YAxisRender.renderAxisLine(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        _ViewPortManager.setChartWH(w, h);

        dataChanged();
    }


    private void dataChanged() {

        if (_lines == null) {
            return;
        }

        // 1. axis
        _XAxis.prepareData(_lines.getmXMin(), _lines.getmXMax());
        _YAxis.prepareData(_lines.getmYMin(), _lines.getmYMax());

        prepareMap();

        // 需要一些计算
        _XAxis.calLabelValues();
        _YAxis.calLabelValues();


        // 2. notifyDataChanged
        _LineRender.notifyDataChanged(_lines);
    }

    private void prepareMap() {
        _ViewPortManager.restrainViewPort(offLeft, offTop, offRight, offBottom);
        _TransformManager.prepareRelation(_XAxis, _YAxis);
    }


    public void setLines(Lines lines) {
        _lines = lines;

        dataChanged();
        postInvalidate();
    }

    public Lines getlines() {
        return _lines;
    }


    public int getOffLeft() {
        return offLeft;
    }

    public void setOffLeft(int offLeft) {
        this.offLeft = offLeft;
        dataChanged();
    }

    public int getOffRight() {
        return offRight;
    }

    public void setOffRight(int offRight) {
        this.offRight = offRight;
        dataChanged();
    }

    public int getOffTop() {
        return offTop;
    }

    public void setOffTop(int offTop) {
        this.offTop = offTop;
        dataChanged();
    }

    public int getOffBottom() {
        return offBottom;
    }

    public void setOffBottom(int offBottom) {
        this.offBottom = offBottom;
        dataChanged();
    }
}
