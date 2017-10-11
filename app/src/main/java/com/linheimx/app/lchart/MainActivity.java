package com.linheimx.app.lchart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

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

    public void btn4(View v) {
        startActivity(new Intent(MainActivity.this, PreviewActivity.class));
    }

    public void btn5(View v) {
        startActivity(new Intent(MainActivity.this, WarnLinesActivity.class));
    }

    public void btn6(View v) {
        startActivity(new Intent(MainActivity.this, RealTimeActivity.class));
    }

    public void btn7(View v) {
        startActivity(new Intent(MainActivity.this, TouchProcessActivity.class));
    }

    public void btn8(View v) {
        startActivity(new Intent(MainActivity.this, ScrollLoadActivity.class));
    }

    public void btn9(View v) {
        startActivity(new Intent(MainActivity.this, AnimateActivity.class));
    }

    public void btn10(View v) {
        startActivity(new Intent(MainActivity.this, NullEntityActivity.class));
    }

    public void btn11(View v) {
        startActivity(new Intent(MainActivity.this, AdvancedActivity.class));
    }
}
