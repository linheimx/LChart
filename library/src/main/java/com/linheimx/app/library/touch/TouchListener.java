package com.linheimx.app.library.touch;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

import com.linheimx.app.library.charts.LineChart;
import com.linheimx.app.library.manager.MappingManager;
import com.linheimx.app.library.utils.LogUtil;

/**
 * Created by Administrator on 2016/11/20.
 */

public class TouchListener implements View.OnTouchListener {

    GestureDetector _GestureDetector;
    Zoomer _Zoomer;

    LineChart _LineChart;
    MappingManager _MappingManager;

    VelocityTracker _VelocityTracker;
    TouchMode _TouchMode = TouchMode.NONE;

    public TouchListener(LineChart lineChart, Zoomer zoomer) {
        this._LineChart = lineChart;

        _Zoomer = zoomer;
        _GestureDetector = new GestureDetector(_LineChart.getContext(), new GestureListener());
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

        boolean hit = _GestureDetector.onTouchEvent(event);
        if (hit) {
            _LineChart.invalidate();
            return true;
        }

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

//                    if (_disXY > 10) {
                    _TouchMode = TouchMode.PINCH_ZOOM;
//                    }
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

    public void computeScroll() {
        if (_Zoomer.computeZoom()) {
            float currentLevel = _Zoomer.getCurrentZoom();
            zoom(currentLevel, _zoom_w, _zoom_h, _zoom_cx, _zoom_cy);
        }
    }

    private void doPinch(MotionEvent event) {

        float absDist = getABSDist(event);
        float scale = _disXY / absDist;

        zoom(scale, scale, _cX, _cY);

        _disXY = absDist;
    }


    private void doDrag(float dx, float dy) {
        _MappingManager.translate(dx, dy);
        _LineChart.postInvalidate();
    }


    boolean canX_zoom = true;
    boolean canY_zoom = true;

    /**
     * 应用于指定的缩放
     *
     * @param scaleX
     * @param scaleY
     * @param cx
     * @param cy
     */
    private void zoom(float scaleX, float scaleY, float cx, float cy) {

        if (!canX_zoom) {
            scaleX = 1;
        }
        if (!canY_zoom) {
            scaleY = 1;
        }

        _MappingManager.zoom(scaleX, scaleY, cx, cy);
        _LineChart.cx = cx;
        _LineChart.cy = cy;
        _LineChart.postInvalidate();
    }


    /**
     * 应用于平滑的缩放
     *
     * @param level
     * @param startW
     * @param startH
     * @param cx
     * @param cy
     */
    private void zoom(float level, double startW, double startH, float cx, float cy) {

        if (!canX_zoom) {
            startW = _MappingManager.get_currentViewPort().width();
        }
        if (!canY_zoom) {
            startH = _MappingManager.get_currentViewPort().height();
        }

        _MappingManager.zoom(level, startW, startH, cx, cy);
        _LineChart.cx = cx;
        _LineChart.cy = cy;
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


    ///////////////////////////////////  平滑的缩放  ///////////////////////////////////
    double _zoom_w, _zoom_h;
    float _zoom_cx, _zoom_cy;


    class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDoubleTap(MotionEvent e) {

            _zoom_w = _MappingManager.get_currentViewPort().width();
            _zoom_h = _MappingManager.get_currentViewPort().height();

            _zoom_cx = e.getX();
            _zoom_cy = e.getY();

            _Zoomer.startZoom(0.5f);

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
