package com.linheimx.app.library.manager;

import android.graphics.RectF;

import com.linheimx.app.library.data.Entry;
import com.linheimx.app.library.utils.RectD;
import com.linheimx.app.library.utils.SingleF_XY;
import com.linheimx.app.library.utils.SingleD_XY;

/**
 * 数据源与绘出来的图之间的映射关系
 * -------------------
 * 人与照片直接的映射关系
 * <p>
 * Created by Administrator on 2016/11/13.
 */

public class MappingManager {

    /**
     * 区域视图
     */
    RectF _contentRect;

    /**
     * 数据视图
     */
    RectD _maxViewPort;
    RectD _currentViewPort;

    public MappingManager(RectF rectMain) {
        _contentRect = rectMain;
        _maxViewPort = new RectD();
        _currentViewPort = new RectD();
    }

    public void prepareRelation(double xMin, double xMax, double yMin, double yMax) {

        _maxViewPort.left = xMin;
        _maxViewPort.right = xMax;
        _maxViewPort.bottom = yMin;
        _maxViewPort.top = yMax;

        _currentViewPort.setRectD(_maxViewPort);
    }

    /**
     * 根据像素位置变换成数值
     *
     * @param x
     * @param y
     * @return
     */
    public SingleD_XY getValueByPx(float x, float y) {
        SingleD_XY value = SingleD_XY.getInstance();
        value.setX(p2v_x(x)).setY(p2v_y(y));
        return value;
    }

    public SingleF_XY getPxByEntry(Entry entry) {
        return getPxByValue(entry.getX(), entry.getY());
    }

    /**
     * 根据数值变换成像素
     *
     * @param x
     * @param y
     * @return
     */
    public SingleF_XY getPxByValue(double x, double y) {

        SingleF_XY value = SingleF_XY.getInstance();
        value.setX(v2p_x(x)).setY(v2p_y(y));
        return value;
    }

    public float v2p_x(double xValue) {
        double px = _contentRect.left + _contentRect.width() * (xValue - _currentViewPort.left) / _currentViewPort.width();
        return (float) px;
    }

    public float v2p_y(double yValue) {
        double py = _contentRect.top + _contentRect.height() - _contentRect.height() * (yValue - _currentViewPort.bottom) / _currentViewPort.height();
        return (float) py;
    }

    public double p2v_x(float xPix) {
        double value = xPix - _contentRect.left;
        value = value / _contentRect.width() * _currentViewPort.width();
        value = value + _currentViewPort.left;
        return value;
    }

    public double p2v_y(float yPix) {
        double value = yPix - (_contentRect.top + _contentRect.height());
        value = value / -_contentRect.height() * _currentViewPort.height();
        value = value + _currentViewPort.bottom;
        return value;
    }


    public void zoom(float scaleX, float scaleY, float cx, float cy) {

        double newWidth = scaleX * _currentViewPort.width();
        double newHeight = scaleY * _currentViewPort.height();

        double hitValueX = p2v_x(cx);
        double left = hitValueX - newWidth * (cx - _contentRect.left) / _contentRect.width();

        double hitValueY = p2v_y(cy);
        double bottom = hitValueY - newHeight * (_contentRect.bottom - cy) / _contentRect.height();

        _currentViewPort.left = left;
        _currentViewPort.bottom = bottom;
        _currentViewPort.right = _currentViewPort.left + newWidth;
        _currentViewPort.top = _currentViewPort.bottom + newHeight;
    }

    public void zoom(float level, double startW, double startH, float cx, float cy) {

        double newWidth = startW * level;
        double newHeight = startH * level;

        double hitValueX = p2v_x(cx);
        double left = hitValueX - newWidth * (cx - _contentRect.left) / _contentRect.width();

        double hitValueY = p2v_y(cy);
        double bottom = hitValueY - newHeight * (_contentRect.bottom - cy) / _contentRect.height();

        _currentViewPort.left = left;
        _currentViewPort.bottom = bottom;
        _currentViewPort.right = _currentViewPort.left + newWidth;
        _currentViewPort.top = _currentViewPort.bottom + newHeight;
    }

    public void translate(float dx, float dy) {
        double ddx = _currentViewPort.width() * dx / _contentRect.width();
        double ddy = _currentViewPort.height() * dy / _contentRect.height();

        double w = _currentViewPort.width();
        double h = _currentViewPort.height();

        _currentViewPort.left += -ddx;
        _currentViewPort.bottom += ddy;
        _currentViewPort.right = _currentViewPort.left + w;
        _currentViewPort.top = _currentViewPort.bottom + h;
    }


    public RectD get_maxViewPort() {
        return _maxViewPort;
    }

    public void set_maxViewPort(RectD _maxViewPort) {
        this._maxViewPort = _maxViewPort;
    }

    public RectD get_currentViewPort() {
        return _currentViewPort;
    }

    public void set_currentViewPort(RectD _currentViewPort) {
        this._currentViewPort = _currentViewPort;
    }
}
