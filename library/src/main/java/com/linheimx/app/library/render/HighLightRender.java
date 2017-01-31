package com.linheimx.app.library.render;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.linheimx.app.library.model.HightLight;
import com.linheimx.app.library.data.Entry;
import com.linheimx.app.library.data.Line;
import com.linheimx.app.library.data.Lines;
import com.linheimx.app.library.manager.MappingManager;
import com.linheimx.app.library.utils.SingleF_XY;
import com.linheimx.app.library.utils.Utils;

/**
 * Created by LJIAN on 2016/12/15.
 */

public class HighLightRender extends BaseRender {

    Lines _lines;
    HightLight _hightLight;

    Paint paintHighLight;
    Paint paintHint;

    public HighLightRender(RectF rectMain, MappingManager _MappingManager, Lines lines, HightLight hightLight) {
        super(rectMain, _MappingManager);

        this._lines = lines;
        this._hightLight = hightLight;

        paintHighLight = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintHint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }


    double highValueX = Double.MIN_VALUE;
    double hightValueY = Double.MIN_VALUE;

    int recordIndex = 0;
    boolean isClick = false;

    public void highLight_ValueXY(double x, double y) {
        highValueX = x;
        hightValueY = y;
        isClick = true;
    }

    public void highLightLeft() {
        recordIndex--;
    }

    public void highLightRight() {
        recordIndex++;
    }


    public void render(Canvas canvas) {

        if (_lines == null) {
            return;
        }

        if (_lines.getLines().size() == 0) {
            return;
        }

        if (highValueX == Double.MIN_VALUE || hightValueY == Double.MIN_VALUE) {
            return;
        }

        canvas.save();
        canvas.clipRect(_rectMain);
        drawHighLight_Hint(canvas);
        canvas.restore();
    }


    private void drawHighLight_Hint(Canvas canvas) {

        double disX = Float.MAX_VALUE;
        double disY = Float.MAX_VALUE;
        Entry hitEntry = null;

        for (Line line : _lines.getLines()) {
            int index = recordIndex;

            if (isClick) {
                index = Line.getEntryIndex(line.getEntries(), highValueX, Line.Rounding.CLOSEST);
                recordIndex = index;
                isClick = false;
            }

            if (index < 0 || index >= line.getEntries().size()) {
                continue;
            }

            Entry entry = line.getEntries().get(index);

            double dx = Math.abs(entry.getX() - highValueX);

            double dy = 0;
            if (hightValueY != Float.MIN_VALUE) {
                dy = Math.abs(entry.getY() - hightValueY);
            }


            // 先考虑 x
            if (dx <= disX) {
                disX = dx;

                // 再考虑 y
                if (hightValueY != Float.MIN_VALUE) {
                    if (dy <= disY) {
                        hitEntry = entry;
                    }
                } else {
                    hitEntry = entry;
                }
            }
        }

        if (hitEntry == null) {
            return;
        }
        
        highValueX = hitEntry.getX();// real indexX

        SingleF_XY xy = _MappingManager.getPxByEntry(hitEntry);


        // draw high light
        paintHighLight.setStrokeWidth(_hightLight.getHighLightWidth());
        paintHighLight.setColor(_hightLight.getHighLightColor());

        canvas.drawLine(_rectMain.left, xy.getY(), _rectMain.right, xy.getY(), paintHighLight);
        canvas.drawLine(xy.getX(), _rectMain.top, xy.getX(), _rectMain.bottom, paintHighLight);


        // draw hint
        paintHint.setColor(_hightLight.getHintColor());
        paintHint.setTextSize(_hightLight.getHintTextSize());

        String xStr = "X: " + hitEntry.getX();
        String yStr = "Y: " + hitEntry.getY();
        float txtHeight = Utils.textHeight(paintHint);
        float txtWidth = Math.max(Utils.textWidth(paintHint, xStr), Utils.textWidth(paintHint, yStr));

        float x = _rectMain.right - txtWidth - 10;
        float y = _rectMain.top + Utils.dp2px(20);

        canvas.drawText(xStr, x, y, paintHint);
        canvas.drawText(yStr, x, y + txtHeight, paintHint);
    }

    public void onDataChanged(Lines lines) {
        _lines = lines;
    }
}
