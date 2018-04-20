package com.yjy.recyclerviewutilssample;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yjy.recyclerviewutils.adapter.SimpleAdapter;
import com.yjy.recyclerviewutils.headerfooter.HeaderFooterDecorator;
import com.yjy.recyclerviewutilssample.databinding.FragmentRecyclerViewBinding;
import com.yjy.recyclerviewutilssample.databinding.ViewItemBinding;

import java.util.ArrayList;
import java.util.UUID;

public class RecyclerViewFragment extends Fragment {

    private View mHeaderView = null;

    private View mFooterView = null;

    private RecyclerView.LayoutManager mLayoutManager = null;

    private SimpleAdapter<String, ViewItemBinding> mAdapter;

    private ArrayList<String> mStrings = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentRecyclerViewBinding binding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_recycler_view, container, false);
        binding.setAdd(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStrings.add(UUID.randomUUID().toString());
                mAdapter.replace(new ArrayList<>(mStrings));
            }
        });
        mAdapter = new SimpleAdapter<String, ViewItemBinding>() {

            @Override
            protected ViewItemBinding onCreateDataBinding(ViewGroup parent) {
                return DataBindingUtil.inflate(getLayoutInflater(), R.layout.view_item, parent, false);
            }

            @Override
            protected void onBindData(ViewItemBinding binding, String item) {
                binding.setMessage(item);
            }
        };
        mAdapter.setOnClickListener(new SimpleAdapter.OnClickListener<String>() {
            @Override
            public void onClickItem(String item) {
                if (mStrings.remove(item)) {
                    mAdapter.replace(new ArrayList<>(mStrings));
                }
            }
        });

        new HeaderFooterDecorator()
                .setAdapter(mAdapter)
                .setLayoutManager(mLayoutManager)
                .setHeaderView(mHeaderView)
                .setFooterView(mFooterView)
                .decorate(binding.list);

        return binding.getRoot();
    }

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
    }

    public void setFooterView(View footerView) {
        mFooterView = footerView;
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        mLayoutManager = layoutManager;
    }
}
