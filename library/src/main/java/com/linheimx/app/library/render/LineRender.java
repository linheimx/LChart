package com.linheimx.app.library.render;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.linheimx.app.library.data.Entry;
import com.linheimx.app.library.data.Line;
import com.linheimx.app.library.data.Lines;
import com.linheimx.app.library.manager.TransformManager;
import com.linheimx.app.library.manager.ViewPortManager;
import com.linheimx.app.library.utils.LogUtil;
import com.linheimx.app.library.utils.U_XY;
import com.linheimx.app.library.utils.Utils;

import java.util.List;

/**
 * Created by LJIAN on 2016/11/17.
 */

public class LineRender extends BaseRender {

    Lines _lines;
    Paint _PaintLine;

    public LineRender(ViewPortManager _ViewPortManager, TransformManager _TransformManager, Lines _lines) {
        super(_ViewPortManager, _TransformManager);
        this._lines = _lines;
        _PaintLine = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    public void draw(Canvas canvas) {

        if (_lines == null) {
            return;
        }

        canvas.save();
        // draw line
        for (Line line : _lines.getLines()) {
            drawLine(canvas, line);
        }
        canvas.restore();
    }

    private float[] _LineBuffer = new float[4];

    private void drawLine(Canvas canvas, Line line) {

        _PaintLine.setStrokeWidth(Utils.dp2px(2));
        _PaintLine.setColor(Color.RED);

        List<Entry> list = line.getEntries();

        if (list.size() <= 1) {
            return;
        }

        int lineCount = list.size() - 1;

        if (_LineBuffer.length < lineCount * 4) {
            _LineBuffer = new float[lineCount * 4];
        }


        int j = 0;

        for (int i = 0; i < list.size() - 1; i++) {
            Entry start = list.get(i);
            Entry end = list.get(i + 1);

            _LineBuffer[j++] = start.getX();
            _LineBuffer[j++] = start.getY();
            _LineBuffer[j++] = end.getX();
            _LineBuffer[j++] = end.getY();
        }

        _TransformManager.value2Px(_LineBuffer);

        canvas.drawLines(_LineBuffer, _PaintLine);

    }


    public void notifyDataChanged(Lines lines) {
        _lines = lines;
    }

}
