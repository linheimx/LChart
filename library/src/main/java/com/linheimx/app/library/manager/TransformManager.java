package com.linheimx.app.library.manager;

import android.graphics.Matrix;

import com.linheimx.app.library.parts.XAxis;
import com.linheimx.app.library.parts.YAxis;
import com.linheimx.app.library.utils.U_XY;

/**
 * Created by Administrator on 2016/11/13.
 */

public class TransformManager {

    Matrix _matrixOff = new Matrix();//偏离
    Matrix _matrixK = new Matrix();//比例系数

    ViewPortManager _viewPortManager;

    public TransformManager(ViewPortManager viewPortManager) {
        _viewPortManager = viewPortManager;
    }

    /**
     * 处理偏离
     */
    private void off() {
        _matrixOff.reset();
        _matrixOff.postTranslate(_viewPortManager.offsetLeft(),
                _viewPortManager.getChartHeight() - _viewPortManager.offsetBottom());
    }

    /**
     * 处理比例
     *
     * @param xMin
     * @param xDelta
     * @param yMin
     * @param yDelta
     */
    private void k(float xMin, float xDelta, float yMin, float yDelta) {
        float scaleX = (_viewPortManager.contentWidth()) / xDelta;
        float scaleY = (_viewPortManager.contentHeight()) / yDelta;

        _matrixK.reset();
        _matrixK.postTranslate(-xMin, -yMin);
        _matrixK.postScale(scaleX, -scaleY);
    }

    public void prepareRelation(XAxis xAxis, YAxis yAxis) {
        k(xAxis.get_min(), xAxis.get_range(), yAxis.get_min(), yAxis.get_range());
        off();
    }


    float[] pxs = new float[2];

    /**
     * 根据像素位置变换成数值
     *
     * @param x
     * @param y
     * @return
     */
    public U_XY getValueByPx(float x, float y) {

        pxs[0] = x;
        pxs[1] = y;

        px2Value(pxs);

        U_XY value = U_XY.getInstance();
        value.setX(pxs[0]).setY(pxs[1]);
        return value;
    }


    /**
     * 根据数值变换成像素
     *
     * @param x
     * @param y
     * @return
     */
    public U_XY getPxByValue(float x, float y) {

        pxs[0] = x;
        pxs[1] = y;

        value2Px(pxs);

        U_XY value = U_XY.getInstance();
        value.setX(pxs[0]).setY(pxs[1]);
        return value;
    }


    Matrix _bufferPV = new Matrix();

    /**
     * 将屏幕上的位置(x,y,x,y,...) 变换成chart上的数值。
     *
     * @param pxs
     */
    public void px2Value(float[] pxs) {
        _bufferPV.reset();

        _matrixOff.invert(_bufferPV);
        _bufferPV.mapPoints(pxs);

        _matrixK.invert(_bufferPV);
        _bufferPV.mapPoints(pxs);
    }

    /**
     * 将chart上的数值 变换成屏幕上的位置(x,y,x,y,...)
     *
     * @param values
     */
    public void value2Px(float[] values) {
        _matrixK.mapPoints(values);
        _matrixOff.mapPoints(values);
    }

}
