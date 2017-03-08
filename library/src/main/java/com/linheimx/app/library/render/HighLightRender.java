package com.linheimx.app.library.render;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.linheimx.app.library.model.HighLight;
import com.linheimx.app.library.data.Entry;
import com.linheimx.app.library.data.Line;
import com.linheimx.app.library.data.Lines;
import com.linheimx.app.library.manager.MappingManager;
import com.linheimx.app.library.utils.SingleF_XY;
import com.linheimx.app.library.utils.Utils;

/**
 * Created by lijian on 2016/12/15.
 */

public class HighLightRender extends BaseRender {

    private final static double UN_CHOOSE = Double.MIN_VALUE;

    Lines _lines;
    HighLight _highLight;

    Paint paintHighLight;
    Paint paintHint;

    public HighLightRender(RectF rectMain, MappingManager _MappingManager, Lines lines, HighLight highLight) {
        super(rectMain, _MappingManager);

        this._lines = lines;
        this._highLight = highLight;

        paintHighLight = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintHint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }


    double highValueX = UN_CHOOSE;
    double hightValueY = UN_CHOOSE;

    int record_LineIndex = -1;// 记录哪条线
    int record_EntryIndex = -1;// 记录哪条entry
    boolean isClick = false;

    public void highLight_ValueXY(double x, double y) {
        highValueX = x;
        hightValueY = y;
        isClick = true;
    }

    public void highLightLeft() {
        record_EntryIndex--;
    }

    public void highLightRight() {
        record_EntryIndex++;
    }


    public void render(Canvas canvas) {

        if (!_highLight.isEnable()) {
            return;
        }

        if (_lines == null) {
            return;
        }

        if (_lines.getLines().size() == 0) {
            return;
        }

        if (highValueX == UN_CHOOSE || hightValueY == UN_CHOOSE) {
            return;
        }

        canvas.save();
        canvas.clipRect(_rectMain); // 限制绘制区域

        drawHighLight_Hint(canvas);

        canvas.restore();
    }


    private void drawHighLight_Hint(Canvas canvas) {

        double closeX = Double.MAX_VALUE;
        double closeY = Double.MAX_VALUE;
        Entry hitEntry = null;

        if (isClick) {
            // 点击光标进来的
            for (int i = 0; i < _lines.getLines().size(); i++) {
                Line line = _lines.getLines().get(i);
                int indexEntry = Line.getEntryIndex(line.getEntries(), highValueX, Line.Rounding.CLOSEST);

                if (indexEntry < 0 || indexEntry >= line.getEntries().size()) {
                    continue;
                }

                Entry entry = line.getEntries().get(indexEntry);

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
                        record_EntryIndex = indexEntry;
                        record_LineIndex = i;
                    }
                }
            }
        } else {
            // 左右移动进来的
            try {
                Line line = _lines.getLines().get(record_LineIndex);
                hitEntry = line.getEntries().get(record_EntryIndex);
            } catch (Exception e) {
                hitEntry = null;
            }
        }


        if (hitEntry == null) {
            return;
        }

        highValueX = hitEntry.getX();// real indexX

        SingleF_XY xy = _MappingManager.getPxByEntry(hitEntry);


        // draw high line
        if (_highLight.isDrawHighLine()) {
            paintHighLight.setStrokeWidth(_highLight.getHighLightWidth());
            paintHighLight.setColor(_highLight.getHighLightColor());

            canvas.drawLine(_rectMain.left, xy.getY(), _rectMain.right, xy.getY(), paintHighLight);
            canvas.drawLine(xy.getX(), _rectMain.top, xy.getX(), _rectMain.bottom, paintHighLight);
        }

        // draw hint
        if (_highLight.isDrawHint()) {
            paintHint.setColor(_highLight.getHintColor());
            paintHint.setTextSize(_highLight.getHintTextSize());

            String xStr = _highLight.getxValueAdapter().value2String(hitEntry.getX());
            String yStr = _highLight.getyValueAdapter().value2String(hitEntry.getY());
            float txtHeight = Utils.textHeight(paintHint);
            float txtWidth = Math.max(Utils.textWidth(paintHint, xStr), Utils.textWidth(paintHint, yStr));

            float x = _rectMain.right - txtWidth - 10;
            float y = _rectMain.top + Utils.dp2px(20);

            canvas.drawText(xStr, x, y, paintHint);
            canvas.drawText(yStr, x, y + txtHeight, paintHint);
        }

        // callback
        if (isClick) {
            isClick = false;

            Line line = _lines.getLines().get(record_LineIndex);
            hitEntry = line.getEntries().get(record_EntryIndex);

            Line.CallBack_OnEntryClick cb = line.getOnEntryClick();
            if (cb != null) {
                cb.onEntry(line, hitEntry);
            }
        }

    }

    public void onDataChanged(Lines lines) {
        _lines = lines;
    }
}
