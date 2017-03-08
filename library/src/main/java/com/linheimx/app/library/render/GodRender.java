package com.linheimx.app.library.render;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.linheimx.app.library.manager.MappingManager;
import com.linheimx.app.library.utils.Utils;

/**
 * Created by lijian on 2017/1/30.
 */

public class GodRender extends BaseRender {

    Paint _PaintRect;
    RectF _GodRect = new RectF(_rectMain);

    public GodRender(RectF _rectMain, MappingManager _MappingManager, RectF godRect) {
        super(_rectMain, _MappingManager);

        this._GodRect = godRect;

        _PaintRect = new Paint(Paint.ANTI_ALIAS_FLAG);
        _PaintRect.setColor(Color.RED);
        _PaintRect.setStrokeWidth(Utils.dp2px(5));
        _PaintRect.setStyle(Paint.Style.STROKE);
    }


    public void render(Canvas canvas) {
        canvas.drawRect(_GodRect, _PaintRect);
    }


}
