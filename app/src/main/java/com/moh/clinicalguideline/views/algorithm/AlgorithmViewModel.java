package com.moh.clinicalguideline.views.algorithm;

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
import com.moh.clinicalguideline.helper.SimpleLayoutAdapter;
import com.moh.clinicalguideline.helper.ViewModel;
import com.moh.clinicalguideline.repository.NodeRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class AlgorithmViewModel extends ViewModel<AlgorithmNavigator> {

    private SimpleLayoutAdapter<AlgorithmCardViewModel> adapter;
    private SimpleLayoutAdapter<AlgorithmDescription> conditionalAdapter;
    private SimpleLayoutAdapter<AlgorithmDescription> optionsAdapter;
    private final NodeRepository nodeRepository;
    private AlgorithmDescription nodeDescription;

    private boolean loading;
    private boolean rendering;

    @Inject
    public AlgorithmViewModel(NodeRepository nodeRepository){

         this.nodeRepository = nodeRepository;
         this.adapter = new SimpleLayoutAdapter<>(R.layout.activity_algorithm_list, item -> {
              if(item.getHasDescription() || item.getChildCount()>1 || nodeDescription.getFirstChildNodeId() == null)
             {
                 navigator.openAlgorithm(item.getId(),nodeDescription.getId());
             }
             else {
                 navigator.openAlgorithm(item.getFirstChildNodeId(),nodeDescription.getId());
             }
         });
         this.conditionalAdapter = new SimpleLayoutAdapter<>(R.layout.activity_algorithm_clist, item -> {
             if(item.getHasDescription() || item.getChildCount()>1 || nodeDescription.getFirstChildNodeId() == null)
             {
                 navigator.openAlgorithm(item.getId(),nodeDescription.getId());
             }
             else {
                 navigator.openAlgorithm(item.getFirstChildNodeId(),nodeDescription.getId());
             }
         });
         this.optionsAdapter = new SimpleLayoutAdapter<>(R.layout.activity_algorithm_option_layout, item -> {
            if(item.getIsSingle() && item.getChildCount()> 2 && nodeDescription.getFirstChildNodeId() == null)
            {
                navigator.openAlgorithm(item.getId(),nodeDescription.getId());
            }
            else {
                navigator.openAlgorithm(item.getFirstChildNodeId(),nodeDescription.getId());
            }
        });

    }


    public void loadNode(int nodeId,int parentId) {
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

    public SimpleLayoutAdapter<AlgorithmCardViewModel> getAdapter(){
         return adapter;
    }

    public SimpleLayoutAdapter<AlgorithmDescription> getConditionalAdapter(){
        return conditionalAdapter;
    }

    public SimpleLayoutAdapter<AlgorithmDescription> getOptionsAdapter(){
        return optionsAdapter;
    }

    private void OnAlgorithmNodeLoaded(AlgorithmDescription nodeDescription) {

        this.nodeDescription = nodeDescription;
        loadNonConditionalChildNodes(nodeDescription.getId());
       // notifyChange();
    }

    public Boolean isOneChild ()
    {
        if(nodeDescription!=null && (!nodeDescription.getNodeTypeCode().equals("ASMPT") && !nodeDescription.getNodeTypeCode().equals("CSMPT") && !nodeDescription.getNodeTypeCode().equals("CHRNC") ))
            return nodeDescription.getChildCount()==1;
        return false;
    }

    public void openNext() {
        this.navigator.openAlgorithm(nodeDescription.getFirstChildNodeId(),nodeDescription.getId());
    }

    private void onNonConditionalChildNodesLoaded(List<AlgorithmDescription> nodeDescriptionList) {

        List<AlgorithmCardViewModel> algorithmNodeViewModels = new ArrayList();
        for (AlgorithmDescription nodeDescription: nodeDescriptionList) {
            algorithmNodeViewModels.add(new AlgorithmCardViewModel(nodeDescription));
            }
        loadConditionalChildNodes(this.nodeDescription.getId());
        adapter.setData(algorithmNodeViewModels);

    }

    private void OnConditionalChildNodesLoaded(List<AlgorithmDescription> nodeDescriptionList) {
        setLoading(false);
        conditionalAdapter.setData(nodeDescriptionList);
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
        if(nodeDescription==null)
            return false;
        return nodeDescription.getHasDescription();
    }

    public boolean IsChildNode(){
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
