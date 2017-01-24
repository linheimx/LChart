package com.linheimx.app.lchart;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.linheimx.app.library.charts.LineChart;
import com.linheimx.app.library.data.Entry;
import com.linheimx.app.library.data.Line;
import com.linheimx.app.library.data.Lines;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LineChartActivity extends AppCompatActivity {

    Handler handler = new Handler();
    LineChart _lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart);

        _lineChart = (LineChart) findViewById(R.id.chart);

        initChart(_lineChart);

        goLeft();
    }


    void initChart(LineChart lineChart) {

        Line line = new Line();
        line.setDrawCircle(false);
        List<Entry> list = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            list.add(new Entry(i,random.nextInt()));
        }

        line.setEntries(list);

        List<Line> list2 = new ArrayList<>();
        list2.add(line);
        Lines lines = new Lines(list2);

        lineChart.setLines(lines);
    }

    void test() {
        //        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//                Line line = new Line();
//                List<Entry> list = new ArrayList<>();
//
//                for (int i = 0; i < 1000; i++) {
//                    list.add(new Entry(i, (float) Math.random()));
//                }
//                line.setEntries(list);
//                line.setDrawCircle(false);
//
//                List<Line> list2 = new ArrayList<>();
//                list2.add(line);
//                Lines lines = new Lines(list2);
//
//                _lineChart.setLines(lines);
//            }
//        }, 5000);
    }


    /**
     * 光标左移动
     */
    void goLeft() {

//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                goLeft();
//            }
//        },3000);

        _lineChart.highLightLeft();
    }
}
