package com.linheimx.app.lchart;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.linheimx.app.library.adapter.IValueAdapter;
import com.linheimx.app.library.charts.LineChart;
import com.linheimx.app.library.data.Entry;
import com.linheimx.app.library.data.Line;
import com.linheimx.app.library.data.Lines;
import com.linheimx.app.library.model.HighLight;
import com.linheimx.app.library.model.XAxis;
import com.linheimx.app.library.model.YAxis;
import com.linheimx.app.plus.LineChartAdvanced;

import java.util.ArrayList;
import java.util.List;

public class AdvancedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced);

        getSupportActionBar().setTitle("折线图：高级");

        LineChartAdvanced lineChartAdvanced = (LineChartAdvanced) findViewById(R.id.linechart);

        setChartData(lineChartAdvanced.get_lineChart());
    }


    private void setChartData(LineChart lineChart) {

        // 高亮
        HighLight highLight = lineChart.get_HighLight1();
        highLight.setEnable(true);// 启用高亮显示  默认为启用状态
        highLight.setxValueAdapter(new IValueAdapter() {
            @Override
            public String value2String(double value) {
                return "X1:" + value;
            }
        });
        highLight.setyValueAdapter(new IValueAdapter() {
            @Override
            public String value2String(double value) {
                return "Y1:" + Math.round(value * 100) / 100f;//保留两位小数
            }
        });

        HighLight highLight2 = lineChart.get_HighLight2();
        highLight2.setHighLightColor(Color.GREEN);
        highLight2.setEnable(true);// 启用高亮显示  默认为启用状态
        highLight2.setxValueAdapter(new IValueAdapter() {
            @Override
            public String value2String(double value) {
                return "X2:" + value;
            }
        });
        highLight2.setyValueAdapter(new IValueAdapter() {
            @Override
            public String value2String(double value) {
                return "Y2:" + Math.round(value * 100) / 100f;//保留两位小数
            }
        });

        // x,y轴上的单位
        XAxis xAxis = lineChart.get_XAxis();
        xAxis.set_unit("s");

        YAxis yAxis = lineChart.get_YAxis();
        yAxis.set_unit("m");

        // 数据
        Line line = new Line();
        line.setDrawCircle(false);//不画圆圈
        line.setLineColor(Color.BLUE);
        List<Entry> list = new ArrayList<>();
        for (int i = 0; i < 300; i++) {
            list.add(new Entry(i, (float) Math.random()));
        }
        line.setEntries(list);

        Lines lines = new Lines();
        lines.addLine(line);

        lineChart.setLines(lines);
    }

}
