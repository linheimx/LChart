package com.linheimx.app.library.manager;

import android.graphics.Matrix;

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

    public void plusOff() {
        _matrixOff.reset();

        _matrixOff.postTranslate(_viewPortManager.offsetLeft(),
                _viewPortManager.getChartHeight() - _viewPortManager.offsetBottom());
    }

    public void scaleK(double xMin, double yMin, double xDelta, double yDelta) {
        float scaleX = (float) ((_viewPortManager.contentWidth()) / xDelta);
        float scaleY = (float) ((_viewPortManager.contentHeight()) / yDelta);

        _matrixK.reset();
        _matrixK.postTranslate((float)-xMin, (float)-yMin);
        _matrixK.postScale(scaleX, -scaleY);
    }

}
