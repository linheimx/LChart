package com.linheimx.app.lchart;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.linheimx.app.library.adapter.DefaultValueAdapter;
import com.linheimx.app.library.charts.LineChart;
import com.linheimx.app.library.data.Entry;
import com.linheimx.app.library.data.Line;
import com.linheimx.app.library.data.Lines;
import com.linheimx.app.library.model.HighLight;
import com.linheimx.app.library.model.WarnLine;
import com.linheimx.app.library.model.XAxis;
import com.linheimx.app.library.model.YAxis;

import java.util.ArrayList;
import java.util.List;

public class WarnLinesActivity extends AppCompatActivity {

    LineChart _lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warn_lines);

        getSupportActionBar().setTitle("折线图：预警线");

        _lineChart = (LineChart) findViewById(R.id.chart);

        setChartData(_lineChart);
    }

    private void setChartData(LineChart lineChart) {

        // 高亮
        HighLight highLight = lineChart.get_HighLight1();
        highLight.setEnable(false);// 启用高亮显示  默认为启用状态

        // x,y轴上的单位
        XAxis xAxis = lineChart.get_XAxis();
        xAxis.set_unit("单位：s");
        xAxis.set_ValueAdapter(new DefaultValueAdapter(1));

        //---> x轴上的预警线
        List<WarnLine> xWarns = new ArrayList<>();
        xWarns.add(new WarnLine(2, Color.BLUE));
        xWarns.add(new WarnLine(3.5));
        xAxis.setListWarnLins(xWarns);

        YAxis yAxis = lineChart.get_YAxis();
        yAxis.set_unit("单位：m");

        //---> y轴上的预警线
        List<WarnLine> yWarns = new ArrayList<>();
        yWarns.add(new WarnLine(4, Color.GREEN));
        yWarns.add(new WarnLine(5.5));
        yAxis.setListWarnLins(yWarns);


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
