package com.moh.clinicalguideline.views.algorithm;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.moh.clinicalguideline.R;
import com.moh.clinicalguideline.helper.BaseViewHolder;
import com.moh.clinicalguideline.helper.SimpleLayoutAdapter;
import com.moh.clinicalguideline.helper.BaseAdapter;

import java.util.List;

public class CardAdapter<T> extends BaseAdapter {
    private List<T> data;
    private final int layoutId;
    private final SimpleLayoutAdapter.OnItemClickListener<T> itemClickListener;
    private Button mapbtn;
    public interface OnItemClickListener<T> {
        void onItemClick(T item);
    }

    public CardAdapter(int layoutId) {
        this.layoutId = layoutId;
        this.itemClickListener = new SimpleLayoutAdapter.OnItemClickListener<T>() {
            @Override
            public void onItemClick(T item) {

            }
        };
    }

    public CardAdapter(int layoutId, SimpleLayoutAdapter.OnItemClickListener<T> itemClickListener) {

        this.layoutId = layoutId;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        @SuppressWarnings("unchecked") T item = (T) getObjForPosition(position);
        holder.bind(item);
        mapbtn = holder.itemView.findViewById(R.id.btnDelete);
        //holder.itemView.setOnClickListener(v -> itemClickListener.onItemClick(item));
        mapbtn.setOnClickListener(v-> itemClickListener.onItemClick(item));
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
