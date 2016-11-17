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
import com.linheimx.app.library.utils.LogUtil;
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
        _LineRender = new LineRender(_ViewPortManager, _TransformManager, _lines);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 1. draw no data
        if (_lines == null || _lines.getLines().size() == 0) {
            _NoDataRender.draw(canvas);
        }

        // 2. draw line
        _LineRender.draw(canvas);

        // 3. draw axis,labels,grid line
        _XAxisRender.draw(canvas);
        _YAxisRender.draw(canvas);



    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        _ViewPortManager.setChartWH(w, h);

        float off = Utils.dp2px(30);
        _ViewPortManager.restrainViewPort(off, off, off, off);

        dataChanged();
    }

    public void setLines(Lines lines) {
        _lines = lines;

        dataChanged();
        postInvalidate();
    }


    private void dataChanged() {

        if (_lines == null) {
            return;
        }


        // 1. axis
        _XAxis.prepareData(_lines.getmXMin(), _lines.getmXMax());
        _YAxis.prepareData(_lines.getmYMin(), _lines.getmYMax());

        _TransformManager.prepareRelation(_XAxis, _YAxis);

        _XAxis.stepValues();
        _YAxis.stepValues();

        // 2. notifyDataChanged
        _LineRender.notifyDataChanged(_lines);
    }

}
