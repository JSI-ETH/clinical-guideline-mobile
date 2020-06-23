package com.moh.clinicalguideline.helper.recyclerview;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.moh.clinicalguideline.R;
import com.moh.clinicalguideline.core.AlgorithmDescription;
import com.moh.clinicalguideline.databinding.NodeItemBinding;
import com.moh.clinicalguideline.views.algorithm.AlgorithmCardViewModel;

import java.util.List;

public class MainNodeAdapter extends RecyclerView.Adapter<MainNodeAdapter.ViewHolder> {
    private Context context;
    private List<AlgorithmCardViewModel> optionNodes;
    private List<AlgorithmDescription> algorithmDescriptions;


    public MainNodeAdapter(Context context, List<AlgorithmDescription> algorithmDescriptions) {
        this.context = context;
        this.algorithmDescriptions = algorithmDescriptions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        NodeItemBinding nodeItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.getContext()), R.layout.node_item, viewGroup, false);

        return new MainNodeAdapter.ViewHolder(nodeItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        AlgorithmDescription model = algorithmDescriptions.get(i);
        viewHolder.nodeItemBinding.setNode(model);

        viewHolder.optionsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    // set child node options and answers
    public void setOptionsNode(List<AlgorithmCardViewModel> childOptions) {
        this.optionNodes = childOptions;
    }

    @Override
    public int getItemCount() {
        return algorithmDescriptions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        NodeItemBinding nodeItemBinding;
        private WebView webView;
        private RecyclerView optionsRecyclerView;
        private RecyclerView answersRecyclerView;

        private ViewHolder(@NonNull NodeItemBinding itemView) {
            super(itemView.getRoot());
            nodeItemBinding = itemView;
        }
    }
}
