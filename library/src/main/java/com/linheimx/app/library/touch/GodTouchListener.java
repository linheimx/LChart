package com.linheimx.app.library.touch;

import android.graphics.RectF;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewParent;

import com.linheimx.app.library.charts.LineChart;
import com.linheimx.app.library.manager.MappingManager;
import com.linheimx.app.library.utils.RectD;

/**
 * Created by lijian on 2017/1/30.
 */

public class GodTouchListener implements View.OnTouchListener {

    GestureDetector _GestureDetector;
    ScaleGestureDetector _ScaleGestureDetector;
    LineChart _LineChart;

    public GodTouchListener(LineChart lineChart) {
        _LineChart = lineChart;
        _GestureDetector = new GestureDetector(lineChart.getContext(), new GestureListener());
        _ScaleGestureDetector = new ScaleGestureDetector(lineChart.getContext(), new ScaleListener());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        ViewParent parent = v.getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(true);
        }

        boolean hit = _GestureDetector.onTouchEvent(event);
        hit |= _ScaleGestureDetector.onTouchEvent(event);
        if (hit) {
            _LineChart.invalidate();
            return true;
        }

        return false;
    }


    class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        float _lastSpanX, _lastSpanY;

        public ScaleListener() {
            super();
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {

            float spanX = detector.getCurrentSpanX();
            float kx = spanX / _lastSpanX;

            float spanY = detector.getCurrentSpanY();
            float ky = spanY / _lastSpanY;

            RectF godRect = _LineChart.get_GodRect();
            RectF mainRect = _LineChart.get_MainPlotRect();

            godRect.right = godRect.left + godRect.width() * kx;
            godRect.bottom = godRect.top + godRect.height() * ky;

            constrainRect(godRect, mainRect);

            nofityViewPortChanged(godRect);

            _lastSpanX = spanX;
            _lastSpanY = spanY;

            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {

            _lastSpanX = detector.getCurrentSpanX();
            _lastSpanY = detector.getCurrentSpanY();

            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            super.onScaleEnd(detector);
        }
    }


    RectD _rectD_ob = new RectD();

    class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            RectF godRect = _LineChart.get_GodRect();
            RectF mainRect = _LineChart.get_MainPlotRect();


            float w = godRect.width();
            float h = godRect.height();
            godRect.left += -distanceX;
            godRect.right = godRect.left + w;
            godRect.top += -distanceY;
            godRect.bottom = godRect.top + h;

            constrainRect(godRect, mainRect);

            nofityViewPortChanged(godRect);

            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return super.onSingleTapConfirmed(e);
        }
    }


    private void nofityViewPortChanged(RectF godRect) {

        // 计算出godRect对应的viewport
        MappingManager mappingManager = _LineChart.get_MappingManager();
        double left = mappingManager.p2v_x(godRect.left);
        double right = mappingManager.p2v_x(godRect.right);
        double bottom = mappingManager.p2v_y(godRect.bottom);
        double top = mappingManager.p2v_y(godRect.top);

        _rectD_ob.left = left;
        _rectD_ob.top = top;
        _rectD_ob.right = right;
        _rectD_ob.bottom = bottom;
        _LineChart.notifyOB_ViewportChanged(_rectD_ob);
    }


    private void constrainRect(RectF godRect, RectF maxRect) {
        float w = godRect.width();
        float h = godRect.height();

        // 1. 保持比例
        if (godRect.left < maxRect.left) {
            godRect.left = maxRect.left;
            godRect.right = godRect.left + w;
        }
        if (godRect.top < maxRect.top) {
            godRect.top = maxRect.top;
            godRect.bottom = godRect.top + h;
        }
        if (godRect.right > maxRect.right) {
            godRect.right = maxRect.right;
            godRect.left = godRect.right - w;
        }
        if (godRect.bottom > maxRect.bottom) {
            godRect.bottom = maxRect.bottom;
            godRect.top = godRect.bottom - h;
        }

        // 2. 限定值的范围
        if (godRect.left < maxRect.left) {
            godRect.left = maxRect.left;
        }
        if (godRect.top < maxRect.top) {
            godRect.top = maxRect.top;
        }
        if (godRect.right > maxRect.right) {
            godRect.right = maxRect.right;
        }
        if (godRect.bottom > maxRect.bottom) {
            godRect.bottom = maxRect.bottom;
        }
    }

}
