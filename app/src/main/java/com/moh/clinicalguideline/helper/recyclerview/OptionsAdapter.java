package com.moh.clinicalguideline.helper.recyclerview;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.moh.clinicalguideline.R;
import com.moh.clinicalguideline.views.algorithm.AlgorithmCardViewModel;

import java.util.List;

public class OptionsAdapter extends RecyclerView.Adapter<OptionsAdapter.ViewHolder> {
    List<AlgorithmCardViewModel> algorithmCardViewModels;
    private static ClickListener clickListener;
    private int parentId;

    public OptionsAdapter(List<AlgorithmCardViewModel> algorithmCardViewModels, Integer pId) {
        this.algorithmCardViewModels = algorithmCardViewModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.options_list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        AlgorithmCardViewModel model = algorithmCardViewModels.get(i);
        boolean urgent = model.getUrgent();
        viewHolder.constraintLayout.setBackgroundResource(urgent ? R.drawable.urgent_card : R.drawable.nonurgent_card);
        viewHolder.textViewTitle.setVisibility(model.getHasTitle() ? View.VISIBLE : View.GONE);
        if (model.getHasTitle()) viewHolder.textViewTitle.setText(model.getTitle());
        viewHolder.textViewContent.setVisibility(model.getHasDescription() ? View.VISIBLE : View.GONE);
        if (model.getHasDescription())
            viewHolder.textViewContent.setText(Html.fromHtml(model.getDescription()));
        viewHolder.buttonNext.setBackgroundResource(urgent ? R.drawable.curved_button_urgent : R.drawable.curved_button_normal);
        viewHolder.buttonNext.setHint(String.valueOf(this.parentId));
    }

    @Override
    public int getItemCount() {
        return algorithmCardViewModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ConstraintLayout constraintLayout;
        TextView textViewTitle, textViewContent;
        Button buttonNext;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            constraintLayout = itemView.findViewById(R.id.constraint_layout_bg);
            textViewTitle = itemView.findViewById(R.id.tvTitle);
            textViewContent = itemView.findViewById(R.id.tvContent);
            buttonNext = itemView.findViewById(R.id.btnNextOptions);
            buttonNext.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }
    }



    public void setOnItemClickListener(ClickListener clickListener) {
        OptionsAdapter.clickListener = clickListener;
    }
}
