package com.moh.clinicalguideline.helper.recyclerview;

import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import androidx.databinding.library.baseAdapters.BR;


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
