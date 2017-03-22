package com.linheimx.app.lchart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.linheimx.app.library.adapter.DefaultValueAdapter;
import com.linheimx.app.library.adapter.IValueAdapter;
import com.linheimx.app.library.charts.LineChart;
import com.linheimx.app.library.data.Entry;
import com.linheimx.app.library.data.Line;
import com.linheimx.app.library.data.Lines;
import com.linheimx.app.library.model.HighLight;
import com.linheimx.app.library.model.XAxis;
import com.linheimx.app.library.model.YAxis;
import com.linheimx.app.library.utils.RectD;

public class RealTimeActivity extends AppCompatActivity {

    LineChart _lineChart;
    Line _line;
    Button _btnStart, _btnStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_time);

        getSupportActionBar().setTitle("折线图：实时趋势");

        _lineChart = (LineChart) findViewById(R.id.chart);
        _btnStart = (Button) findViewById(R.id.btn_start);
        _btnStop = (Button) findViewById(R.id.btn_stop);


        setChartData(_lineChart);

        _btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 如果已经在采集了，不继续执行啦
                if (_go) {
                    return;
                }

                _go = true;

                addData();
            }
        });

        _btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _go = false;
            }
        });
    }

    private void setChartData(LineChart lineChart) {

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
        xAxis.set_unit("单位：个");
        xAxis.set_ValueAdapter(new DefaultValueAdapter(0));

        YAxis yAxis = lineChart.get_YAxis();
        yAxis.set_unit("单位：m");
        yAxis.set_ValueAdapter(new DefaultValueAdapter(1));// 默认精度到小数点后2位,现在修改为0位精度

        // 数据
        _line = new Line();
        _line.setDrawCircle(false);

        Lines lines = new Lines();
        lines.addLine(_line);

        lineChart.setLines(lines);
    }

    boolean _go;
    int i = 0;

    /**
     * 向line中添加数据
     */
    private void addData() {

        if (!_go) {
            return;
        }

        _go = true;
        i++;


        _btnStart.postDelayed(new Runnable() {
            @Override
            public void run() {

                _line.addEntry(new Entry(i, Math.random() * 100));
                _lineChart.notifyDataChanged();

                if (_line.getmXMax() > 100) {
                    // 设置当前可视的viewport
                    RectD viewport = _lineChart.get_currentViewPort();
                    viewport.right = _line.getmXMax();
                    viewport.left = viewport.right - 100;
                    _lineChart.set_currentViewPort(viewport);
                }

                _lineChart.invalidate();

                addData();
            }
        }, 10);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        _go = false;
    }
}
