package com.moh.clinicalguideline.helper;
import android.util.Log;
import android.widget.Filter;
import android.widget.Filterable;

import com.moh.clinicalguideline.data.entities.Node;
import com.moh.clinicalguideline.data.entities.NodeDescription;
import com.moh.clinicalguideline.helper.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class SimpleLayoutAdapter<T> extends BaseAdapter implements Filterable {

    private List<NodeDescription> data;
    private List<NodeDescription> descriptions;
    private final int layoutId;
    private final OnItemClickListener<T> itemClickListener;

    public CustomFilter filter;

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
        return descriptions.size();
    }

    public void setData(List<NodeDescription> data) {
        this.data = data;
        this.notifyDataSetChanged();
        this.descriptions=data;
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


            String charString = constraint.toString();
            if (charString.isEmpty()) {
                descriptions = data;
            } else {
                List<NodeDescription> filteredList = new ArrayList<>();
                for (NodeDescription nd : data) {

                    if (nd.getTitle().toUpperCase().contains(charString.trim().toUpperCase())) {
                        filteredList.add(nd);
                        Log.d("simpleLayoutAdapter", "found it!" + nd.getTitle());
                    }
                }

                descriptions = filteredList;
                for (NodeDescription i : descriptions) {
                    Log.d("simpleLayoutAdapter", " " + i.getTitle());
                }

            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = descriptions;
            return filterResults;
        }


        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            descriptions=(List<NodeDescription>) results.values;
            notifyDataSetChanged();
        }

    }


}
