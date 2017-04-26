package com.linheimx.app.library.charts;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.UiThread;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.linheimx.app.library.R;
import com.linheimx.app.library.adapter.IValueAdapter;
import com.linheimx.app.library.animate.LAnimator;
import com.linheimx.app.library.data.Entry;
import com.linheimx.app.library.data.Line;
import com.linheimx.app.library.data.Lines;
import com.linheimx.app.library.listener.IDragListener;
import com.linheimx.app.library.manager.MappingManager;
import com.linheimx.app.library.model.HighLight;
import com.linheimx.app.library.model.XAxis;
import com.linheimx.app.library.model.YAxis;
import com.linheimx.app.library.render.GodRender;
import com.linheimx.app.library.render.HighLightRender;
import com.linheimx.app.library.render.LineRender;
import com.linheimx.app.library.render.NoDataRender;
import com.linheimx.app.library.render.XAxisRender;
import com.linheimx.app.library.render.YAxisRender;
import com.linheimx.app.library.touch.GodTouchListener;
import com.linheimx.app.library.touch.TouchListener;
import com.linheimx.app.library.utils.LogUtil;
import com.linheimx.app.library.utils.RectD;
import com.linheimx.app.library.utils.SingleD_XY;
import com.linheimx.app.library.utils.Utils;

import java.util.List;

/**
 * Created by lijian on 2016/11/13.
 */

public class LineChart extends Chart {

    MappingManager _MappingManager;

    Lines _lines;

    ////////////////////////////  listener  ///////////////////////////
    IDragListener _dragListener;

    ////////////////////////// function //////////////////////////
    boolean isHighLightEnabled = true;
    boolean isTouchEnabled = true;
    boolean isDragable = true;
    boolean isScaleable = true;
    ChartMode _ChartMode;

    ///////////////////////////////// parts ////////////////////////////////
    XAxis _XAxis;
    YAxis _YAxis;
    HighLight _HighLight;

    //////////////////////////////////  render  /////////////////////////////
    NoDataRender _NoDataRender;
    XAxisRender _XAxisRender;
    YAxisRender _YAxisRender;
    LineRender _LineRender;
    HighLightRender _HighLightRender;
    GodRender _GodRender;


    ////////////////////////////// touch  /////////////////////////////
    TouchListener _TouchListener;
    GodTouchListener _GodTouchListener;

    //////////////////////////// 区域 ///////////////////////////
    RectF _MainPlotRect;// 主要的绘图区域

    float _paddingLeft = 40;
    float _paddingRight = 5;
    float _paddingTop = 17;
    float _paddingBottom = 15;

    RectF _GodRect;//

    //////////////////////////////  animator  ////////////////////////////
    LAnimator _LAnimator;


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
    protected void init(Context context, AttributeSet attributeSet) {
        super.init(context, attributeSet);

        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.LineChart);
        boolean isGodMode = typedArray.getBoolean(R.styleable.LineChart_god_mode, false);
        _ChartMode = isGodMode ? ChartMode.God : ChartMode.Normal;
        typedArray.recycle();

        // animator
        _LAnimator = new LAnimator(this);

        // init v
        _MainPlotRect = new RectF();
        _MappingManager = new MappingManager(_MainPlotRect);
        _GodRect = new RectF();

        // models
        _XAxis = new XAxis();
        _YAxis = new YAxis();
        _HighLight = new HighLight();

        // render
        _NoDataRender = new NoDataRender(_MainPlotRect, _MappingManager);
        _XAxisRender = new XAxisRender(_MainPlotRect, _MappingManager, _XAxis);
        _YAxisRender = new YAxisRender(_MainPlotRect, _MappingManager, _YAxis);
        _LineRender = new LineRender(_MainPlotRect, _MappingManager, _lines, this);
        _HighLightRender = new HighLightRender(_MainPlotRect, _MappingManager, _lines, _HighLight);
        _GodRender = new GodRender(_MainPlotRect, _MappingManager, _GodRect);

        // touch listener
        _TouchListener = new TouchListener(this);
        _GodTouchListener = new GodTouchListener(this);

        // other
        _paddingLeft = Utils.dp2px(_paddingLeft);
        _paddingRight = Utils.dp2px(_paddingRight);
        _paddingTop = Utils.dp2px(_paddingTop);
        _paddingBottom = Utils.dp2px(_paddingBottom);
    }


    @Override
    public void computeScroll() {
        super.computeScroll();

        if (_ChartMode == ChartMode.Normal) {
            _TouchListener.computeScroll();
        }
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

        if (_ChartMode == ChartMode.Normal) {
            return _TouchListener.onTouch(this, event);
        } else if (_ChartMode == ChartMode.God) {
            return _GodTouchListener.onTouch(this, event);
        }

        return false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 1. render no data
        if (_lines == null || _lines.getLines() == null || _lines.getLines().size() == 0) {
            _NoDataRender.render(canvas);
            return;
        }

        // 计算轴线上的数值
        Line line = _lines.getLines().get(0);
        _XAxis.calValues(getVisiableMinX(), getVisiableMaxX(), line);
        _YAxis.calValues(getVisiableMinY(), getVisiableMaxY(), line);

        // render grid line
        _XAxisRender.renderGridline(canvas);
        _YAxisRender.renderGridline(canvas);

        // render line
        _LineRender.render(canvas);
        // render high light
        _HighLightRender.render(canvas);


        // render god
        if (_ChartMode == ChartMode.God) {
            _GodRender.render(canvas);
        }

        // render warn line
        _XAxisRender.renderWarnLine(canvas);
        _YAxisRender.renderWarnLine(canvas);

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

        _LineRender.onChartSizeChanged(w, h);
        notifyDataChanged();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        _lines = null;
    }

    /**
     * 通知数据改变
     */
    public void notifyDataChanged() {

        limitMainPlotArea();

        if (_lines == null) {
            return;
        }
        _lines.calMinMax();

        prepareMap();

        // 2. onDataChanged
        _LineRender.onDataChanged(_lines);
        _HighLightRender.onDataChanged(_lines);
    }

    /**
     * 限制 主绘图区域的边界
     */
    private void limitMainPlotArea() {

        _MainPlotRect.setEmpty();
        _MainPlotRect.right += _MainPlotRect.left + getWidth();
        _MainPlotRect.bottom += _MainPlotRect.top + getHeight();

        // 0. padding
        offsetPadding();
        // 1. 计算label,unit的宽高
        offsetArea();

        if (_ChartMode == ChartMode.God) {
            _GodRect.set(_MainPlotRect);
            _GodRect.right = _GodRect.right / 3;
        }
    }

    private void offsetPadding() {

        // 考虑图例
        if (_lines != null) {
            int h = 0;
            for (Line line : _lines.getLines()) {
                if (line.getLegendHeight() > h) {
                    h = line.getLegendHeight();
                }
            }

            if (h > 0) {
                _paddingTop = h;
            }
        }

        _MainPlotRect.left += _paddingLeft;
        _MainPlotRect.top += _paddingTop;
        _MainPlotRect.right -= _paddingRight;
        _MainPlotRect.bottom -= _paddingBottom;
    }

    private void offsetArea() {

        _YAxisRender.paint_label();
        _YAxisRender.paint_unit();
        Paint paintLabel = _YAxisRender.get_PaintLabel();
        Paint paintUnit = _YAxisRender.get_PaintUnit();

        /*******************************  取一堆label中的head middle tail 中的最大值  ***********************************/
        float labelDimen = _YAxis.getLabelDimen();

        if (_lines != null && _lines.getLines().size() > 0) {
            Line line = _lines.getLines().get(0);

            if (line.getEntries().size() > 0) {
                List<Entry> entryList = line.getEntries();

                Entry head = entryList.get(0);
                Entry tail = entryList.get(entryList.size() - 1);
                Entry middle = entryList.get((0 + entryList.size() - 1) / 2);

                IValueAdapter yAdapter = _YAxis.get_ValueAdapter();
                String s1 = yAdapter.value2String(head.getY());
                float w1 = Utils.textWidth(paintLabel, s1);
                if (labelDimen < w1) {
                    labelDimen = w1;
                }

                String s2 = yAdapter.value2String(middle.getY());
                float w2 = Utils.textWidth(paintLabel, s2);
                if (labelDimen < w2) {
                    labelDimen = w2;
                }

                String s3 = yAdapter.value2String(tail.getY());
                float w3 = Utils.textWidth(paintLabel, s3);
                if (labelDimen < w3) {
                    labelDimen = w3;
                }
            }

        }

        // 考虑y label和unit
        _MainPlotRect.left += _YAxis.offsetLeft(labelDimen, Utils.textHeight(paintUnit));
        // 考虑x label和unit
        _MainPlotRect.bottom -= _XAxis.offsetBottom(Utils.textHeight(paintLabel), Utils.textHeight(paintUnit));
    }


    /**
     * 准备映射关系
     */
    private void prepareMap() {

        double xMin = _lines.getmXMin();
        double xMax = _lines.getmXMax();
        double yMin = _lines.getmYMin();
        double yMax = _lines.getmYMax();

        _MappingManager.prepareRelation(xMin, xMax, yMin, yMax);
    }

    /**
     * 设置数据源
     *
     * @param lines
     */
    public void setLines(Lines lines) {
        _lines = lines;

        notifyDataChanged();
        postInvalidate();
    }

    public Lines getlines() {
        return _lines;
    }

    /**
     * 清空数据
     */
    public void clearData() {
        _lines = null;
        invalidate();
    }


    private void A______________________________________________() {

    }

    /**
     * Y方向进行动画
     */
    @UiThread
    public void animateY() {
        _LAnimator.animateY();
    }

    /**
     * X方向进行动画
     */
    @UiThread
    public void animateX() {
        _LAnimator.animateX();
    }

    /**
     * X,Y方向进行动画
     */
    @UiThread
    public void animateXY() {
        _LAnimator.animateXY();
    }

    public LAnimator get_LAnimator() {
        return _LAnimator;
    }

    public void set_LAnimator(LAnimator _LAnimator) {
        this._LAnimator = _LAnimator;
    }


    private void B_________________________________________________() {

    }

    private void a______________________________________________() {

    }

    public boolean isCanX_zoom() {
        return _TouchListener.isCanX_zoom();
    }

    public void setCanX_zoom(boolean canX_zoom) {
        _TouchListener.setCanX_zoom(canX_zoom);
    }

    public boolean isCanY_zoom() {
        return _TouchListener.isCanY_zoom();
    }

    public void setCanY_zoom(boolean canY_zoom) {
        _TouchListener.setCanY_zoom(canY_zoom);
    }

    public boolean isDragable() {
        return isDragable;
    }

    public void setDragable(boolean dragable) {
        isDragable = dragable;
    }

    public boolean isScaleable() {
        return isScaleable;
    }

    public void setScaleable(boolean scaleable) {
        isScaleable = scaleable;
    }

    public IDragListener get_dragListener() {
        return _dragListener;
    }

    public void set_dragListener(IDragListener _dragListener) {
        this._dragListener = _dragListener;
    }

    private void b______________________________________________() {

    }

    public float get_paddingLeft() {
        return _paddingLeft;
    }

    public void set_paddingLeft(float _paddingLeft) {
        this._paddingLeft = _paddingLeft;
    }

    public float get_paddingRight() {
        return _paddingRight;
    }

    public void set_paddingRight(float _paddingRight) {
        this._paddingRight = _paddingRight;
    }

    public float get_paddingTop() {
        return _paddingTop;
    }

    public void set_paddingTop(float _paddingTop) {
        this._paddingTop = _paddingTop;
    }

    public float get_paddingBottom() {
        return _paddingBottom;
    }

    public void set_paddingBottom(float _paddingBottom) {
        this._paddingBottom = _paddingBottom;
    }

    private void c______________________________________________() {

    }

    public double getVisiableMinX() {
        float px = _MainPlotRect.left;
        SingleD_XY xy = _MappingManager.getValueByPx(px, 0);
        return xy.getX();
    }

    public double getVisiableMaxX() {
        float px = _MainPlotRect.right;
        SingleD_XY xy = _MappingManager.getValueByPx(px, 0);
        return xy.getX();
    }

    public double getVisiableMinY() {
        float py = _MainPlotRect.bottom;
        SingleD_XY xy = _MappingManager.getValueByPx(0, py);
        return xy.getY();
    }

    public double getVisiableMaxY() {
        float py = _MainPlotRect.top;
        SingleD_XY xy = _MappingManager.getValueByPx(0, py);
        return xy.getY();
    }

    public RectD get_currentViewPort() {
        return _MappingManager.get_currentViewPort();
    }

    public void set_currentViewPort(RectD currentViewPort) {
        _MappingManager.set_currentViewPort(currentViewPort);
    }

    /**
     * 设置y轴的范围
     *
     * @param yMin
     * @param yMax
     */
    public void setYMax_Min(double yMin, double yMax) {
        if (_lines != null) {
            _lines.setYCustomMaxMin(true);
            _lines.setmYMin(yMin);
            _lines.setmYMax(yMax);
            postInvalidate();
        } else {
            LogUtil.e("setAxisMaxMin: 请在lines设置后，调用此方法！");
        }
    }

    /**
     * 设置轴线的范围
     *
     * @param xMin
     * @param xMax
     */
    public void setXAix_MaxMin(double xMin, double xMax) {
        if (_lines != null) {
            _lines.setXCustomMaxMin(true);
            _lines.setmXMin(xMin);
            _lines.setmXMax(xMax);
            postInvalidate();
        } else {
            LogUtil.e("setAxisMaxMin: 请在lines设置后，调用此方法！");
        }
    }

    private void d______________________________________________() {

    }

    /**
     * 获取映射管家
     *
     * @return
     */
    public MappingManager get_MappingManager() {
        return _MappingManager;
    }


    ////////////////////////////////  便捷的方法  //////////////////////////////////

    public void highLight_PixXY(float px, float py) {
        SingleD_XY xy = _MappingManager.getValueByPx(px, py);
        highLight_ValueXY(xy.getX(), xy.getY());
    }

    public void highLight_ValueXY(double x, double y) {
        _HighLightRender.highLight_ValueXY(x, y);
        invalidate();
    }

    public void highLightLeft() {
        _HighLightRender.highLightLeft();
        invalidate();
    }

    public void highLightRight() {
        _HighLightRender.highLightRight();
        invalidate();
    }

    public HighLight get_HighLight() {
        return _HighLight;
    }

    public void set_HighLight(HighLight _HighLight) {
        this._HighLight = _HighLight;
    }

    public XAxis get_XAxis() {
        return _XAxis;
    }

    public YAxis get_YAxis() {
        return _YAxis;
    }

    public RectF get_GodRect() {
        return _GodRect;
    }

    public void set_GodRect(RectF _GodRect) {
        this._GodRect = _GodRect;
    }

    public RectF get_MainPlotRect() {
        return _MainPlotRect;
    }

    public void set_MainPlotRect(RectF _MainPlotRect) {
        this._MainPlotRect = _MainPlotRect;
    }

    public ChartMode get_ChartMode() {
        return _ChartMode;
    }

    public LineChart set_ChartMode(ChartMode _ChartMode) {
        this._ChartMode = _ChartMode;
        return this;
    }

    LineChart _ob_linechart;

    public void registObserver(LineChart lineChartOb) {
        this._ob_linechart = lineChartOb;

        notifyDataChanged_FromOb(lineChartOb);
    }

    public void notifyDataChanged_FromOb(LineChart lineChartOb) {

        // x,y轴上的单位
        XAxis xAxis = lineChartOb.get_XAxis();
        this.get_XAxis().set_unit(xAxis.get_unit());

        YAxis yAxis = lineChartOb.get_YAxis();
        this.get_YAxis().set_unit(yAxis.get_unit());

        this.setLines(lineChartOb.getlines());
    }

    public void notifyOB_ViewportChanged(RectD _currentViewPort) {
        _ob_linechart.set_CurrentViewPort(_currentViewPort);
        _ob_linechart.invalidate();
    }

    public LineChart set_CurrentViewPort(RectD _currentViewPort) {
        _MappingManager.set_currentViewPort(_currentViewPort);
        return this;
    }


    public enum ChartMode {
        Normal, God
    }


}
