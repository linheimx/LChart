package com.linheimx.app.lchart;

import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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

public class MultiLineActivity extends AppCompatActivity {

    LineChart _lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_line);

        getSupportActionBar().setTitle("折线图：多条折线");

        _lineChart = (LineChart) findViewById(R.id.chart);

      setChartData(_lineChart);
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
        // line1
        Line line = new Line();
        List<Entry> list = new ArrayList<>();
        list.add(new Entry(1, 5));
        list.add(new Entry(2, 4));
        list.add(new Entry(3, 2));
        list.add(new Entry(4, 3));
        list.add(new Entry(10, 8));
        line.setEntries(list);

        // line2
        Line line2 = new Line();
        line2.setLineColor(Color.BLUE);

        List<Entry> list2 = new ArrayList<>();
        list2.add(new Entry(1, 10));
        list2.add(new Entry(2.5, 4.8));
        list2.add(new Entry(3.6, 2.7));
        list2.add(new Entry(15, 8.7));
        line2.setEntries(list2);

        Lines lines = new Lines();
        lines.addLine(line);
        lines.addLine(line2);

        lineChart.setLines(lines);
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
