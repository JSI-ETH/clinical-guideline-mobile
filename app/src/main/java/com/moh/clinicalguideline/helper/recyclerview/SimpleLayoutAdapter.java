package com.moh.clinicalguideline.helper.recyclerview;
import android.widget.Button;

import com.moh.clinicalguideline.R;

import java.util.List;

public class SimpleLayoutAdapter<T> extends BaseAdapter {

    private List<T> nResults;
    private final int layoutId;

    private Button mapbtn;
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

        mapbtn = holder.itemView.findViewById(R.id.btnNext);
        if(mapbtn ==null) {
            holder.itemView.setOnClickListener(v -> itemClickListener.onItemClick(item));}
        else{
            mapbtn.setOnClickListener(v-> itemClickListener.onItemClick(item));
        }
    }
    @Override
    protected Object getObjForPosition(int position) {
        return nResults.get(position);
    }
    @Override
    protected int getLayoutIdForPosition(int position) {
        return layoutId;
    }
    @Override
    public int getItemCount() {
        if (nResults == null) {
            return 0;
        }
        return nResults.size();
    }

    public void setData(List<T> data) {
        this.nResults = data;
        this.notifyDataSetChanged();
    }



}
