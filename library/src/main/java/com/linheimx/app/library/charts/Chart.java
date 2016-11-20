package com.linheimx.app.library.charts;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.linheimx.app.library.touch.TouchListener;
import com.linheimx.app.library.utils.Utils;

/**
 * Created by Administrator on 2016/11/13.
 */

public abstract class Chart extends ViewGroup implements IFunction {

    ////////////////////////// function //////////////////////////
    boolean isHighLightEnabled = true;
    boolean isTouchEnabled = true;


    public Chart(Context context) {
        super(context);
        init(context);
    }

    public Chart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Chart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    protected void init(Context context) {
        Utils.init(context);
        setWillNotDraw(false); // 我要canvas绘制，so will render
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int size = (int) Utils.dp2px(100);
        int width = resolveSize(size, widthMeasureSpec);
        int height = resolveSize(size, heightMeasureSpec);

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).layout(l, t, r, b);
        }
    }


    public boolean isHighLightEnabled() {
        return isHighLightEnabled;
    }

    public void setHighLightEnabled(boolean highLightEnabled) {
        isHighLightEnabled = highLightEnabled;
    }

    public boolean isTouchEnabled() {
        return isTouchEnabled;
    }

    public void setTouchEnabled(boolean touchEnabled) {
        isTouchEnabled = touchEnabled;
    }
}
