package com.linheimx.app.library.touch;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.linheimx.app.library.charts.LineChart;
import com.linheimx.app.library.manager.TransformManager;
import com.linheimx.app.library.manager.FrameManager;

/**
 * Created by Administrator on 2016/11/20.
 */

public class TouchListenerPlus implements View.OnTouchListener {

    GestureDetector _GestureDetector;
    ScaleGestureDetector _ScaleGestureDetector;

    LineChart _LineChart;
    FrameManager _FrameManager;
    TransformManager _TransformManager;

    public TouchListenerPlus(LineChart lineChart) {
        this._LineChart = lineChart;

        _GestureDetector = new GestureDetector(_LineChart.getContext(), new GestureListener());
        _ScaleGestureDetector = new ScaleGestureDetector(_LineChart.getContext(), new ScaleListener());

        _FrameManager = lineChart.get_FrameManager();
        _TransformManager = lineChart.get_TransformManager();
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        boolean res = _ScaleGestureDetector.onTouchEvent(event);

        return res;
    }


    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDoubleTap(MotionEvent e) {

            float x = e.getX() - _FrameManager.offsetLeft();
            float y = e.getY();

            return true;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return super.onDown(e);
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
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {

            _LineChart.highLight_PixXY(e.getX(), e.getY());
            return true;
        }
    }


    private final class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        float lastSpanX;
        float lastSpanY;

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {

            lastSpanX = ScaleGestureCompat.getCurrentSpanX(detector);
            lastSpanY = ScaleGestureCompat.getCurrentSpanY(detector);
            return true;
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float spanX = ScaleGestureCompat.getCurrentSpanX(detector);
            float spanY = ScaleGestureCompat.getCurrentSpanY(detector);

            float focusX = detector.getFocusX();
            float focusY = detector.getFocusY();

            zoom(spanX / lastSpanX, spanY / lastSpanY, focusX, focusY);

            lastSpanX = spanX;
            lastSpanY = spanY;

            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            super.onScaleEnd(detector);
        }
    }


    private void zoom(float scaleX, float scaleY, float cx, float cy) {
        _TransformManager.zoom(scaleX, scaleY, cx, cy);
        _LineChart.postInvalidate();
    }
}
