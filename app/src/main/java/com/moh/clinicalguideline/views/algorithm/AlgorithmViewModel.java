package com.moh.clinicalguideline.views.algorithm;

import android.annotation.SuppressLint;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.Bindable;
import android.util.Log;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.moh.clinicalguideline.BR;
import com.moh.clinicalguideline.core.AlgorithmDescription;
import com.moh.clinicalguideline.core.RecyclerUpdate;
import com.moh.clinicalguideline.helper.recyclerview.MainNodeAdapter;
import com.moh.clinicalguideline.helper.view.BaseViewModel;
import com.moh.clinicalguideline.repository.NodeRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class AlgorithmViewModel extends BaseViewModel<AlgorithmNavigator> {

    private final NodeRepository nodeRepository;
    private MutableLiveData<AlgorithmDescription> node;
    private MutableLiveData<String> footerText;
    private List<AlgorithmDescription> nodeList;
    private  MutableLiveData<List<AlgorithmDescription>> liveNodeList;
    private List<AlgorithmCardViewModel> nodeOptions;
    private OnNodeSelectedListener onNodeSelectedListener;
    private MutableLiveData<String> title = new MutableLiveData<>();
    private String symptomTitle = "";
    private Map<AlgorithmDescription, List<AlgorithmCardViewModel>> recyclerMap;
    // option/ans map to node
    public Map< AlgorithmDescription, AlgorithmDescription> reverseRecyclerMap;
    private RecyclerUpdate recyclerUpdate;

    private MutableLiveData<AlgorithmDescription> algorithmNodeDescription;
    private MutableLiveData<Double> selectedPageId;
    private boolean isSingleNode;
    private List<Integer> nodeListIds = new ArrayList<>();
    private MainNodeAdapter mainRecyclerAdapter;
    private static final String TAG = AlgorithmViewModel.class.getSimpleName();
    public static HashMap<Integer, Integer> footersList = null;
    public boolean duplicatesRemoved = false;
    public boolean isFirstChildDuplicate = false;

    @Inject
    public AlgorithmViewModel(NodeRepository nodeRepository) {
        this.nodeRepository = nodeRepository;
        this.node = new MutableLiveData<>();
        this.nodeList = new ArrayList<>();
        this.liveNodeList = new MutableLiveData<>();
        this.footerText = new MutableLiveData<>();
        this.selectedPageId = new MutableLiveData<>();
        this.nodeOptions = new ArrayList<>();
        this.recyclerMap = new HashMap<>();
        this.reverseRecyclerMap = new HashMap<>();
        footersList = AlgorithmActivity.getFooterList().getValue();
        this.onNodeSelectedListener = algorithmDescription -> {
        };
        recyclerUpdate = new RecyclerUpdate(-1,-1);
    }

    @SuppressLint("CheckResult")
    public void LoadNode(int nodeId) {
        nodeRepository.getNode(nodeId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onNodeLoaded, this::onLoadError);
    }

    @SuppressLint("CheckResult")
    public void setTitle(int nodeId, TextView symptomTitleTextView) {
        nodeRepository.getNode(nodeId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(node -> {
                    symptomTitleTextView.setText(node.getTitle());
                    getFooter(node.getPage());
                });
    }

    @SuppressLint("CheckResult")
    public void LoadPage(double page, TextView textView) {
        Log.d(TAG, "Node: LoadPage: " + page);
        nodeRepository.getNodeByPage(page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(node -> {
                    onNodeLoadedByUrl(node, textView);
                });
    }

    public MutableLiveData<String> getFooterText() {
        return footerText;
    }
/// OPTIONS SECTION

    @SuppressLint("CheckResult")
    public void loadNodes(int parentId) {
        nodeRepository.getChildNode(parentId, false)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onLoaded, this::onLoadError);
    }

    public List<AlgorithmCardViewModel> getNodes() {
        return nodeOptions;
    }

    @Bindable
    public boolean getIsSingleNode() {
        return isSingleNode;
    }

    private void onLoaded(List<AlgorithmDescription> nodeDescriptionList) {
        Log.d(TAG, "Node: onLoaded: nodeOPtions" + nodeDescriptionList.size());
        List<AlgorithmCardViewModel> algorithmNodeViewModels = new ArrayList();
        for (AlgorithmDescription aNodeDescription : nodeDescriptionList) {
            boolean conditional = false;
            if (aNodeDescription.getIsCondition()) conditional = true;
            algorithmNodeViewModels.add(new AlgorithmCardViewModel(aNodeDescription, conditional));
        }

        nodeOptions = algorithmNodeViewModels;
        setSingleNode(algorithmNodeViewModels.size() == 1);
    }

    public void setSingleNode(boolean singleNode) {
        isSingleNode = singleNode;
        notifyPropertyChanged(BR.isSingleNode);
    }

    public void selectNode(AlgorithmDescription algorithmDescription) {
        node.setValue(algorithmDescription);
    }

    private void onNodeLoaded(AlgorithmDescription node) {
        SelectNode(node, true);
    }

    private void onNodeLoadedByUrl(AlgorithmDescription node, TextView textView) {
        selectNodeByUrl(node, textView);
    }

    public void PreviewNode(AlgorithmDescription node) {
        this.node.setValue(node);
    }

    public AlgorithmDescription getPreviewNode() {
        return this.node.getValue();
    }

    public void SelectNode(AlgorithmDescription node) {
        List<AlgorithmDescription> cp = new ArrayList<>();
        for (AlgorithmDescription algorithmDescription : nodeList) {
            cp.add(algorithmDescription);
            if (algorithmDescription == node) break;
        }
        nodeList = cp;
        liveNodeList.setValue(nodeList);
        SelectNode(node, true);
    }

    public List<AlgorithmDescription> getNodeList() {
        return nodeList;
    }

    public MutableLiveData<List<AlgorithmDescription>> getLiveNodeList() {
        return liveNodeList;
    }

    public Map<AlgorithmDescription, List<AlgorithmCardViewModel>> getRecyclerMap(){
        return recyclerMap;
    }


    public void feedMap(AlgorithmDescription node, AlgorithmDescription parentNode) {
        if (parentNode != null){
            if (node.getChildCount() > 1) {
            List<AlgorithmDescription> subNodes = new ArrayList<>();
            subNodes.add(node);
            populateRecycler(node, subNodes);
            } else {
                if (node.getId() == parentNode.getId()) {
                    getChildNode(node, true);

                } else {
                    List<AlgorithmDescription> subNodes = new ArrayList<>();
                    subNodes.add(node);
                    populateRecycler(node, subNodes);
                }
            }
        } else {
            if (nodeList.size() >= 3)
                isFirstChildDuplicate = false;
            if (!isFirstChildDuplicate) {
            getChildNode(node, true);
            isFirstChildDuplicate = true;
            }
        }
    }

    public void removeDuplicateFirstNode() {
        if(nodeList.size() == 2 && !duplicatesRemoved) {
            removeRecyclerValues(1);
            duplicatesRemoved = true;
        }
    }

    @SuppressLint("CheckResult")
    public void getChildNode(AlgorithmDescription node, boolean useSameForParent){
        nodeRepository.getChildNode(node.getId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(nodes -> {
                    if (useSameForParent && nodes.size() > 0){
                        populateRecycler(nodes.get(0), nodes);
                    } else {
                        populateRecycler(node, nodes);
                    }
                });
    }

    @SuppressLint("CheckResult")
    public void populateRecycler(AlgorithmDescription parentNode, List<AlgorithmDescription> subNodes) {
        List<AlgorithmCardViewModel> optionsAndAnswers = new ArrayList<>();
        for (AlgorithmDescription aNodeDescription : subNodes) {
            if (aNodeDescription.getChildCount() > 1 && subNodes.size() == 1) {
                nodeRepository
                        .getChildNode(aNodeDescription.getId())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(childNodes -> {
                            for (AlgorithmDescription childNode : childNodes) {
                                optionsAndAnswers.add(new AlgorithmCardViewModel(childNode, childNode.getIsCondition()));
                            }
                            updateRecyclerValues(aNodeDescription, optionsAndAnswers);
                        });
            } else {
                List<AlgorithmCardViewModel> previousOptions = recyclerMap.get(parentNode);
                if (previousOptions != null && subNodes.size() == 1){
                    previousOptions = new ArrayList<>();
                    previousOptions.add(new AlgorithmCardViewModel(aNodeDescription, aNodeDescription.getIsCondition()));
                    recyclerMap.put(aNodeDescription, previousOptions);
                } else {

                    optionsAndAnswers.add(new AlgorithmCardViewModel(aNodeDescription, aNodeDescription.getIsCondition()));
                    recyclerMap.put(parentNode, optionsAndAnswers);
                }
                reverseRecyclerMap.put(aNodeDescription, aNodeDescription);
//                if (parentNode.getId() == aNodeDescription.getId()){
//                    getChildNode(parentNode,false);
//                }
                nodeList.add(aNodeDescription);
                liveNodeList.setValue(nodeList);

//                            if (!nodeListIds.contains(aNodeDescription.getId())) {
//                nodeListIds.add(aNodeDescription.getId());
//                            }
            }
        }

    }

    void updateRecyclerValues(AlgorithmDescription aNodeDescription, List<AlgorithmCardViewModel> optionsAndAnswers){
            recyclerMap.put(aNodeDescription, optionsAndAnswers);
        for (AlgorithmCardViewModel model: optionsAndAnswers
             ) {
            reverseRecyclerMap.put(model.getNode(), aNodeDescription);
        }
            nodeList.add(aNodeDescription);
            liveNodeList.postValue(nodeList);
    }

    @SuppressLint("CheckResult")
    private void getFooter(int page) {
        try {
            int id = footersList.get(page);
            if (id != -1 && id != 0)
                nodeRepository.getNode(id)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(nodes -> {
                            setFooter(nodes.getDescription());
                        });
        } catch (Exception ignored) {
        }
    }

    private void setFooter(String description) {
        Pattern pattern = Pattern.compile("<em>(.*?)</em>");
        Matcher matcher = pattern.matcher(description);

        List<String> listMatches = new ArrayList<>();

        if (matcher.find())
        {
            listMatches.add(matcher.group(1));
        }

        StringBuilder footer = new StringBuilder();
        for(String s : listMatches)
        {
            footer.append(s);
        }
        footerText.setValue(footer.toString());
    }

    public void SelectNode(AlgorithmDescription node, boolean skipSingleNode) {
        if (symptomTitle == null || symptomTitle.length() == 0)
            symptomTitle = (node.getTitle());
        if (!skipSingleNode || node.getHasDescription() || node.getChildCount() > 1 || node.getFirstChildNodeId() == null) {
            if (node.getChildCount() > 1) {
                nodeList.add(node);
                liveNodeList.setValue(nodeList);
                feedMap(node, null);
            }
            this.node.setValue(node);
            this.onNodeSelectedListener.onNodeSelected(node);
        } else {
            this.onNodeSelectedListener.onNodeSelected(node);
            LoadNode(node.getFirstChildNodeId());
            nodeList.add(node);
            liveNodeList.setValue(nodeList);
            feedMap(node, null);
        }

        Log.i(TAG, "Node id: " + node.getId() + "\tNode title: " + node.getTitle() + " nodeList size " + nodeList.size() + " Child count " + node.getChildCount());
    }

    private void selectNodeByUrl(AlgorithmDescription node, TextView textView) {
        if (node.getNodeTypeCode().equals("ASMPT") || node.getNodeTypeCode().equals("CSMPT") || node.getNodeTypeCode().equals("CHRNC")) {
            nodeList.clear();
            recyclerMap.clear();
            reverseRecyclerMap.clear();
            isFirstChildDuplicate = false;
            nodeList.add(node);
            liveNodeList.setValue(nodeList);
            mainRecyclerAdapter.notifyDataSetChanged();
            textView.setText(node.getTitle());
            feedMap(node, null);
        }
    }

    //region Properties
    public MutableLiveData<AlgorithmDescription> getNode() {
        return node;
    }

    public void setOnNodeSelectedListener(OnNodeSelectedListener onNodeSelectedListener) {
        this.onNodeSelectedListener = onNodeSelectedListener;
    }

    public MutableLiveData<String> getTitle() {
        return title;
    }

    public String getSymptomTitle() {
        return symptomTitle;
    }

    public int getOptionAnswerIndex(Integer id, boolean includeNewNodes) {
        int recyclerIndex = -1;
        int sizeToObserve = -1;
       if (includeNewNodes)
           sizeToObserve = 0;
        try {
            for (int i = 0; i < nodeList.size() - sizeToObserve; i++ ) {
                AlgorithmDescription algorithmDescription = nodeList.get(i);
                if (id.equals(algorithmDescription.getId())) {
                    return i;
                }
            }
            return recyclerIndex;
        } catch (Exception e){
            return -1;
        }
    }

    public void removeRecyclerValues(int poss) {
        // poss is the index of the answer/option selected
        // the reverse is to map from the options to the node
        try {
            int itemsToRemove = nodeList.size() - (poss + 1);
            for (int i = 0; i < itemsToRemove; i++){
                int removedIndex = nodeList.size() - 1;
                AlgorithmDescription algorithmDescriptionR = getParentNode(removedIndex);
                notifyRecycler(0, removedIndex);
                recyclerMap.remove(algorithmDescriptionR);
                nodeList.remove(removedIndex);
            }
        } catch (Exception ignored) {
        }
    }

    private AlgorithmDescription getParentNode(int poss){
        AlgorithmDescription algorithmDescription = getItemByIndex(poss);
        return reverseRecyclerMap.get(algorithmDescription);
    }

    private AlgorithmDescription getItemByIndex(int index) {
        AlgorithmDescription node = nodeList.get(index);
        return node;
    }

    public RecyclerUpdate getRecyclerUpdate() {
        return recyclerUpdate;
    }
    public void setRecyclerDefault() {
        notifyRecycler(-1, -1);
    }
    private void notifyRecycler(int typeOfUpdate, int updateIndex) {
        recyclerUpdate.setTypeOfUpdate(typeOfUpdate);
        recyclerUpdate.setUpdateIndex(updateIndex);
    }

    public void setAdapterToViewModel(MainNodeAdapter mainNodeAdapter) {
        mainRecyclerAdapter = mainNodeAdapter;
    }

    //endregion

    //region Listeners
    public interface OnNodeSelectedListener {
        void onNodeSelected(AlgorithmDescription algorithmDescription);
    }
    //endregion

    //Content ViewModel

    public void loadNode(AlgorithmDescription nodeDescription) {
        this.algorithmNodeDescription.setValue(nodeDescription);
        notifyChange();
    }

    public String getContentTitle() {
        AlgorithmDescription nodeDescription = node.getValue();
        if (nodeDescription == null || !nodeDescription.getHasTitle())
            return "";
        else {
            String yourHtmlText = nodeDescription.getTitle();
            return yourHtmlText;
        }
    }

    public boolean getHasDescription() {
        AlgorithmDescription nodeDescription = algorithmNodeDescription.getValue();
        if (nodeDescription == null)
            return false;
        return nodeDescription.getHasDescription();
    }

    public String getDescription() {
        String description;
        AlgorithmDescription nodeDescription = node.getValue();
        if (nodeDescription == null)
            return "";
        if (nodeDescription.getHasTitle()) {
            String title = "";
            if (nodeDescription.getNodeTypeCode().equalsIgnoreCase("URGNT")) {
                title = "<h4 class=\"urgent\">" + getContentTitle() + "</h4>";
            } else {
                title = "<h4>" + getContentTitle() + "</h4>";
            }
            description = title + nodeDescription.getDescription().replace("http://35.184.137.237:8915/MyImages/", "file:///android_asset/img/");
            return description;
        }
        description = nodeDescription.getDescription().replace("http://35.184.137.237:8915/MyImages/", "file:///android_asset/img/");

        return description;
    }

    public WebViewClient getClient() {
        return new Client();
    }

    private class Client extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            String pages = url.replace("http://page/", "")
                    .replace("file:///android_asset/styles/page/", "");

            double page = Double.parseDouble(pages.split("/")[0]);
            selectedPageId.setValue(page);
            return true;
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request,
                                    WebResourceError error) {
            super.onReceivedError(view, request, error);

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

        }
    }

    public MutableLiveData<Double> getSelectedPageId() {
        return selectedPageId;
    }

    public void setSelectedPageId(double page) {
        Log.d(TAG, "getSelectedPageId: " + selectedPageId);
        selectedPageId.setValue(page);
    }

    //Content ViewModel
}
