package com.linheimx.app.library.touch;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

import com.linheimx.app.library.charts.LineChart;
import com.linheimx.app.library.manager.TransformManager;
import com.linheimx.app.library.manager.ViewPortManager;
import com.linheimx.app.library.utils.LogUtil;
import com.linheimx.app.library.utils.Utils;

/**
 * Created by Administrator on 2016/11/20.
 */

public class TouchListener implements View.OnTouchListener {

    GestureDetector _GestureDetector;

    LineChart _LineChart;
    ViewPortManager _ViewPortManager;
    TransformManager _TransformManager;

    VelocityTracker _VelocityTracker;
    TouchMode _TouchMode = TouchMode.NONE;

    public TouchListener(LineChart lineChart) {
        this._LineChart = lineChart;

        _GestureDetector = new GestureDetector(_LineChart.getContext(), new GestureListener());
        _ViewPortManager = lineChart.get_ViewPortManager();
        _TransformManager = lineChart.get_TransformManager();


    }


    float _lastX, _lastY;

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        // 速度跟踪
        if (_VelocityTracker == null) {
            _VelocityTracker = VelocityTracker.obtain();
        }
        _VelocityTracker.addMovement(event);
        if (event.getActionMasked() == MotionEvent.ACTION_CANCEL) {
            if (_VelocityTracker != null) {
                _VelocityTracker.recycle();
                _VelocityTracker = null;
            }
        }

        _GestureDetector.onTouchEvent(event);

        float x = event.getX();
        float y = event.getY();

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                _lastX = x;
                _lastY = y;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                if (event.getPointerCount() >= 2) {
                    _TouchMode = TouchMode.PINCH_ZOOM;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = x - _lastX;
                float dy = y - _lastY;

                if (_TouchMode == TouchMode.DRAG) {
                    drag(dx, dy);
                } else if (_TouchMode == TouchMode.PINCH_ZOOM) {

                } else if (_TouchMode == TouchMode.NONE) {
                    _TouchMode = TouchMode.DRAG;
                }

                _lastX = x;
                _lastY = y;
                break;
            case MotionEvent.ACTION_UP:
                _TouchMode = TouchMode.NONE;
                break;
            case MotionEvent.ACTION_CANCEL:
                _TouchMode = TouchMode.NONE;
                break;
        }

        return true;
    }


    private void drag(float dx, float dy) {
        _TransformManager.translate(dx, dy);
        _LineChart.postInvalidate();
    }

    private void zoom(float scaleX, float scaleY, float cx, float cy) {
        _TransformManager.zoom(scaleX, scaleY, cx, cy);
        _LineChart.postInvalidate();
    }

    class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDoubleTap(MotionEvent e) {

            super.onDoubleTap(e);

            float x = e.getX() - _ViewPortManager.offsetLeft();
            float y = e.getY();

            zoom(1.4f, 1.4f, x, y);
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
            return super.onSingleTapConfirmed(e);
        }
    }

    enum TouchMode {
        NONE, DRAG, X_ZOOM, Y_ZOOM, PINCH_ZOOM, ROTATE, SINGLE_TAP, DOUBLE_TAP, LONG_PRESS, FLING
    }
}
