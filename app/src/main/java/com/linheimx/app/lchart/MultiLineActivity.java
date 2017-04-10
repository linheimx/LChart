package com.linheimx.app.lchart;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.linheimx.app.library.adapter.IValueAdapter;
import com.linheimx.app.library.charts.LineChart;
import com.linheimx.app.library.data.Entry;
import com.linheimx.app.library.data.Line;
import com.linheimx.app.library.data.Lines;
import com.linheimx.app.library.model.HighLight;
import com.linheimx.app.library.model.XAxis;
import com.linheimx.app.library.model.YAxis;
import com.linheimx.app.library.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MultiLineActivity extends AppCompatActivity {

    private final static int LINE_NUM = 3;

    LineChart _lineChart;
    SeekBar _SeekBar;
    TextView _tvLineNum;

    Line.CallBack_OnEntryClick onEntryClick = new Line.CallBack_OnEntryClick() {
        @Override
        public void onEntry(Line line, Entry entry) {
            Toast.makeText(MultiLineActivity.this, line.getName() + "    \r\n" + entry.toString(), Toast.LENGTH_SHORT).show();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_line);

        getSupportActionBar().setTitle("折线图：多条折线");

        _lineChart = (LineChart) findViewById(R.id.chart);
        _SeekBar = (SeekBar) findViewById(R.id.sb_line_more);
        _tvLineNum = (TextView) findViewById(R.id.tv_line_nums);
        CheckBox cb = (CheckBox) findViewById(R.id.cb_cb);

        setChartData(_lineChart, LINE_NUM);

        // 1. 折线的数目
        _tvLineNum.setText("折线的数目:" + LINE_NUM);
        _SeekBar.setProgress(LINE_NUM);

        _SeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                _tvLineNum.setText("折线的数目:" + progress);
                setChartData(_lineChart, progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // 2. 点击折线上的点 ，回调
        cb.setChecked(true);
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Lines lines = _lineChart.getlines();
                for (Line line : lines.getLines()) {
                    if (isChecked) {
                        line.setOnEntryClick(onEntryClick);
                    } else {
                        line.setOnEntryClick(null);
                    }
                }
            }
        });
    }

    private void setChartData(LineChart lineChart, int lineCount) {

        // 高亮
        HighLight highLight = lineChart.get_HighLight();
        highLight.setEnable(true);// 启用高亮显示  默认为启用状态，每条折线图想要获取点击回调，highlight需要启用
        highLight.setxValueAdapter(new IValueAdapter() {
            @Override
            public String value2String(double value) {
                return "X:" + value;
            }
        });
        highLight.setyValueAdapter(new IValueAdapter() {
            @Override
            public String value2String(double value) {
                return "Y:" + Math.round(value);
            }
        });

        // x,y轴上的单位
        XAxis xAxis = lineChart.get_XAxis();
        xAxis.set_unit("s");

        YAxis yAxis = lineChart.get_YAxis();
        yAxis.set_unit("m");

        Lines lines = new Lines();

        for (int i = 0; i < lineCount; i++) {

            // 线的颜色
            int color = Color.argb(255,
                    (new Double(Math.random() * 256)).intValue(),
                    (new Double(Math.random() * 256)).intValue(),
                    (new Double(Math.random() * 256)).intValue());

            Line line = createLine(i, color);
            lines.addLine(line);
        }

        lineChart.setLines(lines);
    }


    private Line createLine(int order, int color) {

        final Line line = new Line();
        List<Entry> list = new ArrayList<>();

        Random random = new Random();
        for (int i = 0; i < 10 + order; i++) {
            double x = i;
            double y = random.nextDouble() * 100;
            list.add(new Entry(x, y));
        }

        line.setEntries(list);
        line.setDrawLegend(true);//设置启用绘制图例
        line.setLegendWidth((int)Utils.dp2px(60));//设置图例的宽
//        line.setLegendHeight((int)Utils.dp2px(60));//设置图例的高
//        line.setLegendTextSize((int)Utils.dp2px(19));//设置图例上的字体大小
        line.setName("_line:" + order);
        line.setLineColor(color);
        line.setOnEntryClick(onEntryClick);

        return line;
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
