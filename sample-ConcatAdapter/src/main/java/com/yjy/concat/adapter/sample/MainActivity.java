package com.yjy.concat.adapter.sample;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import com.yjy.concat.adapter.sample.databinding.ActivityMainBinding;
import com.yjy.concat.adapter.sample.databinding.ViewItemBinding;
import com.yjy.concat.adapter.sample.databinding.ViewItemFooterBinding;
import com.yjy.concat.adapter.sample.databinding.ViewItemHeaderBinding;
import com.yjy.recyclerviewutils.adapter.SimpleAdapter;
import com.yjy.recyclerviewutils.decorate.adapter.ConcatAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;

    private ArrayList<String> mHeaderItem;

    private SimpleAdapter<String, ViewItemHeaderBinding> mHeaderAdapter;

    private SimpleAdapter<String, ViewItemBinding> mItemsAdapter;

    private ArrayList<String> mItem;

    private SimpleAdapter<String, ViewItemFooterBinding> mFooterAdapter;

    private ArrayList<String> mFooterItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mHeaderAdapter = new SimpleAdapter<String, ViewItemHeaderBinding>() {
            @Override
            protected int getLayoutId(int position) {
                return R.layout.view_item_header;
            }

            @Override
            protected void onBindData(ViewItemHeaderBinding binding, String item) {
                binding.message.setText(item);
            }
        };
        mHeaderItem = createItem(2);
        mHeaderAdapter.replace(mHeaderItem);
        mBinding.header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHeaderItem = new ArrayList<>(mHeaderItem);
                mHeaderItem.add(UUID.randomUUID().toString());
                mHeaderAdapter.replace(mHeaderItem);
            }
        });

        mItemsAdapter = new SimpleAdapter<String, ViewItemBinding>() {
            @Override
            protected int getLayoutId(int position) {
                return R.layout.view_item;
            }

            @Override
            protected void onBindData(ViewItemBinding binding, String item) {
                binding.message.setText(item);
            }
        };
        mItem = createItem(20);
        mItemsAdapter.replace(mItem);
        mBinding.body.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItem = new ArrayList<>(mItem);
                mItem.add(UUID.randomUUID().toString());
                mItemsAdapter.replace(mItem);
            }
        });

        mFooterAdapter = new SimpleAdapter<String, ViewItemFooterBinding>() {
            @Override
            protected int getLayoutId(int position) {
                return R.layout.view_item_footer;
            }

            @Override
            protected void onBindData(ViewItemFooterBinding binding, String item) {
                binding.message.setText(item);
            }
        };
        mFooterItems = createItem(3);
        mFooterAdapter.replace(mFooterItems);
        mBinding.footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFooterItems = new ArrayList<>(mFooterItems);
                mFooterItems.add(UUID.randomUUID().toString());
                mFooterAdapter.replace(mFooterItems);
            }
        });

        mBinding.list.setAdapter(new ConcatAdapter(mHeaderAdapter, mItemsAdapter, mFooterAdapter));
        mBinding.list.setLayoutManager(new GridLayoutManager(this, 3));
    }

    private ArrayList<String> createItem(int number) {
        ArrayList<String> items = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            items.add(UUID.randomUUID().toString());
        }
        return items;
    }
}
