package com.moh.clinicalguideline.views.algorithm;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.Bindable;
import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.moh.clinicalguideline.BR;
import com.moh.clinicalguideline.R;
import com.moh.clinicalguideline.core.AlgorithmDescription;
import com.moh.clinicalguideline.data.entities.NodeDescription;
import com.moh.clinicalguideline.helper.recyclerview.SimpleLayoutAdapter;
import com.moh.clinicalguideline.helper.view.BaseViewModel;
import com.moh.clinicalguideline.repository.NodeRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class AlgorithmViewModel extends BaseViewModel<AlgorithmNavigator> {

    private final NodeRepository nodeRepository;

    private MutableLiveData<List<AlgorithmCardViewModel>> adapter;
    private MutableLiveData<List<AlgorithmDescription>> conditionalAdapter;
    private MutableLiveData<List<AlgorithmDescription>> optionsAdapter;
    private MutableLiveData<AlgorithmDescription> algorithmNodeDescription;

    public MutableLiveData<AlgorithmDescription> getAlgorithmNodeDescription() {
        return algorithmNodeDescription;
    }

    private boolean loading;
    private boolean rendering;

    @Inject
    public AlgorithmViewModel(NodeRepository nodeRepository){

         this.nodeRepository = nodeRepository;
         adapter = new MutableLiveData<>();
         conditionalAdapter= new MutableLiveData<>();
         optionsAdapter= new MutableLiveData<>();
        algorithmNodeDescription = new MutableLiveData<>();
    }


    public void loadNode(int nodeId) {
        this.rendering = true;
        setLoading(true);
        loadAlgorithmDescription(nodeId);
    }

    private void loadAlgorithmDescription(int nodeId){
        setLoading(true);
        nodeRepository.getNode(nodeId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::OnAlgorithmNodeLoaded,this::onLoadError);
    }

    private void loadNonConditionalChildNodes(int nodeId) {

        nodeRepository.getChildNode(nodeId,false)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onNonConditionalChildNodesLoaded,this::onLoadError);
    }

    private void loadConditionalChildNodes(int nodeId) {

        nodeRepository.getChildNode(nodeId,true)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::OnConditionalChildNodesLoaded,this::onLoadError);
    }

    public MutableLiveData<List<AlgorithmCardViewModel>> getAdapter(){
         return adapter;
    }

    public MutableLiveData<List<AlgorithmDescription>> getConditionalAdapter(){
        return conditionalAdapter;
    }

    public MutableLiveData<List<AlgorithmDescription>> getOptionsAdapter(){
        return optionsAdapter;
    }

    private void OnAlgorithmNodeLoaded(AlgorithmDescription nodeDescription) {

        this.algorithmNodeDescription.setValue(nodeDescription);
        loadNonConditionalChildNodes(nodeDescription.getId());
       // notifyChange();
    }

    public Boolean isOneChild ()
    {
        AlgorithmDescription nodeDescription = algorithmNodeDescription.getValue();
        if(nodeDescription!=null && (!nodeDescription.getNodeTypeCode().equals("ASMPT") && !nodeDescription.getNodeTypeCode().equals("CSMPT") && !nodeDescription.getNodeTypeCode().equals("CHRNC") ))
            return nodeDescription.getChildCount()==1;
        return false;
    }

    public void openNext() {
        AlgorithmDescription nodeDescription = algorithmNodeDescription.getValue();
        this.navigator.openAlgorithm(nodeDescription.getFirstChildNodeId(),nodeDescription.getId());
    }

    private void onNonConditionalChildNodesLoaded(List<AlgorithmDescription> nodeDescriptionList) {

        AlgorithmDescription nodeDescription = algorithmNodeDescription.getValue();
        List<AlgorithmCardViewModel> algorithmNodeViewModels = new ArrayList();
        for (AlgorithmDescription aNodeDescription: nodeDescriptionList) {
            algorithmNodeViewModels.add(new AlgorithmCardViewModel(aNodeDescription));
            }
        loadConditionalChildNodes(nodeDescription.getId());
        adapter.setValue(algorithmNodeViewModels);

    }

    private void OnConditionalChildNodesLoaded(List<AlgorithmDescription> nodeDescriptionList) {
        setLoading(false);
        conditionalAdapter.setValue(nodeDescriptionList);
        notifyPropertyChanged(BR.description);
    }

    public void onLoadError(Throwable throwable) {
        Log.e("Error Fetching data", throwable.getMessage());
        setLoading(false);
    }
    @Bindable
    public boolean isLoading() {
        return loading || rendering;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
        notifyPropertyChanged(BR.loading);
    }
    public void setRendering(boolean rendering){

        this.rendering = rendering;
        notifyPropertyChanged(BR.loading);
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

    public boolean getHasDescription(){

        AlgorithmDescription nodeDescription = algorithmNodeDescription.getValue();
        if(nodeDescription==null)
            return false;
        return nodeDescription.getHasDescription();
    }

    public boolean IsChildNode(){

        AlgorithmDescription nodeDescription = algorithmNodeDescription.getValue();
        return nodeDescription.getNodeTypeCode().equalsIgnoreCase("URGNT")
                || nodeDescription.getNodeTypeCode().equalsIgnoreCase("NTURG")
                || nodeDescription.getNodeTypeCode().equalsIgnoreCase("ALGTM");
    }

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

            AlgorithmDescription nodeDescription = algorithmNodeDescription.getValue();
            navigator.openAlgorithm(algorithmDescription.getId(),nodeDescription.getId());
        }

        public void onLoadError(Throwable throwable) {
            Log.e("Error Fetching data", throwable.getMessage());
            setLoading(false);
        }
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request,
                                    WebResourceError error) {
            super.onReceivedError(view, request, error);

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            setRendering(false);
            super.onPageFinished(view, url);

        }
    }
}
