package com.linheimx.app.lchart;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.linheimx.app.library.adapter.DefaultHighLightValueAdapter;
import com.linheimx.app.library.adapter.DefaultValueAdapter;
import com.linheimx.app.library.adapter.IValueAdapter;
import com.linheimx.app.library.charts.LineChart;
import com.linheimx.app.library.data.Entry;
import com.linheimx.app.library.data.Line;
import com.linheimx.app.library.data.Lines;
import com.linheimx.app.library.model.HightLight;
import com.linheimx.app.library.model.XAxis;
import com.linheimx.app.library.model.YAxis;

import java.util.ArrayList;
import java.util.List;

public class LineChartActivity extends AppCompatActivity {

    LineChart _lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart);

        getSupportActionBar().setTitle("折线图：基本");

        _lineChart = (LineChart) findViewById(R.id.chart);

        setChartData(_lineChart);
    }


    private void setChartData(LineChart lineChart) {

        // 高亮
        HightLight hightLight = lineChart.get_HightLight();
        hightLight.setEnable(true);// 启用高亮显示  默认为启用状态
        hightLight.setxValueAdapter(new IValueAdapter() {
            @Override
            public String value2String(double value) {
                return "X:" + value;
            }
        });
        hightLight.setyValueAdapter(new IValueAdapter() {
            @Override
            public String value2String(double value) {
                return "Y:" + value;
            }
        });

        // x,y轴上的单位
        XAxis xAxis = lineChart.get_XAxis();
        xAxis.set_unit("s");
        xAxis.set_ValueAdapter(new DefaultValueAdapter(1));

        YAxis yAxis = lineChart.get_YAxis();
        yAxis.set_unit("m");
        yAxis.set_ValueAdapter(new DefaultValueAdapter(3));// 默认精度到小数点后两位

        // 数据
        Line line = new Line();
        List<Entry> list = new ArrayList<>();
        list.add(new Entry(1, 5));
        list.add(new Entry(2, 4));
        list.add(new Entry(3, 2));
        list.add(new Entry(4, 3));
        list.add(new Entry(10, 8));
        line.setEntries(list);

        Lines lines = new Lines();
        lines.addLine(line);


        lineChart.setLines(lines);
    }
}
