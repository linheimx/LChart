package com.linheimx.app.library.manager;

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

    FrameManager _frameManager;

    public MappingManager(FrameManager frameManager) {
        _frameManager = frameManager;
    }

    float _xMin, _yMin;
    float _baseKx, _baseKy;

    float _touchKx, _touchKy;
    float _touchDx, _touchDy;

    public void prepareRelation(float xMin, float xMax, float yMin, float yMax) {

        float xRange = xMax - xMin;
        float yRange = yMax - yMin;

        _xMin = xMin;
        _yMin = yMin;
        _baseKx = (_frameManager.frameWidth()) / xRange;
        _baseKy = (_frameManager.frameHeight()) / yRange;

        _touchKx = _touchKy = 1;
        _touchDx = _touchDy = 0;
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
        float px = (xValue - _xMin) * _baseKx * _touchKx + _frameManager.offsetLeft();
        px += _touchDx;
        return px;
    }

    public float v2p_y(float yValue) {
        float py = _frameManager.frameHeight() - (yValue - _yMin) * _baseKy * _touchKy;
        py += _touchDy;
        return py;
    }

    public float p2v_x(float xPix) {
        xPix -= _touchDx;
        float px = _xMin + (xPix - _frameManager.offsetLeft()) / _baseKx / _touchKx;
        return px;
    }

    public float p2v_y(float yPix) {
        yPix -= _touchDy;
        float py = _yMin + (_frameManager.frameHeight() - yPix) / _baseKy / _touchKy;
        return py;
    }


    public void zoom(float scaleX, float scaleY, float cx, float cy) {

        _touchKx = _touchKx * scaleX;
        _touchKy = _touchKy * scaleY;

        float dx = -(cx * scaleX - cx);
        _touchDx += dx;

        float dy = (cy * scaleY - cy);
        _touchDy += dy;
    }

    public void translate(float dx, float dy) {
        _touchDx += dx;
        _touchDy += dy;
    }
}
