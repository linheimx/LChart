package com.linheimx.app.library.render;

import com.linheimx.app.library.manager.TransformManager;
import com.linheimx.app.library.manager.ViewPortManager;

/**
 * Created by LJIAN on 2016/11/14.
 */

public abstract class BaseRender implements IRender {
    ViewPortManager _ViewPortManager;
    TransformManager _TransformManager;

    public BaseRender(ViewPortManager _ViewPortManager, TransformManager _TransformManager) {
        this._ViewPortManager = _ViewPortManager;
        this._TransformManager = _TransformManager;
    }
}
