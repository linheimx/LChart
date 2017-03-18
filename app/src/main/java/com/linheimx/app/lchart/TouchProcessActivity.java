package com.linheimx.app.lchart;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.linheimx.app.lchart.fragment.LineChartFragment;
import com.linheimx.app.lchart.fragment.ScrollView_Fragment;
import com.linheimx.app.lchart.fragment.ViewPager_Fragment;

import java.util.ArrayList;
import java.util.List;

public class TouchProcessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch_process);

        getSupportActionBar().setTitle("滑动冲突的处理");


    }


    public void btn_vp(View view) {

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.content, new ViewPager_Fragment())
                .commit();
    }

    public void btn_sv(View view) {

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.content, new ScrollView_Fragment())
                .commit();
    }

}
