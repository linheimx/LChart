package com.linheimx.app.library.charts;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.linheimx.app.library.R;
import com.linheimx.app.library.data.Lines;
import com.linheimx.app.library.utils.LogUtil;

/**
 * Created by LJIAN on 2017/1/9.
 */

public class LineChartPlus extends FrameLayout {

    LineChart _lineChart;

    public LineChartPlus(Context context) {
        super(context);
        init(context);
    }

    public LineChartPlus(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LineChartPlus(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.linechart_plus, null);
        addView(view);

        _lineChart = (LineChart) view.findViewById(R.id.linechart);

        Button btnLeft = (Button) view.findViewById(R.id.btnLeft);
        Button btnRight = (Button) view.findViewById(R.id.btnRight);

        btnLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                btnLeft();
            }
        });

        btnRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRight();
            }
        });
    }

    public void setLines(Lines lines) {
        _lineChart.setLines(lines);
    }


    public void btnLeft() {
        _lineChart.highLightLeft();
    }

    public void btnRight() {
        _lineChart.highLightRight();
    }

}
