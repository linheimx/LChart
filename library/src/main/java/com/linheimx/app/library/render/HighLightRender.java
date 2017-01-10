package com.linheimx.app.library.render;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.linheimx.app.library.dataprovider.HightLight;
import com.linheimx.app.library.data.Entry;
import com.linheimx.app.library.data.Line;
import com.linheimx.app.library.data.Lines;
import com.linheimx.app.library.manager.TransformManager;
import com.linheimx.app.library.manager.FrameManager;
import com.linheimx.app.library.utils.Single_XY;
import com.linheimx.app.library.utils.Utils;

/**
 * Created by LJIAN on 2016/12/15.
 */

public class HighLightRender extends BaseRender {

    Lines _lines;
    HightLight _hightLight;

    Paint paintHighLight;
    Paint paintHint;

    public HighLightRender(FrameManager _FrameManager, TransformManager _TransformManager, Lines lines, HightLight hightLight) {
        super(_FrameManager, _TransformManager);

        this._lines = lines;
        this._hightLight = hightLight;

        paintHighLight = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintHint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }


    float hightX = Float.MIN_VALUE;
    float hightY = Float.MIN_VALUE;

    public void highLight_ValueXY(float x, float y) {
        hightX = x;
        hightY = y;
    }

    public void highLightLeft() {
        hightX--;
    }

    public void highLightRight() {
        hightX++;
    }


    public void render(Canvas canvas) {

        if (_lines == null) {
            return;
        }

        canvas.save();
        canvas.clipRect(_FrameManager.getFrameRect());
        drawHighLight_Hint(canvas);
        canvas.restore();
    }


    private void drawHighLight_Hint(Canvas canvas) {

        // check
        if (hightX == Float.MIN_VALUE) {
            return;
        }

        if (_lines.getLines().size() == 0) {
            return;
        }

        float disX = Float.MAX_VALUE;
        float disY = Float.MAX_VALUE;
        Entry hitEntry = null;

        for (Line line : _lines.getLines()) {

            int index = Line.getEntryIndex(line.getEntries(), hightX, Line.Rounding.CLOSEST);
            Entry entry = line.getEntries().get(index);

            float dx = Math.abs(entry.getX() - hightX);

            float dy = 0;
            if (hightY != Float.MIN_VALUE) {
                dy = Math.abs(entry.getY() - hightY);
            }


            // 先考虑 x
            if (dx <= disX) {
                disX = dx;

                // 再考虑 y
                if (hightY != Float.MIN_VALUE) {
                    if (dy <= disY) {
                        hitEntry = entry;
                    }
                } else {
                    hitEntry = entry;
                }
            }
        }

        hightX = hitEntry.getX();// real indexX

        Single_XY xy = _TransformManager.getPxByEntry(hitEntry);


        // draw high light
        paintHighLight.setStrokeWidth(_hightLight.getHighLightWidth());
        paintHighLight.setColor(_hightLight.getHighLightColor());

        canvas.drawLine(_FrameManager.frameLeft(), xy.getY(), _FrameManager.frameRight(), xy.getY(), paintHighLight);
        canvas.drawLine(xy.getX(), _FrameManager.frameTop(), xy.getX(), _FrameManager.frameBottom(), paintHighLight);


        // draw hint
        paintHint.setColor(_hightLight.getHintColor());
        paintHint.setTextSize(_hightLight.getHintTextSize());

        String xStr = "X: " + hitEntry.getX();
        String yStr = "Y: " + hitEntry.getY();
        float txtHeight = Utils.textHeight(paintHint);
        float txtWidth = Math.max(Utils.textWidth(paintHint, xStr), Utils.textWidth(paintHint, yStr));

        float x = _FrameManager.frameRight() - txtWidth - 10;
        float y = _FrameManager.frameTop() + Utils.dp2px(20);

        canvas.drawText(xStr, x, y, paintHint);
        canvas.drawText(yStr, x, y + txtHeight, paintHint);
    }

    public void onDataChanged(Lines lines) {
        _lines = lines;
    }
}
