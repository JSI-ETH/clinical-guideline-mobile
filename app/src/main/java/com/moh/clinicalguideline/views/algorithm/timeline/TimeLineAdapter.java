package com.moh.clinicalguideline.views.algorithm.timeline;
import android.view.View;
import android.widget.LinearLayout;

import com.moh.clinicalguideline.R;
import com.moh.clinicalguideline.helper.recyclerview.BaseAdapter;
import com.moh.clinicalguideline.helper.recyclerview.BaseViewHolder;

import java.util.List;

public class TimeLineAdapter<T> extends BaseAdapter {

    private List<T> nResults;
    private final int layoutId;
    private int selectedPosition;
    private View line;
    private final OnItemClickListener<T> itemClickListener;
    public interface OnItemClickListener<T> {
        void onItemClick(T item);
    }
    public TimeLineAdapter(int layoutId) {
        this.layoutId = layoutId;
        this.itemClickListener = new OnItemClickListener<T>() {
            @Override
            public void onItemClick(T item) {

            }
        };
    }

    public TimeLineAdapter(int layoutId, OnItemClickListener<T> itemClickListener) {

        this.layoutId = layoutId;
        this.itemClickListener = itemClickListener;
    }
    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        @SuppressWarnings("unchecked") T item = (T) getObjForPosition(position);
        holder.bind(item);

        line = (View) holder.itemView.findViewById(R.id.tl_line);
        //tl_s_circle

        holder.itemView.setOnClickListener(v -> {
            selectedPosition = position;
            notifyDataSetChanged();
            itemClickListener.onItemClick(item);});
        if(position==0 && nResults.size()==1) {

            line.setBackgroundResource(0);
        }
        else if(position ==0)
        {
            line.setBackgroundResource(R.drawable.line_bg_top);
        }
        else if(position < nResults.size()-1) {
            line.setBackgroundResource(R.drawable.line_bg_middle);
        }
        else {
            line.setBackgroundResource(R.drawable.line_bg_bottom);
        }
//        if(position == selectedPosition)
//        {
//            holder.itemView.findViewById(R.id.tl_s_circle).setVisibility(View.VISIBLE);
//        }
//        else {
//            holder.itemView.findViewById(R.id.tl_s_circle).setVisibility(View.GONE);
//        }


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
        this.selectedPosition = data.size()-1;
        this.notifyDataSetChanged();
    }



}
