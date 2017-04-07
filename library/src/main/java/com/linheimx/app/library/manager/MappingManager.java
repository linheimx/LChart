package com.linheimx.app.library.manager;

import android.graphics.RectF;

import com.linheimx.app.library.data.Entry;
import com.linheimx.app.library.utils.RectD;
import com.linheimx.app.library.utils.SingleD_XY;
import com.linheimx.app.library.utils.SingleF_XY;

/**
 * 数据源与绘出来的图之间的映射关系
 * -------------------
 * 人与照片直接的映射关系
 * <p>
 * Created by lijian on 2016/11/13.
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


    /**
     * 数据视图的约束
     */
    float fatFactor = 1.3f;//肥胖因子
    RectD _constrainViewPort;

    public MappingManager(RectF rectMain) {
        _contentRect = rectMain;
        _maxViewPort = new RectD();
        _currentViewPort = new RectD();
        _constrainViewPort = new RectD();
    }

    public void prepareRelation(double xMin, double xMax, double yMin, double yMax) {

        _maxViewPort.left = xMin;
        _maxViewPort.right = xMax;
        _maxViewPort.bottom = yMin;
        _maxViewPort.top = yMax;

        _currentViewPort.setRectD(_maxViewPort);

        setFatFactor(fatFactor);
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

    /**
     * x方向
     * ---------------
     * 数值转换成像素
     *
     * @param xValue
     * @return
     */
    public float v2p_x(double xValue) {
        double px = _contentRect.left + _contentRect.width() * (xValue - _currentViewPort.left) / _currentViewPort.width();
        return (float) px;
    }

    /**
     * y方向
     * ----------------
     * 数值转换成像素
     *
     * @param yValue
     * @return
     */
    public float v2p_y(double yValue) {
        double py = _contentRect.top + _contentRect.height() - _contentRect.height() * (yValue - _currentViewPort.bottom) / _currentViewPort.height();
        return (float) py;
    }

    /**
     * x方向
     * ---------------
     * 像素转换成数值
     *
     * @param xPix
     * @return
     */
    public double p2v_x(float xPix) {
        double value = xPix - _contentRect.left;
        value = value / _contentRect.width() * _currentViewPort.width();
        value = value + _currentViewPort.left;
        return value;
    }

    /**
     * y方向
     * --------------
     * 像素转换成数值
     *
     * @param yPix
     * @return
     */
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

        constrainViewPort(_currentViewPort);
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

        constrainViewPort(_currentViewPort);
    }

    /**
     * 根据移动的像素偏离---》计算出数据视图的偏离
     *
     * @param dx
     * @param dy
     */
    public void translate(float dx, float dy) {

        double w = _currentViewPort.width();
        double h = _currentViewPort.height();

        double ddx = w * dx / _contentRect.width();
        double ddy = h * dy / _contentRect.height();

        _currentViewPort.left += -ddx;
        _currentViewPort.bottom += ddy;

        // 约束 currentViewPort 的位置
        constrainViewPort(_currentViewPort, w, h);

        _currentViewPort.right = _currentViewPort.left + w;
        _currentViewPort.top = _currentViewPort.bottom + h;
    }

    /**
     * 约束当前的viewport
     * ---------------------
     * 针对平移的方式
     */
    private void constrainViewPort(RectD currentViewPort, double currentWidth, double currentHeight) {

        currentViewPort.left = Math.max(_constrainViewPort.left, Math.min(_constrainViewPort.right - currentWidth, currentViewPort.left));
        currentViewPort.bottom = Math.max(_constrainViewPort.bottom, Math.min(_constrainViewPort.top - currentHeight, currentViewPort.bottom));
    }

    /**
     * 约束当前的viewport
     * ---------------------
     * 针对缩放的方式
     *
     * @param currentViewPort
     */
    private void constrainViewPort(RectD currentViewPort) {

        currentViewPort.left = Math.max(_constrainViewPort.left, currentViewPort.left);
        currentViewPort.right = Math.min(_constrainViewPort.right, currentViewPort.right);

        currentViewPort.bottom = Math.max(_constrainViewPort.bottom, currentViewPort.bottom);
        currentViewPort.top = Math.min(_constrainViewPort.top, currentViewPort.top);
    }

    public RectD get_constrainViewPort() {
        return _constrainViewPort;
    }

    public void set_constrainViewPort(RectD _constrainViewPort) {
        this._constrainViewPort = _constrainViewPort;
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

    public float getFatFactor() {
        return fatFactor;
    }

    /**
     * 设置约束视图的肥胖因子
     * -------------------
     * 和最大的数据视图做比较:
     * 1. 约束视图 是 最大视图的 1.1 倍，那么这个肥胖因子就是 1.1
     *
     * @param fatFactor
     */
    public void setFatFactor(float fatFactor) {

        if (fatFactor < 1) {
            throw new RuntimeException("肥胖因子必须大于1！");
        }

        this.fatFactor = fatFactor;

        double w = _maxViewPort.width();
        double h = _maxViewPort.height();

        fatFactor = fatFactor - 1;

        _constrainViewPort.left = _maxViewPort.left - w * fatFactor;
        _constrainViewPort.right = _maxViewPort.right + w * fatFactor;
        _constrainViewPort.top = _maxViewPort.top + h * fatFactor;
        _constrainViewPort.bottom = _maxViewPort.bottom - h * fatFactor;
    }

}
