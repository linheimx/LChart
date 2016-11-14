package com.linheimx.app.library.parts;

import com.linheimx.app.library.manager.TransformManager;
import com.linheimx.app.library.manager.ViewPortManager;
import com.linheimx.app.library.utils.U_XY;

/**
 * Created by LJIAN on 2016/11/14.
 */

public class XAxis extends Axis {

    public XAxis(ViewPortManager _viewPortManager, TransformManager _transformManager) {
        super(_viewPortManager, _transformManager);
    }

    @Override
    public float getVisiableMin() {
        float px = _ViewPortManager.contentLeft();
        U_XY xy = _TransformManager.getValueByPx(px, 0);
        return xy.getX();
    }

    @Override
    public float getVisiableMax() {
        float px = _ViewPortManager.contentRight();
        U_XY xy = _TransformManager.getValueByPx(px, 0);
        return xy.getX();
    }


}
