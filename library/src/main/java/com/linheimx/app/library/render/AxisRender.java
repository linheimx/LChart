package com.linheimx.app.library.render;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

import com.linheimx.app.library.manager.MappingManager;
import com.linheimx.app.library.model.Axis;
import com.linheimx.app.library.utils.Utils;

/**
 * Created by lijian on 2016/11/14.
 */

public abstract class AxisRender extends BaseRender {

    Axis _Axis;

    Paint _PaintAxis;
    Paint _PaintGridline;
    Paint _PaintLittle;
    Paint _PaintLabel;
    Paint _PaintUnit;
    Paint _PaintWarnText;
    Paint _PaintWarnPath;

    Path _PathGrid;
    Path _PathWarn;

    public AxisRender(RectF rectMain, MappingManager _MappingManager, Axis axis) {
        super(rectMain, _MappingManager);

        this._Axis = axis;

        _PaintAxis = new Paint(Paint.ANTI_ALIAS_FLAG);
        _PaintLabel = new Paint(Paint.ANTI_ALIAS_FLAG);
        _PaintLittle = new Paint(Paint.ANTI_ALIAS_FLAG);
        _PaintGridline = new Paint(Paint.ANTI_ALIAS_FLAG);
        _PaintUnit = new Paint(Paint.ANTI_ALIAS_FLAG);
        _PaintWarnText = new Paint(Paint.ANTI_ALIAS_FLAG);
        _PaintWarnPath = new Paint(Paint.ANTI_ALIAS_FLAG);

        // 虚线效果
        _PaintGridline.setStyle(Paint.Style.STROKE);
        _PaintGridline.setPathEffect(new DashPathEffect(new float[]{3, 2}, 0));

        _PaintWarnPath.setStyle(Paint.Style.STROKE);
        _PaintWarnPath.setPathEffect(new DashPathEffect(new float[]{3, 2}, 0));

        _PathGrid = new Path();
        _PathWarn = new Path();
    }

    public void renderAxisLine(Canvas canvas) {
        _PaintAxis.setColor(_Axis.getAxisColor());
        _PaintAxis.setStrokeWidth(_Axis.getAxisWidth());
    }

    public void renderGridline(Canvas canvas) {
        _PaintGridline.setColor(Color.parseColor("#CCCCCC"));
        _PaintGridline.setStrokeWidth(Utils.dp2px(1));
    }

    public void renderLabels(Canvas canvas) {
        // label
        paint_label();
        // little
        paint_little();
    }

    public void renderUnit(Canvas canvas) {
        paint_unit();
    }

    public void renderWarnLine(Canvas canvas) {


    }


    private void i1_______________________________________________() {

    }

    public void paint_label() {
        _PaintLabel.setColor(_Axis.getLabelColor());
        _PaintLabel.setTextSize(_Axis.getLabelTextSize());
    }

    public void paint_little() {
        _PaintLittle.setColor(_Axis.getAxisColor());
        _PaintLittle.setStrokeWidth(_Axis.getAxisWidth());
    }

    public void paint_unit() {
        _PaintUnit.setColor(_Axis.getUnitColor());
        _PaintUnit.setTextSize(_Axis.getUnitTxtSize());
    }

    private void i2_______________________________________________() {

    }

    public Paint get_PaintAxis() {
        return _PaintAxis;
    }

    public void set_PaintAxis(Paint _PaintAxis) {
        this._PaintAxis = _PaintAxis;
    }

    public Paint get_PaintGridline() {
        return _PaintGridline;
    }

    public void set_PaintGridline(Paint _PaintGridline) {
        this._PaintGridline = _PaintGridline;
    }

    public Paint get_PaintLittle() {
        return _PaintLittle;
    }

    public void set_PaintLittle(Paint _PaintLittle) {
        this._PaintLittle = _PaintLittle;
    }

    public Paint get_PaintLabel() {
        return _PaintLabel;
    }

    public void set_PaintLabel(Paint _PaintLabel) {
        this._PaintLabel = _PaintLabel;
    }

    public Paint get_PaintUnit() {
        return _PaintUnit;
    }

    public void set_PaintUnit(Paint _PaintUnit) {
        this._PaintUnit = _PaintUnit;
    }

    public Paint get_PaintWarnText() {
        return _PaintWarnText;
    }

    public void set_PaintWarnText(Paint _PaintWarnText) {
        this._PaintWarnText = _PaintWarnText;
    }

    public Paint get_PaintWarnPath() {
        return _PaintWarnPath;
    }

    public void set_PaintWarnPath(Paint _PaintWarnPath) {
        this._PaintWarnPath = _PaintWarnPath;
    }

    public Path get_PathGrid() {
        return _PathGrid;
    }

    public void set_PathGrid(Path _PathGrid) {
        this._PathGrid = _PathGrid;
    }

    public Path get_PathWarn() {
        return _PathWarn;
    }

    public void set_PathWarn(Path _PathWarn) {
        this._PathWarn = _PathWarn;
    }
}
