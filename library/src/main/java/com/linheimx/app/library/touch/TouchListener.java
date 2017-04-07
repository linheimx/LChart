package com.linheimx.app.library.touch;

import android.support.annotation.Nullable;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewParent;
import android.widget.Scroller;

import com.linheimx.app.library.charts.LineChart;
import com.linheimx.app.library.listener.IDragListener;
import com.linheimx.app.library.manager.MappingManager;
import com.linheimx.app.library.utils.LogUtil;
import com.linheimx.app.library.utils.RectD;

/**
 * Created by lijian on 2016/11/20.
 */

public class TouchListener implements View.OnTouchListener {

    ViewParent viewParent;

    GestureDetector _GestureDetector;
    Zoomer _Zoomer;
    Scroller _Scroller;

    LineChart _LineChart;
    MappingManager _MappingManager;

    VelocityTracker _VelocityTracker;
    TouchMode _TouchMode = TouchMode.NONE;

    ///////////////////////////////////  平滑的缩放  ///////////////////////////////////
    double _zoom_w, _zoom_h;
    float _zoom_cx, _zoom_cy;

    public TouchListener(LineChart lineChart) {
        this._LineChart = lineChart;

        _GestureDetector = new GestureDetector(_LineChart.getContext(), new GestureListener());
        _MappingManager = lineChart.get_MappingManager();

        _Zoomer = new Zoomer();
        _Scroller = new Scroller(lineChart.getContext());
    }


    float _lastX, _lastY;
    float _disX = 1, _disY = 1, _disXY = 1, _cX, _cY;

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        viewParent = v.getParent();

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
                stopAll();
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

                    _TouchMode = TouchMode.PINCH_ZOOM;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = x - _lastX;
                float dy = y - _lastY;

                if (_TouchMode == TouchMode.DRAG) {
                    _VelocityTracker.computeCurrentVelocity(1000);
                    float vx = _VelocityTracker.getXVelocity();// 得到当前手指在这一点移动的速度
                    float vy = _VelocityTracker.getYVelocity();// 速度分为了x和y轴两个方向的速度
                    int direction = judgeDir(vx, vy);

                    doDrag(dx, dy, direction);
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
                if (_TouchMode == TouchMode.DRAG) {
                    _TouchMode = TouchMode.FLING;

                    _VelocityTracker.computeCurrentVelocity(1000, 5000);
                    int vx = (int) _VelocityTracker.getXVelocity();
                    int vy = (int) _VelocityTracker.getYVelocity();
                    if (vx > 20 || vy > 20) {
                        doFling(event.getX(), event.getY(), vx, vy);
                    }

                    IDragListener listener = _LineChart.get_dragListener();
                    if (listener != null) {
                        listener.onDrag(_LineChart.getVisiableMinX(), _LineChart.getVisiableMaxX());
                    }
                }
                _TouchMode = TouchMode.NONE;
                break;
            case MotionEvent.ACTION_CANCEL:
                _TouchMode = TouchMode.NONE;
                break;
        }

        return true;
    }

    float _lastScrollX, _lastScrollY;

    public void computeScroll() {
        // 考虑滚动
        if (_Scroller.computeScrollOffset()) {

            int currX = _Scroller.getCurrX();
            int currY = _Scroller.getCurrY();

            float dx = currX - _lastScrollX;
            float dy = currY - _lastScrollY;

            doDrag(dx, dy, -1);

            _lastScrollX = currX;
            _lastScrollY = currY;
        }

        // 考虑缩放
        if (_Zoomer.computeZoom()) {

            float currentLevel = _Zoomer.getCurrentZoom();
            zoom(currentLevel, _zoom_w, _zoom_h, _zoom_cx, _zoom_cy);
        }
    }


    boolean hitStart = false;
    int hitCount = 0;

    /**
     * lchart 需要触摸事件
     */
    private void iNeedTouch(boolean need) {

        if (need) {
            viewParent.requestDisallowInterceptTouchEvent(true);
            hitStart = false;
        } else {
            hitCount++;

            if (!hitStart) {
                hitCount = 0;
                hitStart = true;
            } else {

                if (hitCount > 3) {
                    hitStart = false;
                    viewParent.requestDisallowInterceptTouchEvent(false);
                } else {
                    viewParent.requestDisallowInterceptTouchEvent(true);
                }
            }
        }

    }


    private void stopAll() {
        _Scroller.forceFinished(true);
        _Zoomer.stop();
    }


    private int judgeDir(float vx, float vy) {

        float x = Math.abs(vx);
        float y = Math.abs(vy);

        if (x > y) {
            // 方向是水平
            return 1;
        } else {
            // 方向是垂直
            return 2;
        }
    }

    /**
     * 双手缩放
     *
     * @param event
     */
    private void doPinch(MotionEvent event) {

        if (!_LineChart.isScaleable()) {
            return;
        }

        iNeedTouch(true);

        float absDist = getABSDist(event);
        float scale = _disXY / absDist;

        zoom(scale, scale, _cX, _cY);

        _disXY = absDist;
    }


    /**
     * 拖拽
     *
     * @param dx
     * @param dy
     */
    private void doDrag(float dx, float dy, int direction) {

        if (!_LineChart.isDragable()) {
            return;
        }

        if (direction != -1) {

            RectD current = _MappingManager.get_currentViewPort();
            RectD maxx = _MappingManager.get_constrainViewPort();

            boolean need = false;

            if (direction == 1) {
                // hor
                need = current.right < maxx.right;
                need &= current.left > maxx.left;

            } else {
                // ver
                need = current.bottom > maxx.bottom;
                need &= current.top < maxx.top;
            }

            iNeedTouch(need);
        }

        _MappingManager.translate(dx, dy);
        _LineChart.postInvalidate();
    }


    /**
     * 抛掷
     */
    private void doFling(float x, float y, int velocityX, int velocityY) {

        if (!_LineChart.isDragable()) {
            return;
        }

        _Scroller.forceFinished(true);

        _lastScrollX = x;
        _lastScrollY = y;

        _Scroller.fling(
                (int) x, (int) y,
                velocityX, velocityY,
                Integer.MIN_VALUE, Integer.MAX_VALUE,
                Integer.MIN_VALUE, Integer.MAX_VALUE);

        _LineChart.invalidate();
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

        if (!_LineChart.isScaleable()) {
            return;
        }

        if (!canX_zoom) {
            scaleX = 1;
        }
        if (!canY_zoom) {
            scaleY = 1;
        }

        _MappingManager.zoom(scaleX, scaleY, cx, cy);
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

        if (!_LineChart.isScaleable()) {
            return;
        }

        if (!canX_zoom) {
            startW = _MappingManager.get_currentViewPort().width();
        }
        if (!canY_zoom) {
            startH = _MappingManager.get_currentViewPort().height();
        }

        _MappingManager.zoom(level, startW, startH, cx, cy);
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

    public boolean isCanX_zoom() {
        return canX_zoom;
    }

    public void setCanX_zoom(boolean canX_zoom) {
        this.canX_zoom = canX_zoom;
    }

    public boolean isCanY_zoom() {
        return canY_zoom;
    }

    public void setCanY_zoom(boolean canY_zoom) {
        this.canY_zoom = canY_zoom;
    }

    class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDoubleTap(MotionEvent e) {

            _Zoomer.stop();

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
