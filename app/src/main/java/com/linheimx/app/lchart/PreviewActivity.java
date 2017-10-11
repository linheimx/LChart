package com.linheimx.app.lchart;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.linheimx.app.chart_dialog.ChartPreviewDialog;
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

public class PreviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_god);

        getSupportActionBar().setTitle("折线图：预览模式");

        final LineChart lineChart = (LineChart) findViewById(R.id.chart1);
        final LineChart godLineChart = (LineChart) findViewById(R.id.chart2);

//        lineChart2.set_ChartMode(LineChart.ChartMode.God);//切换成上帝视角  xml已经写了
        godLineChart.registObserver(lineChart);

        setChartData(lineChart);

        // 通知 lineChart2 数据改变了（因为lineChart1改变了）
        godLineChart.notifyDataChanged_FromOb(lineChart);
    }

    private void setChartData(LineChart lineChart) {

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
        xAxis.set_unit("s");

        YAxis yAxis = lineChart.get_YAxis();
        yAxis.set_unit("m");

        Line line = new Line();
        line.setDrawCircle(false);
        List<Entry> list = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            list.add(new Entry(i, (float) Math.random()));
        }
        line.setEntries(list);

        List<Line> list2 = new ArrayList<>();
        list2.add(line);
        Lines lines = new Lines(list2);

        lineChart.setLines(lines);
    }

    public void btnShowChart(View view) {
        ChartPreviewDialog dialog = ChartPreviewDialog.newInstance("预览模式");
        dialog.setStyle(DialogFragment.STYLE_NORMAL, 0);
        dialog.setCancelable(true);
        dialog.show(getSupportFragmentManager(), null);
    }
}
