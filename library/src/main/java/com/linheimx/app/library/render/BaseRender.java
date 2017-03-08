package com.linheimx.app.library.render;

import android.graphics.RectF;

import com.linheimx.app.library.manager.MappingManager;

/**
 * Created by lijian on 2016/11/14.
 */

public abstract class BaseRender {
    RectF _rectMain;
    MappingManager _MappingManager;

    public BaseRender(RectF _rectMain, MappingManager _MappingManager) {
        this._rectMain = _rectMain;
        this._MappingManager = _MappingManager;
    }
}
