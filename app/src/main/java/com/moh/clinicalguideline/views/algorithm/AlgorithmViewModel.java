package com.moh.clinicalguideline.views.algorithm;

import android.annotation.SuppressLint;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.Bindable;
import android.databinding.Observable;
import android.util.Log;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.moh.clinicalguideline.BR;
import com.moh.clinicalguideline.core.AlgorithmDescription;
import com.moh.clinicalguideline.data.entities.Node;
import com.moh.clinicalguideline.helper.recyclerview.MainNodeAdapter;
import com.moh.clinicalguideline.helper.view.BaseViewModel;
import com.moh.clinicalguideline.repository.NodeRepository;
import com.moh.clinicalguideline.views.algorithm.content.ContentViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class AlgorithmViewModel extends BaseViewModel<AlgorithmNavigator> {

    private final NodeRepository nodeRepository;
    private MutableLiveData<AlgorithmDescription> node;
    private List<AlgorithmDescription> nodeList;
    private List<AlgorithmCardViewModel> nodeOptions;
    private OnNodeSelectedListener onNodeSelectedListener;
    private MutableLiveData<String> title = new MutableLiveData<>();
    private String symptomTitle = "";
    private Map<AlgorithmDescription, List<AlgorithmCardViewModel>> map;
    private MutableLiveData<AlgorithmDescription> algorithmNodeDescription;
    private MutableLiveData<Double> selectedPageId;
    private boolean isSingleNode;
    private List<Integer> nodeListIds = new ArrayList<>();

    private static final String TAG = AlgorithmViewModel.class.getSimpleName();

    @Inject
    public AlgorithmViewModel(NodeRepository nodeRepository) {
        this.nodeRepository = nodeRepository;
        this.node = new MutableLiveData<>();
        this.nodeList = new ArrayList<>();
        this.selectedPageId = new MutableLiveData<>();
        this.nodeOptions = new ArrayList<AlgorithmCardViewModel>();
        this.map = new HashMap<>();
        this.onNodeSelectedListener = algorithmDescription -> {

        };
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
                });
    }

    @SuppressLint("CheckResult")
    public void LoadPage(double page, TextView textView, MainNodeAdapter mainNodeAdapter) {
        Log.d(TAG, "Node: LoadPage: " + page);
        nodeRepository.getNodeByPage(page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(node -> {
                    onNodeLoadedByUrl(node, textView, mainNodeAdapter);
                });
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

    private void onNodeLoadedByUrl(AlgorithmDescription node, TextView textView, MainNodeAdapter mainNodeAdapter) {
        selectNodeByUrl(node, textView, mainNodeAdapter);
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
        SelectNode(node, true);
    }

    public List<AlgorithmDescription> getNodeList() {
        return nodeList;
    }

    @SuppressLint("CheckResult")
    public void feedMap(AlgorithmDescription node) {
        List<AlgorithmCardViewModel> optionsAndAnswers = new ArrayList<>();
        nodeRepository.getChildNode(node.getId(), false)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(nodes -> {
                    for (AlgorithmDescription aNodeDescription : nodes) {
                        nodeList.add(aNodeDescription);
                        optionsAndAnswers.add(new AlgorithmCardViewModel(aNodeDescription, false));
                    }
                    map.put(node, optionsAndAnswers);
                });
        nodeRepository.getChildNode(node.getId(), true)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(nodes -> {
                    for (AlgorithmDescription aNodeDescription : nodes) {
                        nodeList.add(aNodeDescription);
                        optionsAndAnswers.add(new AlgorithmCardViewModel(aNodeDescription, true));
                    }
                    map.put(node, optionsAndAnswers);
                });
    }

    @SuppressLint("CheckResult")
    public void feedMapChild(AlgorithmDescription node, MainNodeAdapter mainNodeAdapter) {
        List<AlgorithmCardViewModel> optionsAndAnswers = new ArrayList<>();
        nodeRepository.getChildNode(node.getId(), false)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(nodes -> {
                    for (AlgorithmDescription aNodeDescription : nodes) {
                        if (aNodeDescription.getChildCount() > 1 && nodes.size() == 1) {
                            nodeRepository.getChildNode(aNodeDescription.getId(), true)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(childNodes -> {
                                        for (AlgorithmDescription childNode : childNodes) {
                                            optionsAndAnswers.add(new AlgorithmCardViewModel(childNode, true));
                                        }
                                        map.put(aNodeDescription, optionsAndAnswers);
                                        nodeList.add(aNodeDescription);
                                        mainNodeAdapter.setKeyNodesList(nodeList);
                                    });
                            nodeRepository.getChildNode(aNodeDescription.getId(), false)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(childNodes -> {
                                        for (AlgorithmDescription childNode : childNodes) {
                                            optionsAndAnswers.add(new AlgorithmCardViewModel(childNode, false));
                                            Log.d(TAG, "feedMapChild from if false: " + optionsAndAnswers.size());
                                        }
                                        map.put(aNodeDescription, optionsAndAnswers);
                                        nodeList.add(aNodeDescription);
                                        mainNodeAdapter.setKeyNodesList(nodeList);
                                    });
                        } else {
                            optionsAndAnswers.add(new AlgorithmCardViewModel(aNodeDescription, false));
                            map.put(node, optionsAndAnswers);
                            if (!nodeListIds.contains(aNodeDescription.getId())) {
                                nodeListIds.add(aNodeDescription.getId());
                                nodeList.add(aNodeDescription);
                            }
                            mainNodeAdapter.setKeyNodesList(nodeList);
                        }
                    }
                });
    }

    public Map<AlgorithmDescription, List<AlgorithmCardViewModel>> getMap() {
        return map;
    }

    public void SelectNode(AlgorithmDescription node, boolean skipSingleNode) {
        if (symptomTitle == null || symptomTitle.length() == 0)
            symptomTitle = (node.getTitle());
        if (!skipSingleNode || node.getHasDescription() || node.getChildCount() > 1 || node.getFirstChildNodeId() == null) {
            if (!skipSingleNode) {
//                nodeList.add(node);
                feedMap(node);
            }
            this.node.setValue(node);
            this.onNodeSelectedListener.onNodeSelected(node);
        } else {
            this.onNodeSelectedListener.onNodeSelected(node);
            LoadNode(node.getFirstChildNodeId());
            nodeList.add(node);
            feedMap(node);
        }

        Log.i(TAG, "Node id: " + node.getId() + "\tNode title: " + node.getTitle() + " nodeList size " + nodeList.size() + " Child count " + node.getChildCount());
    }

    private void selectNodeByUrl(AlgorithmDescription node, TextView textView, MainNodeAdapter mainNodeAdapter) {
        if (node.getNodeTypeCode().equals("ASMPT") || node.getNodeTypeCode().equals("CSMPT") || node.getNodeTypeCode().equals("CHRNC")) {
            nodeList.clear();
            map.clear();
            textView.setText(node.getTitle());
            feedMapChild(node, mainNodeAdapter);
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

    //endregion

    //region Listeners
    public interface OnNodeSelectedListener {
        void onNodeSelected(AlgorithmDescription algorithmDescription);
    }
    //endregion

    //Content ViewModel
    //Content ViewModel
    //Content ViewModel
    //Content ViewModel
    //Content ViewModel
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

            double page = Double.valueOf(pages.split("/")[0]);
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
    //Content ViewModel
    //Content ViewModel
    //Content ViewModel
}
