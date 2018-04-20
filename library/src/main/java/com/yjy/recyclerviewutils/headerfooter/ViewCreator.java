package com.yjy.recyclerviewutils.headerfooter;

import android.databinding.ViewDataBinding;
import android.view.ViewGroup;

public interface ViewCreator<T extends ViewDataBinding> {
    T onCreateDataBinding(ViewGroup parent);
    void onBindData(T dataBinding);
}
