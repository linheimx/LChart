package com.linheimx.app.lchart;

import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.linheimx.app.library.charts.LineChart;
import com.linheimx.app.library.data.Entry;
import com.linheimx.app.library.data.Line;
import com.linheimx.app.library.data.Lines;

import java.util.ArrayList;
import java.util.List;

public class MultiLineActivity extends AppCompatActivity {

    Handler handler = new Handler();
    LineChart _lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_line);

        getSupportActionBar().setTitle("折线图：多条折线");

        _lineChart = (LineChart) findViewById(R.id.chart);

        Line line1 = new Line();
        List<Entry> list1 = new ArrayList<>();
        list1.add(new Entry(1, 5));
        list1.add(new Entry(2, 4));
        list1.add(new Entry(3, 2));
        list1.add(new Entry(4, 3));
        list1.add(new Entry(10, 8));
        line1.setEntries(list1);

        Line line2 = new Line();
        line2.setLineColor(Color.BLUE);

        List<Entry> list2 = new ArrayList<>();
        list2.add(new Entry(1, 10));
        list2.add(new Entry(2.5, 4.8));
        list2.add(new Entry(3.6, 2.7));
        list2.add(new Entry(15, 8.7));
        line2.setEntries(list2);


        Lines lines = new Lines();
        lines.addLine(line1);
        lines.addLine(line2);

        _lineChart.setLines(lines);
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
