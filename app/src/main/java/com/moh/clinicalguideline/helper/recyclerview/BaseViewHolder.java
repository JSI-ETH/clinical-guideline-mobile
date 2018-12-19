package com.moh.clinicalguideline.helper.recyclerview;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

import com.android.databinding.library.baseAdapters.BR;


public class BaseViewHolder extends RecyclerView.ViewHolder {
    private final ViewDataBinding binding;

    BaseViewHolder(ViewDataBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(Object obj) {
        binding.setVariable(BR.item, obj);
        binding.executePendingBindings();
    }
}
