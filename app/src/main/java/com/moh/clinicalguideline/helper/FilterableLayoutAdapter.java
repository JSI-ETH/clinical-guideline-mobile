package com.moh.clinicalguideline.helper;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Filter;
import android.widget.Filterable;

import com.moh.clinicalguideline.R;

import java.util.ArrayList;
import java.util.List;

public class FilterableLayoutAdapter<T> extends BaseAdapter implements Filterable {

    private List<BaseModel> data;
    private List<BaseModel> nResults;
    private final int layoutId;
    private final OnItemClickListener<BaseModel> itemClickListener;

    public CustomFilter filter;

    public interface OnItemClickListener<T> {
        void onItemClick(T item);
    }

    public FilterableLayoutAdapter(int layoutId) {
        this.layoutId = layoutId;
        this.itemClickListener = new OnItemClickListener<BaseModel>() {
            @Override
            public void onItemClick(BaseModel item) {

            }
        };
    }

    public FilterableLayoutAdapter(int layoutId, OnItemClickListener<BaseModel> itemClickListener) {

        this.layoutId = layoutId;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        @SuppressWarnings("unchecked") BaseModel item = (BaseModel) getObjForPosition(position);
        View view = holder.itemView;
        if (position % 2 == 1) {
            view.setBackgroundColor(Color.parseColor("#ffffff"));
        } else {
            view.setBackgroundColor(Color.parseColor("#f3f3f3"));
        }
        holder.bind(item);
        holder.itemView.setOnClickListener(v -> itemClickListener.onItemClick(item));
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
        this.data = (List<BaseModel>) data;
        this.nResults =   this.data;
        this.notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {

        if(filter == null)
        {
            filter=new CustomFilter();
        }

        return filter;
    }

    //INNER CLASS
    class CustomFilter extends Filter
    {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();
            if (constraint == null || constraint.length() == 0) {
                // No filter implemented we return all the list
                results.values = data;
                results.count = data.size();
            }
            else {
                List<BaseModel> nPlanetList = new ArrayList<BaseModel>();

                for (BaseModel p : data) {
                    if (p.getFilterrableText().toUpperCase()
                            .startsWith(constraint.toString().toUpperCase()))
                        nPlanetList.add(p);
                }

                results.values = nPlanetList;
                results.count = nPlanetList.size();
            }
            return results;
        }


        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            if (results.count == 0)
                notifyDataSetChanged();
            else {
                nResults =  (List<BaseModel>) results.values;
                notifyDataSetChanged();
            }
        }

    }


}
