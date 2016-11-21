package com.linheimx.app.library.manager;

import android.graphics.Matrix;

import com.linheimx.app.library.data.Entry;
import com.linheimx.app.library.parts.XAxis;
import com.linheimx.app.library.parts.YAxis;
import com.linheimx.app.library.utils.LogUtil;
import com.linheimx.app.library.utils.Single_XY;

/**
 * Created by Administrator on 2016/11/13.
 */

public class TransformManager {
    /**
     * matrix 要严格遵从运算顺序
     */
    Matrix _matrixOff = new Matrix();//偏离
    Matrix _matrixK = new Matrix();//比例系数
    Matrix _matrixTouch = new Matrix();

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
        float kx = (_viewPortManager.contentWidth()) / xDelta;
        float ky = (_viewPortManager.contentHeight()) / yDelta;

        _matrixK.reset();
        _matrixK.postTranslate(-xMin, -yMin);
        _matrixK.postScale(kx, ky);
        _matrixK.postScale(1, -1);
    }

    public void prepareRelation(XAxis xAxis, YAxis yAxis) {
        k(xAxis.get_min(), xAxis.get_range(), yAxis.get_min(), yAxis.get_range());
        off();
    }


    float[] xyBuffer = new float[2];

    /**
     * 根据像素位置变换成数值
     *
     * @param x
     * @param y
     * @return
     */
    public Single_XY getValueByPx(float x, float y) {

        xyBuffer[0] = x;
        xyBuffer[1] = y;

        px2Value(xyBuffer);

        Single_XY value = Single_XY.getInstance();
        value.setX(xyBuffer[0]).setY(xyBuffer[1]);
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

        xyBuffer[0] = x;
        xyBuffer[1] = y;

        value2Px(xyBuffer);

        Single_XY value = Single_XY.getInstance();
        value.setX(xyBuffer[0]).setY(xyBuffer[1]);
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

        _matrixTouch.invert(_bufferPV);
        _bufferPV.mapPoints(pxs);

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
        //----------------------------> 注意运算顺序（先乘除，再加减） <------------------------
        _matrixK.mapPoints(values);
        _matrixOff.mapPoints(values);
        _matrixTouch.mapPoints(values);
    }


    Matrix _matrixTmp = new Matrix();

    /**
     * @param scaleX
     * @param scaleY
     * @param cx
     * @param cy
     * @return true:刷新 false:不刷新
     */
    public boolean zoom(float scaleX, float scaleY, float cx, float cy) {

        _matrixTmp.reset();
        _matrixTmp.set(_matrixTouch);
        _matrixTmp.postScale(scaleX, scaleY, cx, cy);
        _matrixTmp.getValues(_mvBuffer);

        float sx = _mvBuffer[Matrix.MSCALE_X];
        float sy = _mvBuffer[Matrix.MSCALE_Y];

        float frame_width = _viewPortManager.contentWidth();
        float frame_height = _viewPortManager.contentHeight();

        float pic_width = frame_width * sx;
        float pic_height = frame_height * sy;

        if (pic_width < frame_width && pic_height < frame_height) {
            return false;
        } else {
            _matrixTouch.postScale(scaleX, scaleY, cx, cy);
            return true;
        }
    }

    public void translate(float dx, float dy) {
        _matrixTouch.postTranslate(dx, dy);
    }


    float[] _mvBuffer = new float[9];

    /**
     * 检查平移与缩放的范围
     */
    private void checkRange(Matrix touch) {

        touch.getValues(_mvBuffer);

        float tx = _mvBuffer[Matrix.MTRANS_X];
        float sx = _mvBuffer[Matrix.MSCALE_X];
        float ty = _mvBuffer[Matrix.MTRANS_Y];
        float sy = _mvBuffer[Matrix.MSCALE_Y];

        float frame_width = _viewPortManager.contentWidth();
        float frame_height = _viewPortManager.contentHeight();

        float pic_width = frame_width * sx;
        float pic_height = frame_height * sy;

        if (pic_width < frame_width) {
            sx = 1;
        }
        if (pic_height < frame_height) {
            sy = 1;
        }

        LogUtil.e("---->" + tx + "    " + ty);

        _mvBuffer[Matrix.MTRANS_X] = tx;
        _mvBuffer[Matrix.MSCALE_X] = sx;
        _mvBuffer[Matrix.MTRANS_Y] = ty;
        _mvBuffer[Matrix.MSCALE_Y] = sy;

        _matrixTouch.setValues(_mvBuffer);
    }

}
