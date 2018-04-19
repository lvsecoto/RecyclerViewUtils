package com.yjy.recyclerviewutils.headerfooter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class HeaderFooterDecorator {

    private RecyclerView.Adapter mAdapter = null;

    private View mHeaderView = null;

    private View mFooterView = null;

    private RecyclerView.LayoutManager mLayoutManager = null;

    public HeaderFooterDecorator setAdapter(RecyclerView.Adapter adapter) {
        mAdapter = adapter;
        return this;
    }

    public HeaderFooterDecorator setHeaderView(View headerView) {
        mHeaderView = headerView;
        return this;
    }

    public HeaderFooterDecorator setFooterView(View footerView) {
        mFooterView = footerView;
        return this;
    }

    public HeaderFooterDecorator setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        mLayoutManager = layoutManager;
        return this;
    }

    public void decorate(RecyclerView recyclerView) {
        if (mAdapter == null) {
            throw new IllegalArgumentException("adapter is null");
        }
        if (mLayoutManager == null) {
            throw new IllegalArgumentException("layout manager is null");
        }
        if (!supportLayoutManager(mLayoutManager)) {
            throw new IllegalArgumentException("NOT support layout manager");
        }

        AdapterWrapper adapterWrapper = new AdapterWrapper(mAdapter);
        adapterWrapper.setHeaderView(mHeaderView);
        adapterWrapper.setFooterView(mFooterView);

        if (isGridLayoutManager(mLayoutManager)) {
            configGridLayoutManager(adapterWrapper, (GridLayoutManager) mLayoutManager);
        }

        recyclerView.setAdapter(adapterWrapper);
        recyclerView.setLayoutManager(mLayoutManager);
    }

    private boolean supportLayoutManager(RecyclerView.LayoutManager layoutManager) {
        return isVerticalLinearLayoutManager(layoutManager)
                || isGridLayoutManager(layoutManager);
    }

    private boolean isGridLayoutManager(RecyclerView.LayoutManager layoutManager) {
        return layoutManager instanceof GridLayoutManager;
    }

    private boolean isVerticalLinearLayoutManager(RecyclerView.LayoutManager layoutManager) {
        return (layoutManager instanceof LinearLayoutManager) &&
                (((LinearLayoutManager) layoutManager).getOrientation() == LinearLayoutManager.VERTICAL);
    }

    private void configGridLayoutManager(final AdapterWrapper adapterWrapper, final GridLayoutManager gridLayoutManager) {
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (adapterWrapper.hasHeaderView() && position == 0) {
                    return gridLayoutManager.getSpanCount();
                } else if (adapterWrapper.hasFooterView() && position == adapterWrapper.getItemCount() - 1) {
                    return gridLayoutManager.getSpanCount();
                } else {
                    return 1;
                }
            }
        });
    }
}
