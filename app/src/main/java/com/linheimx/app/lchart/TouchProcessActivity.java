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

import java.util.ArrayList;
import java.util.List;

public class TouchProcessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch_process);

        getSupportActionBar().setTitle("滑动冲突");

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(new Adapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
    }


    class Adapter extends FragmentStatePagerAdapter {

        List<Fragment> list;

        public Adapter(FragmentManager fm) {
            super(fm);

            list = new ArrayList<>();
            list.add(new LineChartFragment());
            list.add(new LineChartFragment());
            list.add(new LineChartFragment());
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "chart " + position;
        }
    }
}
