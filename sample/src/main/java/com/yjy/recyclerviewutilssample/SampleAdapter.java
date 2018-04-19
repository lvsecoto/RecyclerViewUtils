package com.yjy.recyclerviewutilssample;

import android.annotation.SuppressLint;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yjy.recyclerviewutilssample.databinding.ViewItemBinding;

import java.util.ArrayList;

class SampleAdapter extends RecyclerView.Adapter<SampleAdapter.ViewHolder> {

    private Context mContext;

    private ArrayList<String> mStrings = new ArrayList<>();

    private OnClickListener mOnClickListener;

    private int mVersion;

    @SuppressLint("StaticFieldLeak")
    public void replace(final ArrayList<String> strings) {
        mVersion++;

        new AsyncTask<Void, Void, DiffUtil.DiffResult>() {

            final int startVersion = mVersion;

            final ArrayList<String> oldItem = mStrings;

            @Override
            protected DiffUtil.DiffResult doInBackground(Void... voids) {

                return DiffUtil.calculateDiff(new DiffUtil.Callback() {
                    @Override
                    public int getOldListSize() {
                        return oldItem.size();
                    }

                    @Override
                    public int getNewListSize() {
                        return strings.size();
                    }

                    @Override
                    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                        return oldItem.get(oldItemPosition).equals(strings.get(newItemPosition));
                    }

                    @Override
                    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                        return false;
                    }
                });
            }

            @Override
            protected void onPostExecute(DiffUtil.DiffResult diffResult) {
                if (mVersion > startVersion) {
                    return;
                }

                diffResult.dispatchUpdatesTo(SampleAdapter.this);
                mStrings = new ArrayList<>(strings);
            }
        }.execute();
    }

    SampleAdapter(Context context, OnClickListener onClickListener) {
        mContext = context;
        mOnClickListener = onClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.view_item, parent, false);
        return new ViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.mBinding.setMessage(mStrings.get(position));
        holder.item = mStrings.get(position);
        holder.mBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClickListener.onClickItem(holder.item);
            }
        });
        holder.mBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mStrings.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final ViewItemBinding mBinding;

        String item;

        ViewHolder(View view, ViewItemBinding binding) {
            super(view);
            mBinding = binding;
        }
    }

    interface OnClickListener {
        void onClickItem(String item);
    }
}
