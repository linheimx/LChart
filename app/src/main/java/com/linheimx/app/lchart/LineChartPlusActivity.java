package com.linheimx.app.lchart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.linheimx.app.library.adapter.IValueAdapter;
import com.linheimx.app.library.charts.LineChart;
import com.linheimx.app.library.charts.LineChartPlus;
import com.linheimx.app.library.data.Entry;
import com.linheimx.app.library.data.Line;
import com.linheimx.app.library.data.Lines;
import com.linheimx.app.library.model.HightLight;
import com.linheimx.app.library.model.XAxis;
import com.linheimx.app.library.model.YAxis;

import java.util.ArrayList;
import java.util.List;

public class LineChartPlusActivity extends AppCompatActivity {

    LineChartPlus lineChartPlus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart_plus);

        getSupportActionBar().setTitle("折线图：带功能按钮");

        lineChartPlus = (LineChartPlus) findViewById(R.id.chartPlus);

        setChartData(lineChartPlus.get_lineChart());
    }


    private void setChartData(LineChart lineChart){

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
        XAxis xAxis=lineChart.get_XAxis();
        xAxis.set_unit("s");

        YAxis yAxis=lineChart.get_YAxis();
        yAxis.set_unit("m");

        // 数据
        Line line = new Line();
        line.setDrawCircle(false);//不画圆圈
        List<Entry> list = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            list.add(new Entry(i, (float) Math.random()));
        }
        line.setEntries(list);

        Lines lines = new Lines();
        lines.addLine(line);

        lineChart.setLines(lines);
    }
}
