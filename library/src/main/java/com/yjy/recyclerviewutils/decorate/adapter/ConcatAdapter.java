package com.yjy.recyclerviewutils.decorate.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;

public class ConcatAdapter extends Adapter {

    private final ArrayList<Adapter> mAdapters;

    /**
     * When {@link #onCreateViewHolder(ViewGroup, int)} we only know the item view type,
     * but we don't know which adapter the view type belong to.
     * So we make a map to record their relation when {@link #getItemViewType(int)}
     */
    private SparseArray<Adapter> mViewTypeToAdapter = new SparseArray<>();

    public ConcatAdapter(Adapter... adapters) {
        mAdapters = new ArrayList<>(Arrays.asList(adapters));

        for (Adapter adapter : mAdapters) {
            adapter.registerAdapterDataObserver(new InnerAdapterObserver(adapter));
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return findAdapterByViewType(viewType).onCreateViewHolder(parent, viewType);
    }

    private Adapter findAdapterByViewType(int viewType) {
        Adapter adapter = mViewTypeToAdapter.get(viewType);
        if (adapter == null) {
            throw new IllegalArgumentException("no adapter match this viewType");
        }
        return adapter;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        findAdapterByPosition(position).onBindViewHolder(holder, getPositionInAdapter(position));
    }

    @Override
    public int getItemViewType(int position) {
        Adapter adapter = findAdapterByPosition(position);
        int itemViewType = adapter.getItemViewType(getPositionInAdapter(position));
        mViewTypeToAdapter.put(itemViewType, adapter);
        return itemViewType;
    }

    @Override
    public int getItemCount() {
        int itemCount = 0;
        for (Adapter adapter : mAdapters) {
            itemCount += adapter.getItemCount();
        }
        return itemCount;
    }

    @NonNull
    private Adapter findAdapterByPosition(int position) {
        int nextAdapterStart = 0;
        for (Adapter adapter : mAdapters) {
            nextAdapterStart += adapter.getItemCount();
            if (position < nextAdapterStart) {
                return adapter;
            }
        }

        throw new IllegalArgumentException("no adapter match this position");
    }

    private int getPositionInAdapter(int position) {
        int thisAdapterStartAt = 0;
        for (Adapter adapter : mAdapters) {
            if (position < thisAdapterStartAt + adapter.getItemCount()) {
                return position - thisAdapterStartAt;
            }
            thisAdapterStartAt += adapter.getItemCount();
        }

        throw new IllegalArgumentException("no adapter match this position");
    }

    private int getAdapterStartPosition(Adapter adapter) {
        int position = 0;
        for (Adapter a : mAdapters) {
            if (a == adapter) {
                return position;
            }
            position += a.getItemCount();
        }
        return position;
    }

    private class InnerAdapterObserver extends RecyclerView.AdapterDataObserver {

        private Adapter mAdapter;

        InnerAdapterObserver(Adapter adapter) {
            mAdapter = adapter;
        }

        @Override
        public void onChanged() {
            notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            notifyItemRangeChanged(getAdapterStartPosition(mAdapter) + positionStart, itemCount);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            notifyItemRangeInserted(getAdapterStartPosition(mAdapter) + positionStart, itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            notifyItemRangeRemoved(getAdapterStartPosition(mAdapter) + positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            int adapterStartPosition = getAdapterStartPosition(mAdapter);
            notifyItemMoved(adapterStartPosition + fromPosition, adapterStartPosition + toPosition);
        }
    }

}
