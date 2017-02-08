package com.linheimx.app.lchart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.linheimx.app.library.charts.LineChart;
import com.linheimx.app.library.data.Entry;
import com.linheimx.app.library.data.Line;
import com.linheimx.app.library.data.Lines;

import java.util.ArrayList;
import java.util.List;

public class GodActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_god);

        getSupportActionBar().setTitle("折线图：上帝视角");

        LineChart lineChart1 = (LineChart) findViewById(R.id.chart1);
        LineChart lineChart2 = (LineChart) findViewById(R.id.chart2);

//        lineChart2.set_ChartMode(LineChart.ChartMode.God);//切换成上帝视角  xml已经写了
        lineChart2.registObserver(lineChart1);

        addData(lineChart1);
        addData(lineChart2);
    }

    private void addData(LineChart lineChart) {
        Line line = new Line();
        List<Entry> list = new ArrayList<>();
        list.add(new Entry(1, 5));
        list.add(new Entry(2, 4));
        list.add(new Entry(3, 2));
        list.add(new Entry(4, 3));
        list.add(new Entry(5, 8));
        line.setEntries(list);

        List<Line> list2 = new ArrayList<>();
        list2.add(line);
        Lines lines = new Lines(list2);

        lineChart.setLines(lines);
    }
}
