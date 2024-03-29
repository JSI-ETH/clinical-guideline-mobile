package com.moh.clinicalguideline.views.algorithm.content;

import androidx.lifecycle.MutableLiveData;
import androidx.databinding.Bindable;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.moh.clinicalguideline.core.AlgorithmDescription;
import com.moh.clinicalguideline.helper.view.BaseViewModel;


import javax.inject.Inject;


public class ContentViewModel extends BaseViewModel {

    private MutableLiveData<AlgorithmDescription> algorithmNodeDescription;
    private MutableLiveData<Double> selectedPageId;

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

        String description;
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
            description = title+nodeDescription.getDescription().replace("http://35.184.137.237:8915/MyImages/","file:///android_asset/img/");
            return description;
        }
        description = nodeDescription.getDescription().replace("http://35.184.137.237:8915/MyImages/","file:///android_asset/img/");

        return description;  }

    public MutableLiveData<Double> getSelectedPageId() {
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
    //endregion

}
