package com.yjy.recyclerviewutils.headerfooter;

import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

public class AdapterWrapper<H extends ViewDataBinding, F extends ViewDataBinding> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_HEADER = Integer.MAX_VALUE;

    private static final int VIEW_TYPE_FOOTER = Integer.MIN_VALUE;

    private RecyclerView.Adapter mContent;

    private ViewCreator<H> mHeaderView;

    private ViewCreator<F> mFooterView;

//    private View mHeaderView = null;
//
//    private View mFooterView = null;


    public AdapterWrapper(RecyclerView.Adapter container) {
        mContent = container;
        registerContentDataSetChangedObserver();
    }

    private void registerContentDataSetChangedObserver() {
        mContent.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                notifyDataSetChanged();
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                notifyItemRangeChanged(getWrapperPosition(positionStart), itemCount);
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount, @Nullable Object payload) {
                notifyItemRangeChanged(getWrapperPosition(positionStart), itemCount, payload);
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                notifyItemRangeInserted(getWrapperPosition(positionStart), itemCount);
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                notifyItemRangeRemoved(getWrapperPosition(positionStart), itemCount);
            }

            @Override
            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                notifyItemMoved(getWrapperPosition(fromPosition), getContentPosition(toPosition));
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        if (hasHeaderView() && position == 0) {
            return VIEW_TYPE_HEADER;
        } else if (hasFooterView() && position == getItemCount() - 1) {
            return VIEW_TYPE_FOOTER;
        } else {
            return mContent.getItemViewType(getContentPosition(position));
        }
    }

    public boolean hasFooterView() {
        return mFooterView != null;
    }

    public boolean hasHeaderView() {
        return mHeaderView != null;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                return new ViewHolder(mHeaderView.onCreateDataBinding(parent));
            case VIEW_TYPE_FOOTER:
                return new ViewHolder(mFooterView.onCreateDataBinding(parent));
            default:
                return mContent.onCreateViewHolder(parent, viewType);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (hasHeaderView() && position == 0) {
            ViewHolder viewHolder = (ViewHolder) holder;
            mHeaderView.onBindData((H) viewHolder.mViewDataBinding);
        } else if (hasFooterView() && position == getItemCount() - 1) {
            ViewHolder viewHolder = (ViewHolder) holder;
            mFooterView.onBindData((F) viewHolder.mViewDataBinding);
        } else {
            mContent.onBindViewHolder(holder, getContentPosition(position));
        }
    }

    private int getContentPosition(int wrapperPosition) {
        if (hasHeaderView()) {
            return wrapperPosition - 1;
        }
        return wrapperPosition;
    }

    private int getWrapperPosition(int actualPosition) {
        if (hasHeaderView()) {
            return actualPosition + 1;
        }
        return actualPosition;
    }

    @Override
    public int getItemCount() {
        int itemCount = mContent.getItemCount();
        if (hasHeaderView()) {
            itemCount++;
        }

        if (hasFooterView()) {
            itemCount++;
        }

        return itemCount;
    }

    public void setHeaderView(ViewCreator<H> headerView) {
        mHeaderView = headerView;
    }

    public void setFooterView(ViewCreator<F> footerView) {
        mFooterView = footerView;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ViewDataBinding mViewDataBinding;
        ViewHolder(ViewDataBinding viewDataBinding) {
            super(viewDataBinding.getRoot());
            mViewDataBinding = viewDataBinding;
        }
    }

}
