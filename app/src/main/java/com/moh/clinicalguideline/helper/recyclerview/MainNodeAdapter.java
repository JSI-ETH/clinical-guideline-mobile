package com.moh.clinicalguideline.helper.recyclerview;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
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
    private static final String TAG = "MainNodeAdapter";
    private static ClickListener clickHandler;
    private static int currentNode;
    private static int selectedPosition;
    private Map<Integer, Boolean> displayed = new HashMap<>();
    List<AlgorithmCardViewModel> answers = new ArrayList<>();
    List<AlgorithmCardViewModel> options = new ArrayList<>();

    public MainNodeAdapter(Context context,
                           List<AlgorithmDescription> keyNodes,
                           Map<AlgorithmDescription, List<AlgorithmCardViewModel>> algorithmDescriptions,
                           AlgorithmViewModel algorithmViewModel) {
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
        ContentViewHelper cvh = new ContentViewHelper();
        keyNodes = cvh.removeDuplicateNodes(keyNodes);
        int currentItem = keyNodes.size() <= i + 1 ? i : i + 1;
        AlgorithmDescription model = keyNodes.get(currentItem);

        if (model.getHasDescription()) {
            tailCounter++;
            displayWebView(viewHolder, cvh, model, i);
            displayed.put(model.getId(), true);
            currentNode = keyNodes.indexOf(model);
        } else if (algorithmDescriptions.get(model) != null && Objects.requireNonNull(algorithmDescriptions.get(model)).size() > 0 && !model.getHasDescription()) {
            displayWebView(viewHolder, cvh, algorithmDescriptions.get(model).get(0).getNode(), i);
            displayed.put(model.getId(), true);
            currentNode = keyNodes.indexOf(algorithmDescriptions.get(model).get(0).getNode());
        } else {
            if (!displayed.isEmpty() && displayed.get(model.getId())) return;
            // todo
        }
        // TODO: Create button programmatically


        List<AlgorithmCardViewModel> answers = new ArrayList<>();
        List<AlgorithmCardViewModel> options = new ArrayList<>();

        for (AlgorithmDescription ald : algorithmDescriptions.keySet()) {
            if (ald.equals(model)) {
                for (AlgorithmCardViewModel alCVM : Objects.requireNonNull(algorithmDescriptions.get(model))) {
                    if (alCVM.isCondition()) {
                        Log.d(TAG, "onBindViewHolder: " + alCVM.getTitle());
                        answers.add(alCVM);
                    } else {
                        options.add(alCVM);
                    }
                }
            }
        }

        AnswersAdapter answersAdapter = new AnswersAdapter(answers, this);
        viewHolder.answersRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        viewHolder.answersRecyclerView.setAdapter(answersAdapter);
        answersAdapter.setOnItemClickHandler(new ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                rearrangeRecyclerView(position, currentItem);
                algorithmViewModel.feedMapChild(algorithmDescriptions.get(keyNodes.get(rearrangeRecyclerView(position, currentItem))).get(position).getNode(), MainNodeAdapter.this);
                Log.d(TAG, "onItemClick: " + answers.get(position).getId() + "\tcurrent item position: " + currentItem);
            }

            @Override
            public void selectNextChildNode(int selectedPosition, int itemPosition, View v) {

            }
        });

        OptionsAdapter optionsAdapter = new OptionsAdapter(options);
        viewHolder.optionsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        viewHolder.optionsRecyclerView.setAdapter(optionsAdapter);
        optionsAdapter.setOnItemClickListener(new ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.d(TAG, "onItemClick: " + options.get(position).getId() + "\tcurrent item position: " + currentItem);
                algorithmViewModel.feedMapChild(algorithmDescriptions.get(keyNodes.get(rearrangeRecyclerView(position, currentItem))).get(position).getNode() /*options.get(position).getNode()*/, MainNodeAdapter.this);
            }

            @Override
            public void selectNextChildNode(int selectedPosition, int itemPosition, View v) {

            }
        });
//
//        }
        // TODO: timeLine depends on tailCounter
        // if node has no options or answers tail is gone
//      viewHolder.viewTimeLine.setVisibility(currentItem == keyNodes.size() - 1 ? View.GONE : View.VISIBLE);
    }

    private int rearrangeRecyclerView(int position, int currentItem) {
        if(currentItem == 4){
            return 3;
        }
        if (keyNodes.size() - 1 > currentItem) {
            for (int i = currentItem; i < keyNodes.size(); i++) keyNodes.remove(i);
        }
        if (keyNodes.size() - 1 == currentItem) {
            return currentItem;
        } else {
            return currentItem - 1;
        }
    }

    private void displayWebView(ViewHolder viewHolder, ContentViewHelper cvh, AlgorithmDescription model, int i) {
        WebView webView = viewHolder.webView;
        webView.loadDataWithBaseURL("file:///android_asset/styles/",
                cvh.loadDataWithBaseURL(cvh.setTitleToDescription(model)),
                "text/html;", "utf-8", null);
        webView.setWebViewClient(cvh.getWebViewClient(context, algorithmViewModel));
        webView.getSettings().setJavaScriptEnabled(false);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        viewHolder.nextButton.setVisibility(model.getChildCount() == 1 ? View.VISIBLE : View.GONE);
        webView.setBackgroundColor(model.isUrgent() ? context.getResources().getColor(R.color.very_light_red) : context.getResources().getColor(R.color.white));
        viewHolder.cardView.setCardBackgroundColor(model.isUrgent() ? context.getResources().getColor(R.color.very_light_red) : context.getResources().getColor(R.color.white));
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
        private CardView cardView;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);

            webView = itemView.findViewById(R.id.web_view);
            nextButton = itemView.findViewById(R.id.next_button);
            linearLayoutOptionsSection = itemView.findViewById(R.id.options_section);
            optionsRecyclerView = itemView.findViewById(R.id.recycler_view_options);
            answersRecyclerView = itemView.findViewById(R.id.recycler_view_answers);
            viewTimeLine = itemView.findViewById(R.id.time_line);
            cardView = itemView.findViewById(R.id.card_view_webView_holder);
            nextButton.setOnClickListener(this);
            optionsRecyclerView.setOnClickListener(this);
            answersRecyclerView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            selectedPosition = getAdapterPosition();
            Log.d(TAG, "onClick: " + getAdapterPosition() + " oldPosition: " + getOldPosition() + " getLayoutPosition: " + getLayoutPosition());
            if (v.getId() == nextButton.getId()) {
                clickHandler.selectNextChildNode(currentNode, getAdapterPosition(), v);
            }
        }
    }

    public List<AlgorithmDescription> getList() {
        return keyNodes;
    }

    public void setKeyNodesList(List<AlgorithmDescription> list) {
        Handler mainHandler = new Handler(context.getMainLooper());
        this.keyNodes = list;
        Runnable myRunnable = () -> notifyItemInserted(currentNode);
        mainHandler.post(myRunnable);

        // TODO: request focus down
    }

    public void setOnItemClickHandler(ClickListener clickHandler) {
        MainNodeAdapter.clickHandler = clickHandler;
    }

    private boolean isNodeAvailable(AlgorithmDescription node, List<AlgorithmDescription> list) {
        return list.indexOf(node) == -1;
    }
}
