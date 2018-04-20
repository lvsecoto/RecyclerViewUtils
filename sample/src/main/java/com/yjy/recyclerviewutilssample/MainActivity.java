package com.yjy.recyclerviewutilssample;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.yjy.recyclerviewutils.headerfooter.ViewCreator;
import com.yjy.recyclerviewutilssample.databinding.ActivityMainBinding;
import com.yjy.recyclerviewutilssample.databinding.ViewFooterBinding;
import com.yjy.recyclerviewutilssample.databinding.ViewHeaderBinding;

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

        private ViewCreator<ViewFooterBinding> newFooterView() {
            return new ViewCreator<ViewFooterBinding>(){

                @Override
                public ViewFooterBinding onCreateDataBinding(ViewGroup parent) {
                    return DataBindingUtil.inflate(
                            LayoutInflater.from(parent.getContext()), R.layout.view_footer, parent,false);
                }

                @Override
                public void onBindData(ViewFooterBinding dataBinding) {

                }
            };
        }

        private ViewCreator<ViewHeaderBinding> newHeaderView() {
            return new ViewCreator<ViewHeaderBinding>(){

                @Override
                public ViewHeaderBinding onCreateDataBinding(ViewGroup parent) {
                    return DataBindingUtil.inflate(
                            LayoutInflater.from(parent.getContext()), R.layout.view_header, parent,false);
                }

                @Override
                public void onBindData(ViewHeaderBinding dataBinding) {

                }
            };
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
