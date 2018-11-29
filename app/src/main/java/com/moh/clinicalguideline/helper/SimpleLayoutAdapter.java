package com.moh.clinicalguideline.helper;

import java.util.List;

public class SimpleLayoutAdapter<T> extends BaseAdapter {
    private List<T> data;
    private final int layoutId;
    private final OnItemClickListener<T> itemClickListener;

    public interface OnItemClickListener<T> {
        void onItemClick(T item);
    }

    public SimpleLayoutAdapter(int layoutId) {
        this.layoutId = layoutId;
        this.itemClickListener = new OnItemClickListener<T>() {
            @Override
            public void onItemClick(T item) {

            }
        };
    }

    public SimpleLayoutAdapter(int layoutId, OnItemClickListener<T> itemClickListener) {

        this.layoutId = layoutId;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        @SuppressWarnings("unchecked") T item = (T) getObjForPosition(position);
        holder.bind(item);
        holder.itemView.setOnClickListener(v -> itemClickListener.onItemClick(item));
    }

    @Override
    protected Object getObjForPosition(int position) {
        return data.get(position);
    }

    @Override
    protected int getLayoutIdForPosition(int position) {
        return layoutId;
    }

    @Override
    public int getItemCount() {
        if (data == null) {
            return 0;
        }
        return data.size();
    }

    public void setData(List<T> data) {
        this.data = data;
        this.notifyDataSetChanged();
    }
}
