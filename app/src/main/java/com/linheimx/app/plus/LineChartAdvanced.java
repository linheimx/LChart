package com.linheimx.app.plus;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.linheimx.app.lchart.R;
import com.linheimx.app.library.charts.LineChart;
import com.linheimx.app.library.data.Lines;

import java.util.ArrayList;
import java.util.List;

/**
 * 包装一下 lineChart
 * <p>
 * Created by LJIAN on 2017/1/9.
 */

public class LineChartAdvanced extends FrameLayout {

    LineChart _lineChart;

    public LineChartAdvanced(Context context) {
        super(context);
        init(context);
    }

    public LineChartAdvanced(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LineChartAdvanced(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.linechart_advanced, null);
        addView(view);

        _lineChart = (LineChart) view.findViewById(R.id.linechart);

        final List<String> curList = new ArrayList<>();
        curList.add("光标1");
        curList.add("光标2");

        Spinner spinner = (Spinner) view.findViewById(R.id.sp_cursor);
        spinner.setAdapter(new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_list_item_1, curList));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String s = curList.get(position);
                if (s.equals("光标1")) {
                    _lineChart.set_CursorMode(LineChart.CursorMode.cursor1);
                } else if (s.equals("光标2")) {
                    _lineChart.set_CursorMode(LineChart.CursorMode.cursor2);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ImageButton btnLeft = (ImageButton) view.findViewById(R.id.btnLeft);
        ImageButton btnRight = (ImageButton) view.findViewById(R.id.btnRight);

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

    public LineChart get_lineChart() {
        return _lineChart;
    }

    public void set_lineChart(LineChart _lineChart) {
        this._lineChart = _lineChart;
    }

    public void btnLeft() {
        _lineChart.highLightLeft();
    }

    public void btnRight() {
        _lineChart.highLightRight();
    }

}
