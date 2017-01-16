package com.linheimx.app.library.render;

import com.linheimx.app.library.manager.MappingManager;
import com.linheimx.app.library.manager.FrameManager;

/**
 * Created by LJIAN on 2016/11/14.
 */

public abstract class BaseRender {
    FrameManager _FrameManager;
    MappingManager _MappingManager;

    public BaseRender(FrameManager _FrameManager, MappingManager _MappingManager) {
        this._FrameManager = _FrameManager;
        this._MappingManager = _MappingManager;
    }

}
