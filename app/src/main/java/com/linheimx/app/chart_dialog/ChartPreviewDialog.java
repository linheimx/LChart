package com.linheimx.app.chart_dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.linheimx.app.lchart.R;
import com.linheimx.app.library.adapter.IValueAdapter;
import com.linheimx.app.library.charts.LineChart;
import com.linheimx.app.library.data.Entry;
import com.linheimx.app.library.data.Line;
import com.linheimx.app.library.data.Lines;
import com.linheimx.app.library.model.HighLight;
import com.linheimx.app.library.model.XAxis;
import com.linheimx.app.library.model.YAxis;
import com.linheimx.app.plus.LineChartPlus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LJIAN on 2017/3/9.
 */

public class ChartPreviewDialog extends DialogFragment {

    public static ChartPreviewDialog newInstance(String title) {
        ChartPreviewDialog dialog = new ChartPreviewDialog();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        dialog.setArguments(bundle);
        return dialog;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chart_god, null);

        LineChart lineChart1 = ((LineChartPlus) view.findViewById(R.id.chart1)).get_lineChart();
        LineChart godLineChart = (LineChart) view.findViewById(R.id.chart2);

        godLineChart.registObserver(lineChart1);

        setChartData(lineChart1);

        // 通知 lineChart2 数据改变了（因为lineChart1改变了）
        godLineChart.notifyDataChanged_FromOb(lineChart1);

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

        // safety check
        if (getDialog() == null)
            return;

        String title = "";
        Bundle bun = getArguments();
        if (bun != null) {
            title = bun.getString("title");
        }

        Dialog dialog = getDialog();
        dialog.setTitle(title);

        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setLayout(width, height);
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
}
