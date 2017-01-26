package com.linheimx.app.lchart;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.linheimx.app.library.charts.LineChart;
import com.linheimx.app.library.charts.LineChartPlus;
import com.linheimx.app.library.data.Entry;
import com.linheimx.app.library.data.Line;
import com.linheimx.app.library.data.Lines;

import java.util.ArrayList;
import java.util.List;

public class LineChartPlusActivity extends AppCompatActivity {
    Handler handler = new Handler();
    LineChartPlus lineChartPlus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart_plus);

        lineChartPlus = (LineChartPlus) findViewById(R.id.chartPlus);

        initChart(lineChartPlus);
    }


    void initChart(LineChartPlus lineChartPlus) {

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

        lineChartPlus.setLines(lines);

        test();
    }

    void test() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Line line = new Line();
                List<Entry> list = new ArrayList<>();

                for (int i = 0; i < 2000; i++) {
                    list.add(new Entry(i, (float) Math.random()));
                }
                line.setEntries(list);
                line.setDrawCircle(false);

                List<Line> list2 = new ArrayList<>();
                list2.add(line);
                Lines lines = new Lines(list2);

                lineChartPlus.setLines(lines);
            }
        }, 5000);
    }
}
