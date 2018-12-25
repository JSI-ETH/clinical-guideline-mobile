package com.moh.clinicalguideline.views.algorithm.content;

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
import com.moh.clinicalguideline.views.algorithm.AlgorithmNavigator;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class ContentViewModel extends BaseViewModel {

    private MutableLiveData<AlgorithmDescription> algorithmNodeDescription;
    private MutableLiveData<Integer> selectedPageId;

    @Inject
    public ContentViewModel(){
         this.algorithmNodeDescription = new MutableLiveData<>();
         this.selectedPageId = new MutableLiveData<>();
    }

    public void loadNode(AlgorithmDescription nodeDescription) {

        this.algorithmNodeDescription.setValue(nodeDescription);
        notifyChange();
    }

    public String getTitle(){

        AlgorithmDescription nodeDescription = algorithmNodeDescription.getValue();
        if(nodeDescription==null || !nodeDescription.getHasTitle())
            return "";
        else
        {
            String yourHtmlText = nodeDescription.getTitle();
            return yourHtmlText;
        }

    }
    @Bindable
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

    public MutableLiveData<Integer> getSelectedPageId() {
        return selectedPageId;
    }

    ///region WebViewClient
    @Bindable
    public WebViewClient getClient() {
        return new Client();
    }

    private class Client extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            String pages =  url.replace("http://page/","")
                    .replace("file:///android_asset/styles/page/","");

            int page = Integer.valueOf(pages.split("/")[0]);
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
    //endregion

}
