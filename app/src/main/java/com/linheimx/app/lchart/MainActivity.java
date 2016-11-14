package com.linheimx.app.lchart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.linheimx.app.library.charts.LineChart;
import com.linheimx.app.library.data.Entry;
import com.linheimx.app.library.data.Line;
import com.linheimx.app.library.data.Lines;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LineChart lineChart = (LineChart) findViewById(R.id.chart);

        Line line = new Line();
        List<Entry> list = new ArrayList<>();
        list.add(new Entry(1, 5));
        list.add(new Entry(2, 4));
        list.add(new Entry(3, 2));
        list.add(new Entry(4, 3));
        list.add(new Entry(5, 10));
        line.setEntries(list);

        List<Line> list2 = new ArrayList<>();
        list2.add(line);
        Lines lines = new Lines(list2);

        lineChart.setLines(lines);

    }
}
