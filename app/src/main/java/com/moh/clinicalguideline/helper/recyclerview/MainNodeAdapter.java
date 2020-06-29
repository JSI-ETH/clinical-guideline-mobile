package com.moh.clinicalguideline.helper.recyclerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.moh.clinicalguideline.R;
import com.moh.clinicalguideline.core.AlgorithmDescription;
import com.moh.clinicalguideline.helper.ContentViewHelper;
import com.moh.clinicalguideline.views.algorithm.AlgorithmCardViewModel;
import com.moh.clinicalguideline.views.algorithm.AlgorithmViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainNodeAdapter extends RecyclerView.Adapter<MainNodeAdapter.ViewHolder> {
    private Context context;
    private List<AlgorithmDescription> keyNodes;
    private Map<AlgorithmDescription, List<AlgorithmCardViewModel>> algorithmDescriptions;
    private AlgorithmViewModel algorithmViewModel;
    private int tailCounter = 0;
    private static ClickListener clickHandler;
    private static int currentNode;

    public MainNodeAdapter(Context context, List<AlgorithmDescription> keyNodes, Map<AlgorithmDescription, List<AlgorithmCardViewModel>> algorithmDescriptions, AlgorithmViewModel algorithmViewModel) {
        this.context = context;
        this.keyNodes = keyNodes;
        this.algorithmDescriptions = algorithmDescriptions;
        this.algorithmViewModel = algorithmViewModel;
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
        boolean flag = false;
        AlgorithmDescription model = keyNodes.get(i);
        ContentViewHelper cvh = new ContentViewHelper();
        Map<AlgorithmDescription, Boolean> displayed = new HashMap<>();
//        viewHolder.webView.setVisibility(model.getHasDescription() ? View.VISIBLE : View.GONE);

        if (model.getHasDescription()) {
            tailCounter++;
            displayWebView(viewHolder, cvh, model, i);
            displayed.put(model, true);
            currentNode = keyNodes.indexOf(model);
        } else if (Objects.requireNonNull(algorithmDescriptions.get(model)).size() > 0) {
            displayWebView(viewHolder, cvh, algorithmDescriptions.get(model).get(0).getNode(), i);
            displayed.put(model, true);
            currentNode = keyNodes.indexOf(algorithmDescriptions.get(model).get(0).getNode());
        } else {
            displayed.put(model, false);
        }
        viewHolder.nextButton.setVisibility(model.getChildCount() > 0 ? View.VISIBLE : View.GONE);


        List<AlgorithmCardViewModel> answers = new ArrayList<>();
        List<AlgorithmCardViewModel> options = new ArrayList<>();

        for (AlgorithmDescription ald : algorithmDescriptions.keySet()) {
            if (ald.equals(model)) {
                for (AlgorithmCardViewModel alCVM : Objects.requireNonNull(algorithmDescriptions.get(model))) {
                    if (!alCVM.isCondition() && !displayed.get(model)) {
                        options.add(alCVM);
                    } else {
                        if (!displayed.get(model)) answers.add(alCVM);
                    }
                }
            }
        }
//        if (Objects.requireNonNull(algorithmDescriptions.get(model)).size() > 0) {
//            List<AlgorithmCardViewModel> answers = new ArrayList<>();
//            List<AlgorithmCardViewModel> options = new ArrayList<>();
//            for (AlgorithmCardViewModel alCVM : algorithmDescriptions.get(model)) {
//                if (!alCVM.isCondition()) {
//                    options.add(alCVM);
//                } else {
//                    answers.add(alCVM);
//                }
//            }
        viewHolder.linearLayoutOptionsSection.setVisibility(options.size() > 0 ? View.VISIBLE : View.GONE);
        AnswersAdapter answersAdapter = new AnswersAdapter(answers);
        viewHolder.answersRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        viewHolder.answersRecyclerView.setAdapter(answersAdapter);

        OptionsAdapter optionsAdapter = new OptionsAdapter(options);
        viewHolder.optionsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        viewHolder.optionsRecyclerView.setAdapter(optionsAdapter);
//
//        }
        // TODO: timeLine depends on tailCounter
        if (i == keyNodes.size() - 1) viewHolder.viewTimeLine.setVisibility(View.GONE);
    }

    private void displayWebView(ViewHolder viewHolder, ContentViewHelper cvh, AlgorithmDescription model, int i) {
        viewHolder.webView
                .loadDataWithBaseURL("file:///android_asset/styles/",
                        cvh.loadDataWithBaseURL(cvh.setTitleToDescription(model)),
                        "text/html;", "utf-8", null);
//            new AlgorithmViewModel()
        viewHolder.webView.setWebViewClient(cvh.getWebViewClient(context, algorithmViewModel));
        viewHolder.webView.getSettings().setJavaScriptEnabled(true);
        viewHolder.webView.getSettings().setDomStorageEnabled(true);
        viewHolder.webView.getSettings().setDefaultTextEncodingName("utf-8");
    }

    // set child node options and answers
    @Override
    public int getItemCount() {
        return algorithmDescriptions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private WebView webView;
        private Button nextButton;
        private View viewTimeLine;
        private LinearLayout linearLayoutOptionsSection;
        private RecyclerView optionsRecyclerView;
        private RecyclerView answersRecyclerView;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            webView = itemView.findViewById(R.id.web_view);
            nextButton = itemView.findViewById(R.id.next_button);
            linearLayoutOptionsSection = itemView.findViewById(R.id.options_section);
            optionsRecyclerView = itemView.findViewById(R.id.recycler_view_options);
            answersRecyclerView = itemView.findViewById(R.id.recycler_view_answers);
            viewTimeLine = itemView.findViewById(R.id.time_line);
            nextButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == nextButton.getId()) {
                clickHandler.selectNextChildNode(currentNode, getAdapterPosition(), v);
                clickHandler.onItemClick(getAdapterPosition(), v);
            }
        }
    }

    public List<AlgorithmDescription> getList() {
        return keyNodes;
    }

    public void setOnItemClickHandler(ClickListener clickHandler) {
        MainNodeAdapter.clickHandler = clickHandler;
    }

    private boolean nodeAvailable(AlgorithmDescription node, List<AlgorithmDescription> list) {
        return -1 == list.indexOf(node);
    }
}
