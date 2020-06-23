package com.moh.clinicalguideline.views.algorithm;

import android.annotation.SuppressLint;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.Bindable;
import android.util.Log;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.moh.clinicalguideline.BR;
import com.moh.clinicalguideline.core.AlgorithmDescription;
import com.moh.clinicalguideline.data.entities.Node;
import com.moh.clinicalguideline.helper.view.BaseViewModel;
import com.moh.clinicalguideline.repository.NodeRepository;
import com.moh.clinicalguideline.views.algorithm.content.ContentViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class AlgorithmViewModel extends BaseViewModel<AlgorithmNavigator> {

    private final NodeRepository nodeRepository;
    private MutableLiveData<AlgorithmDescription> node;
    private List<AlgorithmDescription> nodeList;
    private MutableLiveData<List<AlgorithmDescription>> nodes;
    private MutableLiveData<List<AlgorithmCardViewModel>> nodeOptions;
    private OnNodeSelectedListener onNodeSelectedListener;
    private MutableLiveData<String> title = new MutableLiveData<>();
    private String symptomTitle = "";
    private MutableLiveData<AlgorithmDescription> algorithmNodeDescription;
    private MutableLiveData<Double> selectedPageId;
    private boolean isSingleNode;
    private static final String TAG = AlgorithmViewModel.class.getSimpleName();

    @Inject
    public AlgorithmViewModel(NodeRepository nodeRepository) {
        this.nodeRepository = nodeRepository;
        this.node = new MutableLiveData<>();
        this.nodeList = new ArrayList<>();
        this.nodes = new MutableLiveData<>();
        this.selectedPageId = new MutableLiveData<>();
        this.nodeOptions = new MutableLiveData<List<AlgorithmCardViewModel>>();
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
    public void LoadPage(double page) {
        nodeRepository.getNodeByPage(page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onNodeLoaded, this::onLoadError);

    }
/// OPTIONS SECTION
/// OPTIONS SECTION
/// OPTIONS SECTION
/// OPTIONS SECTION

    @SuppressLint("CheckResult")
    public void loadNodes(int parentId) {
        nodeRepository.getChildNode(parentId, false)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onLoaded, this::onLoadError);
    }

    public MutableLiveData<List<AlgorithmCardViewModel>> getNodes() {
        return nodeOptions;
    }

    @Bindable
    public boolean getIsSingleNode() {
        return isSingleNode;
    }

    private void onLoaded(List<AlgorithmDescription> nodeDescriptionList) {
        List<AlgorithmCardViewModel> algorithmNodeViewModels = new ArrayList();
        for (AlgorithmDescription aNodeDescription : nodeDescriptionList) {
            algorithmNodeViewModels.add(new AlgorithmCardViewModel(aNodeDescription));
        }
        nodeOptions.setValue(algorithmNodeViewModels);
        setSingleNode(algorithmNodeViewModels.size() == 1);
    }

    public void setSingleNode(boolean singleNode) {
        isSingleNode = singleNode;
        notifyPropertyChanged(BR.isSingleNode);
    }

    public void selectNode(AlgorithmDescription algorithmDescription) {
        node.setValue(algorithmDescription);
    }

    public void selectFirstNode() {
        if (nodeOptions.getValue() != null && !nodeOptions.getValue().isEmpty()) {
            AlgorithmDescription firstNode = nodeOptions.getValue().get(0).getNode();
            selectNode(firstNode);
        }
    }

    ////////////////////////////////
    ////////////////////////////////
    ////////////////////////////////
    ////////////////////////////////

    private void onNodeLoaded(AlgorithmDescription node) {
        SelectNode(node, true);
    }


    public void PreviewNode(AlgorithmDescription node) {
        this.node.setValue(node);
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

    public void SelectNode(AlgorithmDescription node, boolean skipSingleNode) {
        if (symptomTitle == null || symptomTitle.length() == 0)
            symptomTitle = (node.getTitle());
        if (!skipSingleNode || node.getHasDescription() || node.getChildCount() > 1 || node.getFirstChildNodeId() == null) {

            this.node.setValue(node);
            nodeList.add(node);
            if (node.getNodeTypeCode().equals("ASMPT") || node.getNodeTypeCode().equals("CSMPT") || node.getNodeTypeCode().equals("CHRNC")) {
                nodeList = new ArrayList<>();
                nodeList.add(node);
                symptomTitle = (node.getTitle());
            }
            this.onNodeSelectedListener.onNodeSelected(node);
        } else {
            this.onNodeSelectedListener.onNodeSelected(node);
            LoadNode(node.getFirstChildNodeId());
        }

        Log.i(TAG, "Node id: " + node.getId() + "\tNode title: " + node.getTitle());
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

    //Content ViewModel
    //Content ViewModel
    //Content ViewModel
    //Content ViewModel
}
