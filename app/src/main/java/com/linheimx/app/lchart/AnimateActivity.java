package com.linheimx.app.lchart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.linheimx.app.library.adapter.DefaultValueAdapter;
import com.linheimx.app.library.adapter.IValueAdapter;
import com.linheimx.app.library.charts.LineChart;
import com.linheimx.app.library.data.Entry;
import com.linheimx.app.library.data.Line;
import com.linheimx.app.library.data.Lines;
import com.linheimx.app.library.model.HighLight;
import com.linheimx.app.library.model.XAxis;
import com.linheimx.app.library.model.YAxis;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AnimateActivity extends AppCompatActivity {

    LineChart _lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animate);

        getSupportActionBar().setTitle("折线图：动画");

        _lineChart = (LineChart) findViewById(R.id.chart);

        setChartData(_lineChart);
    }

    private void setChartData(LineChart lineChart) {

//        lineChart.get_MappingManager().setFatFactor(1f);//设置 可见视图与原始数据视图的比例
//        lineChart.setCanY_zoom(false);//设置只能y方向是否能缩放！

        // 高亮
        HighLight highLight = lineChart.get_HighLight();
        highLight.setEnable(true);// 启用高亮显示  默认为启用状态
        highLight.setxValueAdapter(new IValueAdapter() {
            @Override
            public String value2String(double value) {
                return "X:" + value;
            }
        });
        highLight.setyValueAdapter(new IValueAdapter() {
            @Override
            public String value2String(double value) {
                return "Y:" + value;
            }
        });

        // x,y轴上的单位
        XAxis xAxis = lineChart.get_XAxis();
//        xAxis.setCalWay(Axis.CalWay.every); // 轴线上label的计算方式
        xAxis.set_unit("单位：s");
        xAxis.set_ValueAdapter(new DefaultValueAdapter(1));

        YAxis yAxis = lineChart.get_YAxis();
        yAxis.set_unit("单位：m");
//        yAxis.set_enableUnit(false);
        yAxis.set_ValueAdapter(new DefaultValueAdapter(2));// 默认精度到小数点后2位,现在修改为3位精度

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
//        lineChart.setYMax_Min(2, 20);//手动设置，x和y方向上的数据范围
//        lineChart.setXAix_MaxMin(1, 10);
    }

    public void animateY(View view) {
        _lineChart.animateY();
    }

}
