package com.linheimx.app.library.render;

import android.graphics.Canvas;
import android.graphics.Color;
import android.text.TextUtils;

import com.linheimx.app.library.adapter.DefaultValueAdapter;
import com.linheimx.app.library.adapter.IValueAdapter;
import com.linheimx.app.library.manager.TransformManager;
import com.linheimx.app.library.manager.ViewPortManager;
import com.linheimx.app.library.parts.Axis;
import com.linheimx.app.library.utils.Utils;

/**
 * Created by LJIAN on 2016/11/14.
 */

public abstract class AxisRender extends BaseRender {

    Axis _Axis;


    public AxisRender(ViewPortManager _ViewPortManager, TransformManager _TransformManager, Axis axis) {
        super(_ViewPortManager, _TransformManager);

        this._Axis = axis;
    }

    public abstract void renderAxisLine(Canvas canvas);

    public abstract void renderLabels(Canvas canvas);

    public abstract void renderGridline(Canvas canvas);

    public abstract void renderUnit(Canvas canvas);

}
