package com.moh.clinicalguideline.views.algorithm;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.Bindable;
import android.util.Log;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.moh.clinicalguideline.BR;
import com.moh.clinicalguideline.core.AlgorithmDescription;
import com.moh.clinicalguideline.helper.view.BaseViewModel;
import com.moh.clinicalguideline.repository.NodeRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class AlgorithmViewModel extends BaseViewModel<AlgorithmNavigator> {

    private final NodeRepository nodeRepository;
    private MutableLiveData<List<AlgorithmDescription>> answerNodes;
    private MutableLiveData<AlgorithmDescription> algorithmNodeDescription;
    private MutableLiveData<Integer> selectedItemId = new MutableLiveData<>();

    @Inject
    public AlgorithmViewModel(NodeRepository nodeRepository){
         this.nodeRepository = nodeRepository;
        answerNodes = new MutableLiveData<>();
         algorithmNodeDescription = new MutableLiveData<>();
    }
    public void loadNode(int nodeId) {
        this.selectedItemId.setValue(nodeId);
        loadAlgorithmDescription(nodeId);
    }
    public void loadNodeByPage(int pageId){
        nodeRepository.getNodeByPage(pageId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::OnAlgorithmNodeLoaded,this::onLoadError);
    }
    private void loadAlgorithmDescription(int nodeId){
        setLoading(true);
        nodeRepository.getNode(nodeId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::OnAlgorithmNodeLoaded,this::onLoadError);
    }
    private void loadConditionalChildNodes(int nodeId) {

        nodeRepository.getChildNode(nodeId,true)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::OnConditionalChildNodesLoaded,this::onLoadError);
    }
    private void OnAlgorithmNodeLoaded(AlgorithmDescription nodeDescription) {

        this.algorithmNodeDescription.setValue(nodeDescription);
        loadConditionalChildNodes(nodeDescription.getId());
        // notifyChange();
    }
    public Boolean isOneChild () {
        AlgorithmDescription nodeDescription = algorithmNodeDescription.getValue();
        if(nodeDescription!=null && (!nodeDescription.getNodeTypeCode().equals("ASMPT") && !nodeDescription.getNodeTypeCode().equals("CSMPT") && !nodeDescription.getNodeTypeCode().equals("CHRNC") ))
            return nodeDescription.getChildCount()==1;
        return false;
    }
    private void OnConditionalChildNodesLoaded(List<AlgorithmDescription> nodeDescriptionList) {
        setLoading(false);
        answerNodes.setValue(nodeDescriptionList);
        notifyPropertyChanged(BR.description);
    }

    public MutableLiveData<AlgorithmDescription> getAlgorithmNodeDescription() {
        return algorithmNodeDescription;
    }
    public MutableLiveData<List<AlgorithmDescription>> getAnswerNodes(){
        return answerNodes;
    }


    public void selectedNewNode(int id){
        navigator.openAlgorithm(id);
    }
    public String getTitle(){

        AlgorithmDescription nodeDescription = algorithmNodeDescription.getValue();
        if(nodeDescription==null || !nodeDescription.getHasTitle())
            return "";
        else
        {
            String yourHtmlText = nodeDescription.getTitle().replace("span style=\"color:", "font color='").replace(";\"","'").replace("</span>", "</font>");
            return yourHtmlText;
        }

    }
    public boolean getHasDescription(){

        AlgorithmDescription nodeDescription = algorithmNodeDescription.getValue();
        if(nodeDescription==null)
            return false;
        return nodeDescription.getHasDescription();
    }
    @Bindable
    public String getDescription(){

        AlgorithmDescription nodeDescription = algorithmNodeDescription.getValue();
        if(nodeDescription==null)
            return "";
        if(nodeDescription.getHasTitle())
        {
            String title= "";
            if(nodeDescription.getNodeTypeCode().equalsIgnoreCase("URGNT")) {
                title = "<h4 class=\"urgent\">" + getTitle() + "</h4>";
            } else {
                title = "<h4>" +getTitle() +"</h4>";
            }
            return title+nodeDescription.getDescription();
        }
        return  nodeDescription.getDescription();
    }

    //region Properties
    public MutableLiveData<Integer> getSelectedItemId() {
        return selectedItemId;
    }
    //endregion

    ///region WebViewClient
    public WebViewClient getClient() {
        return new Client();
    }

    private class Client extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            String pages =  url.replace("http://page/","")
                    .replace("file:///android_asset/styles/page/","");

            int page = Integer.valueOf(pages.split("/")[0]);

            nodeRepository.getNodeByPage(page)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::OnAlgorithmNodePageLoaded,this::onLoadError);
            return true;

        }
        private void OnAlgorithmNodePageLoaded(AlgorithmDescription algorithmDescription) {

            navigator.openAlgorithm(algorithmDescription.getId());
        }

        public void onLoadError(Throwable throwable) {
            Log.e("Error Fetching data", throwable.getMessage());

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
    //endregion

}
