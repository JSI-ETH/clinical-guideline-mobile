package com.moh.clinicalguideline.helper.recyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.moh.clinicalguideline.R;
import com.moh.clinicalguideline.views.algorithm.AlgorithmCardViewModel;

import java.util.List;

public class AnswersAdapter extends RecyclerView.Adapter<AnswersAdapter.ViewHolder> {
    private List<AlgorithmCardViewModel> answersNode;
    private static ClickListener clickHandler;
    private int parentId;

    public AnswersAdapter(List<AlgorithmCardViewModel> answersNode, Integer pId) {
        this.answersNode = answersNode;
        this.parentId = pId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.answers_list_item, viewGroup, false);
        return new AnswersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        AlgorithmCardViewModel model = answersNode.get(i);
        viewHolder.answer.setText(model.getTitle());
        viewHolder.answer.setHint( String.valueOf(this.parentId));
    }

    @Override
    public int getItemCount() {
        return answersNode.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Button answer;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            answer = itemView.findViewById(R.id.btnAnswer);
            answer.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickHandler.onItemClick(getAdapterPosition(),v);
        }
    }

    public void setOnItemClickHandler(ClickListener clickHandler) {
        AnswersAdapter.clickHandler = clickHandler;
    }
}
