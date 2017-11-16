package com.linheimx.app.lchart;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;

import com.linheimx.app.library.adapter.DefaultValueAdapter;
import com.linheimx.app.library.adapter.IValueAdapter;
import com.linheimx.app.library.charts.LineChart;
import com.linheimx.app.library.data.Entry;
import com.linheimx.app.library.data.Line;
import com.linheimx.app.library.data.Lines;
import com.linheimx.app.library.model.Axis;
import com.linheimx.app.library.model.HighLight;
import com.linheimx.app.library.model.XAxis;
import com.linheimx.app.library.model.YAxis;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LineChartActivity extends AppCompatActivity {

    LineChart _lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart);

        getSupportActionBar().setTitle("折线图：基本");

        _lineChart = (LineChart) findViewById(R.id.chart);
        SeekBar sb1 = (SeekBar) findViewById(R.id.sb_entry_more);
        CheckBox cb = (CheckBox) findViewById(R.id.cb_high);
        CheckBox cb_fill = (CheckBox) findViewById(R.id.cb_fill);

//        _lineChart.setCanX_drag(true);// x 方向可以拖拽
//        _lineChart.setCanY_drag(false);// y 方向不可拖拽
//        _lineChart.setCanX_zoom(true);// x 方向可以缩放
//        _lineChart.setCanY_zoom(false);// y 方向不可用缩放
//        _lineChart.setZoom_alone(true);// 设置在 x，y方向上是否要独立缩放


        setChartData(_lineChart);

        sb1.setProgress(5);
        sb1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Lines lines = _lineChart.getlines();
                Line line = lines.getLines().get(0);

                line.setEntries(generateLineData(progress));
                _lineChart.notifyDataChanged();
                _lineChart.invalidate();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                HighLight highLight = _lineChart.get_HighLight1();
                highLight.setEnable(isChecked);// 启用高亮显示  默认为启用状态
                _lineChart.invalidate();
            }
        });

        cb_fill.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Lines lines = _lineChart.getlines();
                if (lines != null && lines.getLines().size() > 0) {
                    Line line = lines.getLines().get(0);
                    line.setFilled(isChecked);
                    _lineChart.invalidate();
                }
            }
        });
    }


    private void setChartData(LineChart lineChart) {

        lineChart.get_MappingManager().setCurrent2Fat(true);
        lineChart.get_MappingManager().setFatFactor(1.1f);//设置 可见视图与原始数据视图的比例
//        lineChart.setCanY_zoom(false);//设置只能y方向是否能缩放！

        // 高亮
        HighLight highLight = lineChart.get_HighLight1();
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
        xAxis.setCalWay(Axis.CalWay.every); // 轴线上label的计算方式
        xAxis.set_unit("单位：s");
        xAxis.set_ValueAdapter(new DefaultValueAdapter(1));

        YAxis yAxis = lineChart.get_YAxis();
        yAxis.set_unit("单位：m");
        yAxis.setCalWay(Axis.CalWay.every);
//        yAxis.set_enableUnit(false);
        yAxis.set_ValueAdapter(new DefaultValueAdapter(2));// 默认精度到小数点后2位,现在修改为3位精度

        // 数据
        Line line = new Line();
        line.setLineColor(Color.RED);
        List<Entry> list = new ArrayList<>();
//        list.add(new Entry(1, 5));
//        list.add(new Entry(2, 4));
//        list.add(new Entry(3, 2));
//        list.add(new Entry(4, 3));
//        list.add(new Entry(10, 8));

        list.add(new Entry(0, 0));
        list.add(new Entry(4, 0));
        list.add(new Entry(4, 1));
        list.add(new Entry(8, 1));
        list.add(new Entry(8, 2));
        list.add(new Entry(12, 2));
        list.add(new Entry(12, 3));
        list.add(new Entry(16, 3));
        list.add(new Entry(16, 2));
        list.add(new Entry(20, 2));
        list.add(new Entry(20, 1));
        list.add(new Entry(24, 1));

        line.setEntries(list);

        Lines lines = new Lines();
        lines.addLine(line);

        lineChart.setLines(lines);

        //        lineChart.setMaxMin(2,20,1,10);//手动设置，x和y方向上的数据范围
    }


    private List<Entry> generateLineData(int dataCount) {

        List<Entry> list = new ArrayList<>();

        Random random = new Random();
        for (int i = 0; i < dataCount; i++) {
            double x = i;
            double y = random.nextDouble() * 100;
            list.add(new Entry(x, y));
        }

        return list;
    }
}
