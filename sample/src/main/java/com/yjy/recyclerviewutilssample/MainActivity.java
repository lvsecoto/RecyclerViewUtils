package com.yjy.recyclerviewutilssample;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.yjy.recyclerviewutilssample.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(
                this, R.layout.activity_main
        );

        binding.vp.setAdapter(new RecyclerViewFragmentAdapter());
        binding.vp.setOffscreenPageLimit(100);

    }

    private class RecyclerViewFragmentAdapter extends FragmentPagerAdapter {

        ArrayList<Fragment> mFragments = new ArrayList<>();

        {
            RecyclerViewFragment rf = new RecyclerViewFragment();
            rf.setHeaderView(newHeaderView());
            rf.setFooterView(newFooterView());
            rf.setLayoutManager(new LinearLayoutManager(getBaseContext()));
            mFragments.add(rf);

            rf = new RecyclerViewFragment();
            rf.setFooterView(newFooterView());
            rf.setLayoutManager(new LinearLayoutManager(getBaseContext()));
            mFragments.add(rf);

            rf = new RecyclerViewFragment();
            rf.setLayoutManager(new LinearLayoutManager(getBaseContext()));
            mFragments.add(rf);

            rf = new RecyclerViewFragment();
            rf.setFooterView(newFooterView());
            rf.setHeaderView(newHeaderView());
            rf.setLayoutManager(new GridLayoutManager(getBaseContext(), 3));
            mFragments.add(rf);

            rf = new RecyclerViewFragment();
            rf.setFooterView(newFooterView());
            rf.setLayoutManager(new GridLayoutManager(getBaseContext(), 3));
            mFragments.add(rf);

            rf = new RecyclerViewFragment();
            rf.setLayoutManager(new GridLayoutManager(getBaseContext(), 3));
            mFragments.add(rf);
        }

        private View newFooterView() {
            return View.inflate(getBaseContext(), R.layout.view_footer, null);
        }

        private View newHeaderView() {
            return View.inflate(getBaseContext(), R.layout.view_header, null);
        }

        RecyclerViewFragmentAdapter() {
            super(MainActivity.this.getSupportFragmentManager());
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }

}
