package com.linheimx.app.library.render;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.linheimx.app.library.model.HightLight;
import com.linheimx.app.library.data.Entry;
import com.linheimx.app.library.data.Line;
import com.linheimx.app.library.data.Lines;
import com.linheimx.app.library.manager.MappingManager;
import com.linheimx.app.library.utils.LogUtil;
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

    int recordIndex = -1;
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

        if (!_hightLight.isEnable()) {
            return;
        }

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

        double closeX = Float.MAX_VALUE;
        double closeY = Float.MAX_VALUE;
        Entry hitEntry = null;

        if (isClick) {
            // 点击光标进来的
            for (Line line : _lines.getLines()) {
                int index = Line.getEntryIndex(line.getEntries(), highValueX, Line.Rounding.CLOSEST);

                if (index < 0 || index >= line.getEntries().size()) {
                    continue;
                }

                Entry entry = line.getEntries().get(index);

                // 到点击的距离
                double dx = Math.abs(entry.getX() - highValueX);
                double dy = Math.abs(entry.getY() - hightValueY);

                // 先考虑 x
                if (dx <= closeX) {
                    closeX = dx;
                    // 再考虑 y
                    if (dy <= closeY) {
                        closeY = dy;
                        hitEntry = entry;
                        recordIndex = index;
                    }
                }
            }

            isClick = false;
        } else {
            // 左右移动进来的
            for (Line line : _lines.getLines()) {
                int index = recordIndex;

                if (index < 0 || index >= line.getEntries().size()) {
                    continue;
                }

                Entry entry = line.getEntries().get(index);

                // 到点击的距离
                double dx = Math.abs(entry.getX() - highValueX);
                double dy = Math.abs(entry.getY() - hightValueY);

                // 先考虑 x
                if (dx <= closeX) {
                    closeX = dx;
                    // 再考虑 y
                    if (dy <= closeY) {
                        closeY = dy;
                        hitEntry = entry;
                    }
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

        String xStr = _hightLight.getxValueAdapter().value2String(hitEntry.getX());
        String yStr = _hightLight.getyValueAdapter().value2String(hitEntry.getY());
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
