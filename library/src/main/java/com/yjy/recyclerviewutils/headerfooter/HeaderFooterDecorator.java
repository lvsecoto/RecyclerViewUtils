package com.yjy.recyclerviewutils.headerfooter;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class HeaderFooterDecorator<H extends ViewDataBinding, F extends ViewDataBinding> {

    private RecyclerView.Adapter mAdapter = null;

    private ViewCreator<H> mHeaderView = null;

    private ViewCreator<F> mFooterView = null;

    private RecyclerView.LayoutManager mLayoutManager = null;

    public HeaderFooterDecorator<H,F> setAdapter(RecyclerView.Adapter adapter) {
        mAdapter = adapter;
        return this;
    }

    public HeaderFooterDecorator<H,F> setHeaderView(ViewCreator<H> headerView) {
        mHeaderView = headerView;
        return this;
    }

    public HeaderFooterDecorator<H,F> setFooterView(ViewCreator<F> footerView) {
        mFooterView = footerView;
        return this;
    }

    public HeaderFooterDecorator<H,F> setLayoutManager(RecyclerView.LayoutManager layoutManager) {
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

        AdapterWrapper<H,F> adapterWrapper = new AdapterWrapper<>(mAdapter);
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
