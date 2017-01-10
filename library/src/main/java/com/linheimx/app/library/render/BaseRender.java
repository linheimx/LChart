package com.linheimx.app.library.render;

import com.linheimx.app.library.manager.TransformManager;
import com.linheimx.app.library.manager.FrameManager;

/**
 * Created by LJIAN on 2016/11/14.
 */

public abstract class BaseRender {
    FrameManager _FrameManager;
    TransformManager _TransformManager;

    public BaseRender(FrameManager _FrameManager, TransformManager _TransformManager) {
        this._FrameManager = _FrameManager;
        this._TransformManager = _TransformManager;
    }

}
