package com.linheimx.app.library.charts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.linheimx.app.library.data.Lines;
import com.linheimx.app.library.manager.TransformManager;
import com.linheimx.app.library.manager.ViewPortManager;
import com.linheimx.app.library.parts.XAxis;
import com.linheimx.app.library.parts.YAxis;
import com.linheimx.app.library.render.LineRender;
import com.linheimx.app.library.render.NoDataRender;
import com.linheimx.app.library.render.XAxisRender;
import com.linheimx.app.library.render.YAxisRender;
import com.linheimx.app.library.touch.TouchListener;
import com.linheimx.app.library.utils.Single_XY;
import com.linheimx.app.library.utils.Utils;

/**
 * Created by Administrator on 2016/11/13.
 */

public class LineChart extends Chart {

    ViewPortManager _ViewPortManager;
    TransformManager _TransformManager;

    Lines _lines;

    ///////////////////////////////// parts ////////////////////////////////
    XAxis _XAxis;
    YAxis _YAxis;

    //////////////////////////////////  render  /////////////////////////////
    NoDataRender _NoDataRender;
    XAxisRender _XAxisRender;
    YAxisRender _YAxisRender;
    LineRender _LineRender;


    ////////////////////////////// touch  /////////////////////////////
    TouchListener _touchListener;

    //////////////////////////// other ///////////////////////////
    private RectF _MainPlotRect = new RectF();// 主要的 图谱区域
    float padding = 15;


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

        // parts
        _XAxis = new XAxis();
        _YAxis = new YAxis();

        // render
        _NoDataRender = new NoDataRender(_ViewPortManager, _TransformManager);
        _XAxisRender = new XAxisRender(_ViewPortManager, _TransformManager, _XAxis);
        _YAxisRender = new YAxisRender(_ViewPortManager, _TransformManager, _YAxis);
        _LineRender = new LineRender(_ViewPortManager, _TransformManager, _lines, this);

        // touch listener
        _touchListener = new TouchListener(this);


        ////////////////////// other  ///////////////////////
        setXAxisUnit("mm/s");
        setYAxisUnit("hz");
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

        // 计算轴线上的数值
        _XAxis.calValues(getVisiableMinX(), getVisiableMaxX());
        _YAxis.calValues(getVisiableMinY(), getVisiableMaxY());

        canvas.save();
        canvas.clipRect(_ViewPortManager.getContentRect());

        // render grid line
        _XAxisRender.renderGridline(canvas);
        _YAxisRender.renderGridline(canvas);

        // render line
        _LineRender.render(canvas);
        canvas.restore();

        // render Axis
        _XAxisRender.renderAxisLine(canvas);
        _YAxisRender.renderAxisLine(canvas);

        // render labels
        _XAxisRender.renderLabels(canvas);
        _YAxisRender.renderLabels(canvas);

        // render unit
        _XAxisRender.renderUnit(canvas);
        _YAxisRender.renderUnit(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        _ViewPortManager.setChartWH(w, h);

        notifyDataChanged();
    }


    /**
     * 通知数据改变
     */
    private void notifyDataChanged() {

        if (_lines == null) {
            return;
        }

        /************************  各部分请做好自己的职责  *************************/
        _XAxis.internalSetup();
        _YAxis.internalSetup();

        limitMainPlotArea();

        prepareMap();

        // 2. notifyDataChanged
        _LineRender.notifyDataChanged(_lines);
    }

    /**
     * 限制 主绘图区域的边界
     */
    private void limitMainPlotArea() {

        _MainPlotRect.setEmpty();

        // 0. padding
        offsetPadding();
        // 1. 计算label,unit的宽高
        offsetArea();

    }

    private void offsetPadding() {
        float pad = Utils.dp2px(padding);
        // other
        _MainPlotRect.left += pad;
        _MainPlotRect.top += pad;
        _MainPlotRect.right += pad;
        _MainPlotRect.bottom += pad;
    }

    private void offsetArea() {
        // bottom
        _MainPlotRect.bottom += _XAxis.offsetBottom();
        // left
        _MainPlotRect.left += _XAxis.offsetLeft();

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


    public float getVisiableMinX() {
        float px = _ViewPortManager.contentLeft();
        Single_XY xy = _TransformManager.getValueByPx(px, 0);
        return xy.getX();
    }

    public float getVisiableMaxX() {
        float px = _ViewPortManager.contentRight();
        Single_XY xy = _TransformManager.getValueByPx(px, 0);
        return xy.getX();
    }

    public float getVisiableMinY() {
        float py = _ViewPortManager.contentBottom();
        Single_XY xy = _TransformManager.getValueByPx(0, py);
        return xy.getY();
    }

    public float getVisiableMaxY() {
        float py = _ViewPortManager.contentTop();
        Single_XY xy = _TransformManager.getValueByPx(0, py);
        return xy.getY();
    }


    public void setLines(Lines lines) {
        _lines = lines;

        notifyDataChanged();
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


    ////////////////////////////////  便捷的方法  //////////////////////////////////

    public void highLight_PixXY(float px, float py) {
        Single_XY xy = _TransformManager.getValueByPx(px, py);
        highLight_ValueXY(xy.getX(), xy.getY());
    }

    public void highLight_ValueXY(float x, float y) {
        _LineRender.highLight_ValueXY(x, y);
        invalidate();
    }

    public void highLightLeft() {

    }

    public void highLightRight() {

    }

    public void setXAxisUnit(String unit) {
        _XAxis.set_unit(unit);
    }

    public void setYAxisUnit(String unit) {
        _YAxis.set_unit(unit);
    }


}
