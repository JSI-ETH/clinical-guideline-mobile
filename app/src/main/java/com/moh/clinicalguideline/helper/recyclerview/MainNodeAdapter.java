package com.moh.clinicalguideline.helper.recyclerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatButton;
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
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainNodeAdapter extends RecyclerView.Adapter<MainNodeAdapter.ViewHolder> {
    private Context context;
    private List<AlgorithmDescription> keyNodes;
    private Map<AlgorithmDescription, List<AlgorithmCardViewModel>> algorithmDescriptions;
    private AlgorithmViewModel algorithmViewModel;
    private static final String TAG = "MainNodeAdapter";
    private static ClickListener clickHandler;
    private static int currentNode;
    List<AlgorithmCardViewModel> answers = new ArrayList<>();
    List<AlgorithmCardViewModel> options = new ArrayList<>();
    private int currentItem = 0;

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
//        keyNodes = cvh.removeDuplicateNodes(keyNodes);
        int currentItem = keyNodes.size() <= i + 1 ? i : i + 1;
//        int difference = keyNodes.size() - currentItem;
//        for (int d = 0; d < difference; d++) {
        AlgorithmDescription model = keyNodes.get(currentItem);
        if (model.getChildCount() == 0) {
            viewHolder.viewTimeLine.setVisibility(View.GONE);
        }

        if (model.getHasDescription() || model.getHasTitle()) {
            displayWebView(viewHolder, cvh, model, i);
            currentNode = keyNodes.indexOf(model);
        } else if (algorithmDescriptions.get(model) != null && Objects.requireNonNull(algorithmDescriptions.get(model)).size() > 0 && !model.getHasDescription()) {
            displayWebView(viewHolder, cvh, Objects.requireNonNull(algorithmDescriptions.get(model)).get(0).getNode(), i);
            currentNode = keyNodes.indexOf(Objects.requireNonNull(algorithmDescriptions.get(model)).get(0).getNode());
        }

        answers = new ArrayList<>();
        options = new ArrayList<>();

        for (AlgorithmDescription ald : algorithmDescriptions.keySet()) {
            if (ald.getId() == model.getId() || algorithmDescriptions.keySet().size() == 1) {
                for (AlgorithmCardViewModel alCVM : Objects.requireNonNull(algorithmDescriptions.get(ald))) {
//                    if (alCVM.isCondition()) {
//                        Log.d(TAG, "onBindViewHolder: " + alCVM.getTitle());
                        if (alCVM.getId() != model.getId() && model.getChildCount() != 1
//                                && (!model.getDescription().equals(""))
//                                && (
//                                (!model.getDescription().equals("")) ||
//                                        (model.getTitle().equals("") && model.getDescription().equals("")))
                        )
                            answers.add(alCVM);
//                    }
//                    else {
//                        if (alCVM.getId() != model.getId() && model.getDescription() != null && model.getHasDescription())
//                            options.add(alCVM);
//                    }
                }
            }
        }
        addAnswersToNode(viewHolder, model);
        addOptionsToNode(viewHolder, model);
//            currentItem = keyNodes.size() <= currentItem + 1 ? currentItem + 1 : currentItem;
//        }
    }

    private void addAnswersToNode(ViewHolder viewHolder, AlgorithmDescription model) {
        AnswersAdapter answersAdapter = new AnswersAdapter(answers, model.getId());
        viewHolder.answersRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        viewHolder.answersRecyclerView.setAdapter(answersAdapter);
        answersAdapter.setOnItemClickHandler(new ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                try {
                    int id = Integer.parseInt((String) ((AppCompatButton) v).getHint()) ;
                    int ansPos =  algorithmViewModel.getOptionAnswerIndex(id,false);
                    if (ansPos != -1)
                        algorithmViewModel.removeRecyclerValues(ansPos);
                    currentItem = keyNodes.size() - 1;

                    AlgorithmDescription childNode = null;
                    for (AlgorithmDescription ald : algorithmViewModel.reverseRecyclerMap.keySet()) {
                        if (ald.getTitle() == ((AppCompatButton) v).getText()) {
                            childNode = ald;
                            break;
                        }
                    }
                algorithmViewModel
                        .feedMap(childNode, keyNodes.get(algorithmViewModel.getOptionAnswerIndex(id,true)));
                } catch (Exception ignored) {
                }
            }

            @Override
            public void selectNextChildNode(int selectedPosition, int itemPosition, View v) {

            }
        });

        if (algorithmDescriptions.size() == 2) {

        }
    }

    private void addOptionsToNode(ViewHolder viewHolder, AlgorithmDescription model) {
        OptionsAdapter optionsAdapter = new OptionsAdapter(options, model.getId());
        viewHolder.optionsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        viewHolder.optionsRecyclerView.setAdapter(optionsAdapter);
        optionsAdapter.setOnItemClickListener(new ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                try {
                    int id = Integer.parseInt((String) ((AppCompatButton) v).getHint()) ;
                    int ansPos =  algorithmViewModel.getOptionAnswerIndex(id,false);
                    if (ansPos != -1)
                        algorithmViewModel.removeRecyclerValues(ansPos);
                    currentItem = keyNodes.size() - 1;
                    algorithmViewModel
                            .feedMap(
                                    Objects.requireNonNull(algorithmDescriptions
                                            .get(keyNodes.get(currentItem)))
                                            .get(position)
                                            .getNode(), keyNodes.get(algorithmViewModel.getOptionAnswerIndex(id,true)));
                } catch (Exception ignored) {
                }
            }

            @Override
            public void selectNextChildNode(int selectedPosition, int itemPosition, View v) {
            }
        });
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
        private Integer id;
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
            int selectedPosition = getAdapterPosition();
            Log.d(TAG, "onClick: " + getAdapterPosition() + " oldPosition: " + getOldPosition() + " getLayoutPosition: " + getLayoutPosition());
            if (v.getId() == nextButton.getId()) {
                clickHandler.selectNextChildNode(currentNode, getAdapterPosition(), v);
            }
        }
    }

    public List<AlgorithmDescription> getList() {
        return keyNodes;
    }

    public void setKeyNodesList(
            List<AlgorithmDescription> list, Map<AlgorithmDescription,
            List<AlgorithmCardViewModel>> recyclerMap,
            int typeOfUpdate,
            int updateIndex) {

        this.keyNodes = list;
        if (typeOfUpdate == 0) {
            notifyItemRemoved(updateIndex);
        } else {
            notifyItemInserted(currentNode);
        }
//        this.algorithmDescriptions = recyclerMap;
//        notifyDataSetChanged();
//        }
    }

    public void setOnItemClickHandler(ClickListener clickHandler) {
        MainNodeAdapter.clickHandler = clickHandler;
    }

    private boolean isNodeAvailable(AlgorithmDescription node, List<AlgorithmDescription> list) {
        return list.indexOf(node) == -1;
    }
}
