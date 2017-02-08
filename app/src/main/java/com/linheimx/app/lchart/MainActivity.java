package com.linheimx.app.lchart;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.linheimx.app.library.charts.LineChart;
import com.linheimx.app.library.charts.LineChartPlus;
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
    }

    public void btn1(View v) {
        startActivity(new Intent(MainActivity.this, LineChartActivity.class));
    }

    public void btn2(View v) {
        startActivity(new Intent(MainActivity.this, MultiLineActivity.class));
    }

    public void btn3(View v) {
        startActivity(new Intent(MainActivity.this, LineChartPlusActivity.class));
    }

    public void btn4(View v) {
        startActivity(new Intent(MainActivity.this, GodActivity.class));
    }


}
