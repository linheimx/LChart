package com.linheimx.app.library.touch;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

import com.linheimx.app.library.charts.LineChart;
import com.linheimx.app.library.manager.MappingManager;
import com.linheimx.app.library.manager.FrameManager;

/**
 * Created by Administrator on 2016/11/20.
 */

public class TouchListener implements View.OnTouchListener {

    GestureDetector _GestureDetector;

    LineChart _LineChart;
    FrameManager _FrameManager;
    MappingManager _MappingManager;

    VelocityTracker _VelocityTracker;
    TouchMode _TouchMode = TouchMode.NONE;

    public TouchListener(LineChart lineChart) {
        this._LineChart = lineChart;

        _GestureDetector = new GestureDetector(_LineChart.getContext(), new GestureListener());
        _FrameManager = lineChart.get_FrameManager();
        _MappingManager = lineChart.get_MappingManager();


    }


    float _lastX, _lastY;
    float _disX = 1, _disY = 1, _disXY = 1, _cX, _cY;

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
                    _disX = getXDist(event);
                    _disY = getYDist(event);
                    _disXY = getABSDist(event);
                    float tmpX = event.getX(0) + event.getX(1);
                    float tmpY = event.getY(0) + event.getY(1);
                    _cX = (tmpX / 2f);
                    _cY = (tmpY / 2f);

                    if (_disXY > 10) {
                        _TouchMode = TouchMode.PINCH_ZOOM;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = x - _lastX;
                float dy = y - _lastY;

                if (_TouchMode == TouchMode.DRAG) {
                    doDrag(dx, dy);
                } else if (_TouchMode == TouchMode.PINCH_ZOOM) {
                    doPinch(event);
                } else if (_TouchMode == TouchMode.NONE) {
                    _TouchMode = TouchMode.DRAG;
                }

                _lastX = x;
                _lastY = y;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                _TouchMode = TouchMode.NONE;
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


    private void doPinch(MotionEvent event) {
        float absDist = getABSDist(event);
        float scale = absDist / _disXY;
        zoom(scale, scale, _cX, _cY);

        _disXY = absDist;
    }


    private void doDrag(float dx, float dy) {
        _MappingManager.translate(dx, dy);
        _LineChart.postInvalidate();
    }


    boolean canX_zoom =true;
    boolean canY_zoom =false;

    private void zoom(float scaleX, float scaleY, float cx, float cy) {
        cx = cx - _FrameManager.frameLeft();
        cy = cy - _FrameManager.frameTop();

        if(!canX_zoom){
            scaleX=1;
        }
        if(!canY_zoom){
            scaleY=1;
        }

        _MappingManager.zoom(scaleX, scaleY, cx, cy);
        _LineChart.postInvalidate();
    }


    private static float getABSDist(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    private static float getXDist(MotionEvent e) {
        float x = Math.abs(e.getX(0) - e.getX(1));
        return x;
    }

    private static float getYDist(MotionEvent e) {
        float y = Math.abs(e.getY(0) - e.getY(1));
        return y;
    }


    class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDoubleTap(MotionEvent e) {

            float x = e.getX() - _FrameManager.offsetLeft();
            float y = e.getY() - _FrameManager.offsetTop();

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

            _LineChart.highLight_PixXY(e.getX(), e.getY());
            return true;
        }
    }

    enum TouchMode {
        NONE, DRAG, X_ZOOM, Y_ZOOM, PINCH_ZOOM, ROTATE, SINGLE_TAP, DOUBLE_TAP, LONG_PRESS, FLING
    }
}
