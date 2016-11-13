package com.linheimx.app.library.charts;

import android.content.Context;
import android.util.AttributeSet;

import com.linheimx.app.library.manager.TransformManager;
import com.linheimx.app.library.manager.ViewPortManager;

/**
 * Created by Administrator on 2016/11/13.
 */

public class LineChart extends Chart {

    ViewPortManager _viewPortManager;
    TransformManager _transformManager;

    public LineChart(Context context) {
        super(context);
    }

    public LineChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LineChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        _viewPortManager = new ViewPortManager();
        _transformManager = new TransformManager(_viewPortManager);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        _viewPortManager.setChartWH(w, h);

        dataChanged();
    }


    private void dataChanged() {

    }

}
