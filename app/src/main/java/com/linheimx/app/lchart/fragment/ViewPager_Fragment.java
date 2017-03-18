package com.linheimx.app.lchart.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.linheimx.app.lchart.R;

import java.util.ArrayList;
import java.util.List;


public class ViewPager_Fragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_view_pager_, container, false);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);

        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(new Adapter(getChildFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

        return view;
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
