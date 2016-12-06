package com.linheimx.app.library.render;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.linheimx.app.library.manager.TransformManager;
import com.linheimx.app.library.manager.ViewPortManager;
import com.linheimx.app.library.utils.Utils;

/**
 * Created by LJIAN on 2016/11/14.
 */

public class NoDataRender extends BaseRender {

    Paint _Paint;
    String txt = "无数据";
    int txtColor = Color.RED;
    int txtSize = 30;

    public NoDataRender(ViewPortManager _ViewPortManager, TransformManager _TransformManager) {
        super(_ViewPortManager, _TransformManager);

        _Paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }
    
    public void render(Canvas canvas) {

        _Paint.setColor(txtColor);
        _Paint.setTextSize(txtSize);

        float x = _ViewPortManager.getContentCenter().getX();
        float y = _ViewPortManager.getContentCenter().getY();

        float halfW = Utils.textWidth(_Paint, txt) / 2;
        float halfH = Utils.textHeightAsc(_Paint) / 2;

        canvas.drawText(txt, x - halfW, y + halfH, _Paint);
    }
}
