package com.linheimx.app.library.parts;

import com.linheimx.app.library.manager.TransformManager;
import com.linheimx.app.library.manager.ViewPortManager;
import com.linheimx.app.library.utils.U_XY;

/**
 * Created by LJIAN on 2016/11/14.
 */

public class YAxis extends Axis {

    public YAxis(ViewPortManager _viewPortManager, TransformManager _transformManager) {
        super(_viewPortManager, _transformManager);
    }

    @Override
    public float getVisiableMin() {
        float py = _ViewPortManager.contentBottom();
        U_XY xy = _TransformManager.getValueByPx(0, py);
        return xy.getY();
    }

    @Override
    public float getVisiableMax() {
        float py = _ViewPortManager.contentTop();
        U_XY xy = _TransformManager.getValueByPx(0, py);
        return xy.getY();
    }
}
