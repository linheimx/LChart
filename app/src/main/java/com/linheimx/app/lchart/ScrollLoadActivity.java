package com.linheimx.app.lchart;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.linheimx.app.library.adapter.DefaultValueAdapter;
import com.linheimx.app.library.adapter.IValueAdapter;
import com.linheimx.app.library.charts.LineChart;
import com.linheimx.app.library.data.Entry;
import com.linheimx.app.library.data.Line;
import com.linheimx.app.library.data.Lines;
import com.linheimx.app.library.listener.IDragListener;
import com.linheimx.app.library.model.HighLight;
import com.linheimx.app.library.model.XAxis;
import com.linheimx.app.library.model.YAxis;

import java.util.ArrayList;
import java.util.List;

public class ScrollLoadActivity extends AppCompatActivity {

    LineChart _lineChart;
    ProgressDialog _dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_load);

        getSupportActionBar().setTitle("滑动加载");

        _lineChart = (LineChart) findViewById(R.id.chart);


        _dialog = new ProgressDialog(this);
        _dialog.setTitle("loading...");

        setChartData(_lineChart);
    }


    private void setChartData(LineChart lineChart) {

//        _lineChart.setDragable(true);
//        _lineChart.setScaleable(false);


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
        xAxis.set_unit("单位：s");
        xAxis.set_ValueAdapter(new DefaultValueAdapter(1));

        YAxis yAxis = lineChart.get_YAxis();
        yAxis.set_unit("单位：m");
        yAxis.set_ValueAdapter(new DefaultValueAdapter(3));// 默认精度到小数点后2位,现在修改为3位精度

        // 数据
        final Line line = new Line();
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

        _lineChart.set_dragListener(new IDragListener() {
            @Override
            public void onDrag(final double xMin, final double xMax) {

                // 右边添加更多的数据
                if (xMax > line.getmXMax()) {
                    _dialog.show();
                    _lineChart.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            line.addEntry(new Entry(xMax + 1, 1));
                            line.addEntry(new Entry(xMax + 2, 5));
                            _lineChart.notifyDataChanged();
                            _lineChart.invalidate();
                            _dialog.dismiss();
                        }
                    }, 1000);
                }

                // 左边添加更多的数据
                if (xMin < line.getmXMin()) {
                    _dialog.show();
                    _lineChart.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            line.addEntryInHead(new Entry(xMin - 1, 1));
                            line.addEntryInHead(new Entry(xMin - 2, 5));
                            _lineChart.notifyDataChanged();
                            _lineChart.invalidate();
                            _dialog.dismiss();
                        }
                    }, 1000);
                }
            }
        });
    }


}
