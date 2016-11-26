package com.linheimx.app.library.render;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.linheimx.app.library.adapter.DefaultValueAdapter;
import com.linheimx.app.library.adapter.IValueAdapter;
import com.linheimx.app.library.manager.TransformManager;
import com.linheimx.app.library.manager.ViewPortManager;
import com.linheimx.app.library.utils.Utils;

/**
 * Created by LJIAN on 2016/11/14.
 */

public abstract class AxisRender extends BaseRender {

    float min, max, range;

    float[] labelValues = new float[]{};
    int labelCount = 6;
    int labelCountAdvice = labelCount;
    boolean isPerfectLabel = true;

    IValueAdapter _ValueAdapter = new DefaultValueAdapter(2);

    Paint _PaintAxis;
    Paint _PaintGridline;
    Paint _PaintLittle;
    Paint _PaintLabel;

    int axisColor = Color.BLACK;
    int axisWidth = 2;
    int labelColor = Color.BLUE;
    int labelSize = 7;


    public AxisRender(ViewPortManager _ViewPortManager, TransformManager _TransformManager) {
        super(_ViewPortManager, _TransformManager);

        _PaintAxis = new Paint(Paint.ANTI_ALIAS_FLAG);
        _PaintLabel = new Paint(Paint.ANTI_ALIAS_FLAG);
        _PaintLittle = new Paint(Paint.ANTI_ALIAS_FLAG);
        _PaintGridline = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    public void render(Canvas canvas) {
        // 考虑到外观的原因，绘制的次序由 lineChart 中的draw来控制。
    }

    public void renderAxisLine(Canvas canvas) {
        _PaintAxis.setColor(axisColor);
        _PaintAxis.setStrokeWidth(Utils.dp2px(axisWidth));
    }

    public void renderLabels(Canvas canvas) {
        _PaintLabel.setColor(labelColor);
        _PaintLabel.setTextSize(Utils.dp2px(labelSize));

        _PaintLittle.setColor(axisColor);
        _PaintLittle.setStrokeWidth(Utils.dp2px(axisWidth));

        _PaintGridline.setColor(Color.GRAY);
        _PaintGridline.setStrokeWidth(Utils.dp2px(1));
    }

    public void renderGridline(Canvas canvas){

    }



    /**
     * 计算与存储：可见区域内的每一步的数值
     * -----------------------------
     * 注意：可见区域！
     */
    public void calValues() {

        min = getVisiableMin();
        max = getVisiableMax();
        range = max - min;

        if (Math.abs(max - min) == 0) {
            return;
        }

        if (isPerfectLabel) {

            double rawInterval = range / (labelCountAdvice - 1);
            // 1.以最大数值为量程
            double interval = Utils.roundNumber2One(rawInterval);//314->300
            // 2. 量程>5，则以10为单位
            double intervalMagnitude = Math.pow(10, (int) Math.log10(interval));//100
            int intervalSigDigit = (int) (interval / intervalMagnitude);
            if (intervalSigDigit > 5) {
                interval = Math.floor(10 * intervalMagnitude);// 以10位单位 //
            }

            double first = Math.floor(min / interval) * interval;//有几个interval
            double last = Math.ceil(max / interval) * interval;

            double f;
            int n = 0;
            if (interval != 0.0) {
                for (f = first; f <= last; f += interval) {
                    ++n;
                }
            }
            labelCount = n;

            if (labelValues.length < labelCount) {
                labelValues = new float[labelCount];
            }

            f = first;
            for (int i = 0; i < n; f += interval, ++i) {

                if (f == 0.0) // Fix for negative zero case (Where value == -0.0, and 0.0 == -0.0)
                    f = 0.0;

                labelValues[i] = (float) f;
            }

//            //--------------------------> 修复第一个和最后一个不显示问题 <----------------------
//            // 第一个
//            if (first < min) {
//                labelValues[0] = min;
//            }
//            // 第二个
//            if (n > 1 && labelValues[n - 1] > max) {
//                labelValues[n - 1] = max;
//            }

        } else {
            if (labelValues.length < labelCount) {
                labelValues = new float[labelCount];
            }

            float v = min;
            float interval = range / (labelCount - 1);

            labelValues[0] = min;
            for (int i = 1; i < labelCount - 1; i++) {
                v = v + interval;
                labelValues[i] = v;
            }
            labelValues[labelCount - 1] = max;
        }
    }

    public abstract float getVisiableMin();

    public abstract float getVisiableMax();


    public float getMax() {
        return max;
    }

    public float getMin() {
        return min;
    }

    public float getRange() {
        return range;
    }
}
