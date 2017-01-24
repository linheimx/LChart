package com.linheimx.app.library.manager;

import android.graphics.RectF;

import com.linheimx.app.library.data.Entry;
import com.linheimx.app.library.utils.LogUtil;
import com.linheimx.app.library.utils.Single_XY;

/**
 * 数据源与绘出来的图之间的映射关系
 * -------------------
 * 人与照片直接的映射关系
 * <p>
 * Created by Administrator on 2016/11/13.
 */

public class MappingManager {

    RectF _contentRect;

    RectF _maxViewPort;
    RectF _currentViewPort;

    public MappingManager(RectF contentRect) {

        _contentRect = contentRect;
        _maxViewPort = new RectF();
        _currentViewPort = new RectF();
    }

    public void prepareRelation(float xMin, float xMax, float yMin, float yMax) {

        _maxViewPort.left = xMin;
        _maxViewPort.right = xMax;
        _maxViewPort.bottom = yMin;
        _maxViewPort.top = yMax;

        _currentViewPort.set(_maxViewPort);
    }

    /**
     * 根据像素位置变换成数值
     *
     * @param x
     * @param y
     * @return
     */
    public Single_XY getValueByPx(float x, float y) {

        Single_XY value = Single_XY.getInstance();
        value.setX(p2v_x(x)).setY(p2v_y(y));
        return value;
    }

    public Single_XY getPxByEntry(Entry entry) {
        return getPxByValue(entry.getX(), entry.getY());
    }

    /**
     * 根据数值变换成像素
     *
     * @param x
     * @param y
     * @return
     */
    public Single_XY getPxByValue(float x, float y) {

        Single_XY value = Single_XY.getInstance();
        value.setX(v2p_x(x)).setY(v2p_y(y));
        return value;
    }

    /**
     * 将chart上的数值 变换成屏幕上的位置(x,y,x,y,...)
     *
     * @param values
     */
    public void values2Px(float[] values) {

        for (int i = 0; i < values.length; i++) {
            if (i % 2 == 0) {
                // x
                values[i] = v2p_x(values[i]);
            } else {
                //y
                values[i] = v2p_y(values[i]);
            }
        }
    }

    public float v2p_x(float xValue) {
        float px = _contentRect.left + _contentRect.width() * (xValue - _currentViewPort.left) / Math.abs(_currentViewPort.width());
        return px;
    }

    public float v2p_y(float yValue) {
        float py = _contentRect.top + _contentRect.height() - _contentRect.height() * (yValue - _currentViewPort.bottom) / Math.abs(_currentViewPort.height());
        return py;
    }

    public float p2v_x(float xPix) {
        xPix -= _contentRect.left;
        xPix = xPix / _contentRect.width() * Math.abs(_currentViewPort.width());
        float value = xPix + _currentViewPort.left;
        return value;
    }

    public float p2v_y(float yPix) {
        yPix -= (_contentRect.top + _contentRect.height());
        yPix = yPix / -_contentRect.height() * Math.abs(_currentViewPort.height());
        float value = yPix + _currentViewPort.bottom;
        return value;
    }


    public void zoom(float scaleX, float scaleY, float cx, float cy) {

        float newWidth = scaleX * Math.abs(_currentViewPort.width());
        float newHeight = scaleY * Math.abs(_currentViewPort.height());

        float hitValueX = p2v_x(cx);
        float left = hitValueX - newWidth * (cx - _contentRect.left) / Math.abs(_contentRect.width());

        float hitValueY = p2v_y(cy);
        float bottom = hitValueY - newHeight * (_contentRect.bottom - cy) / Math.abs(_contentRect.height());

        _currentViewPort.left = left;
        _currentViewPort.bottom = bottom ;
        _currentViewPort.right = _currentViewPort.left + newWidth;
        _currentViewPort.top = _currentViewPort.bottom + newHeight;
    }

    public void translate(float dx, float dy) {
        float ddx = _currentViewPort.width() * dx / _contentRect.width();
        float ddy = _currentViewPort.height() * dy / _contentRect.height();

        float w = Math.abs(_currentViewPort.width());
        float h = Math.abs(_currentViewPort.height());

        _currentViewPort.left += -ddx;
        _currentViewPort.bottom += -ddy;
        _currentViewPort.right = _currentViewPort.left + w;
        _currentViewPort.top = _currentViewPort.bottom + h;
    }
}
