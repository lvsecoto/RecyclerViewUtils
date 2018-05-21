package com.yjy.recyclerviewutils.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.AsyncTask;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * This adapter supports {@link java.util.ArrayList} data and data binding and do
 * {@link DiffUtil#calculateDiff(DiffUtil.Callback)} in background and notify the item changing
 *
 * @param <T> type of adapter item
 * @param <V> type of view dataBinding
 */
public abstract class SimpleAdapter<T, V extends ViewDataBinding> extends RecyclerView.Adapter<SimpleAdapter.ViewHolder<V>> {

    private ArrayList<T> mItems = new ArrayList<>();

    private OnClickListener<T> mOnClickListener;

    private int mVersion;

    /**
     * To get this position layout id
     * <p>
     * <p></p>
     * <p>This layout id is use for creating a data binding.
     * And it will be assigned to item view type</p>
     */
    protected abstract int getLayoutId(int position);

    /**
     * To bind data to dataBinding
     */
    protected abstract void onBindData(V binding, T item);

    /**
     * Replace all items the adapter keep.
     * <P></P>
     * <p>DiffUtils thread run in background. You <b>can't</b> modify the items you passed after,
     * or it will throw an illegal argument exception; Called attach on a child which is not detached</p>
     */
    public void replace(ArrayList<T> newItems) {
        mVersion++;
        new CalculateDiffAsyncTask<>(mItems, newItems, this).execute();
    }

    @NonNull
    @Override
    public ViewHolder<V> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        V dataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                viewType, parent, false);
        return new ViewHolder<>(dataBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder<V> holder, int position) {
        final T item = mItems.get(position);

        if (mOnClickListener != null) {
            holder.mBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnClickListener.onClickItem(item);
                }
            });
        }

        onBindData(holder.mBinding, item);

        // update immediate, to prevent the view flashing
        holder.mBinding.executePendingBindings();
    }

    @Override
    public int getItemViewType(int position) {
        return getLayoutId(position);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void setOnClickListener(@Nullable OnClickListener<T> onClickListener) {
        mOnClickListener = onClickListener;
    }

    static class ViewHolder<V extends ViewDataBinding> extends RecyclerView.ViewHolder {

        final V mBinding;

        ViewHolder(V dataBinding) {
            super(dataBinding.getRoot());
            mBinding = dataBinding;
        }
    }

    public interface OnClickListener<T> {

        void onClickItem(T item);
    }

    private static class CalculateDiffAsyncTask<T> extends AsyncTask<Void, Void, DiffUtil.DiffResult> {

        private final ArrayList<T> mOldItems;

        private final ArrayList<T> mNewItems;

        private final WeakReference<SimpleAdapter> mListAdapterWeakReference;

        private int mStartVersion;

        @MainThread
        CalculateDiffAsyncTask(ArrayList<T> oldItems,
                               ArrayList<T> newItems,
                               SimpleAdapter simpleAdapter) {
            mOldItems = oldItems;
            mNewItems = newItems;
            mListAdapterWeakReference = new WeakReference<>(simpleAdapter);
            mStartVersion = simpleAdapter.mVersion;
        }

        @Override
        protected DiffUtil.DiffResult doInBackground(Void... voids) {

            return DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mOldItems.size();
                }

                @Override
                public int getNewListSize() {
                    return mNewItems.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mOldItems.get(oldItemPosition).equals(
                            mNewItems.get(newItemPosition));
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    return false;
                }
            });
        }

        @Override
        protected void onPostExecute(DiffUtil.DiffResult diffResult) {
            SimpleAdapter simpleAdapter = mListAdapterWeakReference.get();

            if (simpleAdapter == null) {
                return;
            }

            if (simpleAdapter.mVersion > mStartVersion) {
                return;
            }

            diffResult.dispatchUpdatesTo(simpleAdapter);

            simpleAdapter.mItems = mNewItems;
        }
    }
}
