package com.moh.clinicalguideline.helper.recyclerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.moh.clinicalguideline.R;
import com.moh.clinicalguideline.core.AlgorithmDescription;
import com.moh.clinicalguideline.helper.ContentViewHelper;
import com.moh.clinicalguideline.views.algorithm.AlgorithmCardViewModel;
import com.moh.clinicalguideline.views.algorithm.AlgorithmViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainNodeAdapter extends RecyclerView.Adapter<MainNodeAdapter.ViewHolder> {
    private Context context;
    private List<AlgorithmDescription> keyNodes;
    private Map<AlgorithmDescription, List<AlgorithmCardViewModel>> algorithmDescriptions;


    public MainNodeAdapter(Context context, List<AlgorithmDescription> keyNodes, Map<AlgorithmDescription, List<AlgorithmCardViewModel>> algorithmDescriptions) {
        this.context = context;
        this.keyNodes = keyNodes;
        this.algorithmDescriptions = algorithmDescriptions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.node_item, viewGroup, false);
        return new MainNodeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        AlgorithmDescription model = keyNodes.get(i);
        ContentViewHelper cvh = new ContentViewHelper();
        viewHolder.webView.setVisibility(model.getHasDescription() ? View.VISIBLE : View.GONE);
        if (model.getHasDescription()) {
            viewHolder.webView
                    .loadDataWithBaseURL("file:///android_asset/styles/",
                            cvh.loadDataWithBaseURL(cvh.setTitleToDescription(model)),
                            "text/html;", "utf-8", null);
//            new AlgorithmViewModel()
//            viewHolder.webView.setWebViewClient(client);
            viewHolder.webView.getSettings().setJavaScriptEnabled(true);
            viewHolder.webView.getSettings().setDomStorageEnabled(true);
            viewHolder.webView.getSettings().setDefaultTextEncodingName("utf-8");
        }
        if (Objects.requireNonNull(algorithmDescriptions.get(model)).size() > 0) {
            List<AlgorithmCardViewModel> answers = new ArrayList<>();
            List<AlgorithmCardViewModel> options = new ArrayList<>();
            for (AlgorithmCardViewModel alCVM : algorithmDescriptions.get(model)) {
                if (!alCVM.isCondition()) {
                    options.add(alCVM);
                } else {
                    answers.add(alCVM);
                }
            }
            viewHolder.frameLayoutOptionsSection.setVisibility(options.size() > 0 ? View.VISIBLE : View.VISIBLE);
            AnswersAdapter answersAdapter = new AnswersAdapter(answers);
            viewHolder.answersRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            viewHolder.answersRecyclerView.setAdapter(answersAdapter);

            OptionsAdapter optionsAdapter = new OptionsAdapter(options);
            viewHolder.optionsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            viewHolder.optionsRecyclerView.setAdapter(optionsAdapter);

        }
        if (i == keyNodes.size() - 1) viewHolder.viewTimeLine.setVisibility(View.GONE);
    }

    // set child node options and answers
    @Override
    public int getItemCount() {
        return algorithmDescriptions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private WebView webView;
        private View viewTimeLine;
        private LinearLayout frameLayoutOptionsSection;
        private RecyclerView optionsRecyclerView;
        private RecyclerView answersRecyclerView;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            webView = itemView.findViewById(R.id.web_view);
            frameLayoutOptionsSection = itemView.findViewById(R.id.options_section);
            optionsRecyclerView = itemView.findViewById(R.id.recycler_view_options);
            answersRecyclerView = itemView.findViewById(R.id.recycler_view_answers);
            viewTimeLine = itemView.findViewById(R.id.time_line);
        }
    }
}
