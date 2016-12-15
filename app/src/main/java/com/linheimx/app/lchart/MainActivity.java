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

public class MainActivity extends AppCompatActivity {

    Handler handler = new Handler();
    LineChart _lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _lineChart = (LineChart) findViewById(R.id.chart);

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

        _lineChart.setLines(lines);


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


        goLeft();
    }

    void goLeft(){

//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                goLeft();
//            }
//        },3000);

        _lineChart.highLightLeft();
    }
}
