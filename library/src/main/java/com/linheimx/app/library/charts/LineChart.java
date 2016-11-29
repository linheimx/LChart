package com.linheimx.app.library.charts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.linheimx.app.library.data.Lines;
import com.linheimx.app.library.manager.TransformManager;
import com.linheimx.app.library.manager.ViewPortManager;
import com.linheimx.app.library.render.LineRender;
import com.linheimx.app.library.render.NoDataRender;
import com.linheimx.app.library.render.XAxisRender;
import com.linheimx.app.library.render.YAxisRender;
import com.linheimx.app.library.touch.TouchListener;

/**
 * Created by Administrator on 2016/11/13.
 */

public class LineChart extends Chart {

    ViewPortManager _ViewPortManager;
    TransformManager _TransformManager;

    Lines _lines;

    //////////////////////////////////  render  /////////////////////////////
    NoDataRender _NoDataRender;
    XAxisRender _XAxisRender;
    YAxisRender _YAxisRender;
    LineRender _LineRender;


    ////////////////////////////// touch  /////////////////////////////
    TouchListener _touchListener;


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

        // render
        _NoDataRender = new NoDataRender(_ViewPortManager, _TransformManager);
        _XAxisRender = new XAxisRender(_ViewPortManager, _TransformManager);
        _YAxisRender = new YAxisRender(_ViewPortManager, _TransformManager);
        _LineRender = new LineRender(_ViewPortManager, _TransformManager, _lines, _XAxisRender);

        // touch listener
        _touchListener = new TouchListener(this);

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        if (_lines == null) {
            return false;
        }
        if (!isTouchEnabled) {
            return false;
        }

        return _touchListener.onTouch(this, event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 1. render no data
        if (_lines == null || _lines.getLines().size() == 0) {
            _NoDataRender.render(canvas);
        }

        // cal
        _XAxisRender.calValues();
        _YAxisRender.calValues();

        canvas.save();
        canvas.clipRect(_ViewPortManager.getContentRect());

        // render grid line
        _XAxisRender.renderGridline(canvas);
        _YAxisRender.renderGridline(canvas);

        // render line
        _LineRender.render(canvas);
        canvas.restore();

        // render axis
        _XAxisRender.renderAxisLine(canvas);
        _YAxisRender.renderAxisLine(canvas);

        // render labels
        _XAxisRender.renderLabels(canvas);
        _YAxisRender.renderLabels(canvas);
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

        limitPlotArea();

        prepareMap();

        // 2. notifyDataChanged
        _LineRender.notifyDataChanged(_lines);
    }


    private RectF _MainPlotRect = new RectF();// 主要的 图谱区域

    /**
     * 限制绘图区域的上下左右
     */
    private void limitPlotArea() {

        _MainPlotRect.setEmpty();

        // 计算其他的 offset

        // 0. padding
        int padding=5;
        _MainPlotRect.left=padding;
        _MainPlotRect.top=padding;
        _MainPlotRect.right=padding;
        _MainPlotRect.bottom=padding;

        // 1. 计算label的 宽高
        offsetLabel();

    }

    private void offsetLabel() {

        _MainPlotRect.bottom += _XAxisRender.offsetBottom();

        float ymin = _lines.getmYMin();
        String label = _YAxisRender.get_ValueAdapter().value2String(ymin);

        _MainPlotRect.left += _YAxisRender.offsetLeft(label);

    }


    private void prepareMap() {

        float xMin = _lines.getmXMin();
        float xMax = _lines.getmXMax();
        float yMin = _lines.getmYMin();
        float yMax = _lines.getmYMax();

        _ViewPortManager.setViewPort(_MainPlotRect.left, _MainPlotRect.top, _MainPlotRect.right, _MainPlotRect.bottom);
        _TransformManager.prepareRelation(
                xMin, xMax - xMin,
                yMin, yMax - yMin);
    }


    public void setLines(Lines lines) {
        _lines = lines;

        dataChanged();
        postInvalidate();
    }

    public Lines getlines() {
        return _lines;
    }

    public TransformManager get_TransformManager() {
        return _TransformManager;
    }

    public ViewPortManager get_ViewPortManager() {
        return _ViewPortManager;
    }
}
