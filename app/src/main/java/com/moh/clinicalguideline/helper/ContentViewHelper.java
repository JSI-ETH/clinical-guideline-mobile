package com.moh.clinicalguideline.helper;

import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.moh.clinicalguideline.core.AlgorithmDescription;

public class ContentViewHelper {

    public String loadDataWithBaseURL(String data) {

        if (data == null || data == "") {
            return "";
        }
        String header = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?> <html><head> " +
                "<link rel=\"stylesheet\" type=\"text/css\" href=\"font/css/all.min.css\" />" +
                "<link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\" />" +
                "</head><body>";
        String footer = "</body><html>";
        data = data.replace("&hArr;", "<i class=\"fas fa-reply\"></i>");
        data = data.replace("&rArr;", "<i class=\"fas fa-share\"></i>");

        return header + data + footer;
    }

    public String setTitleToDescription(AlgorithmDescription nodeDescription) {
        String description;
        if (nodeDescription == null)
            return "";
        if (nodeDescription.getHasTitle()) {
            String title;
            if (nodeDescription.getNodeTypeCode().equalsIgnoreCase("URGNT")) {
                title = "<h4 class=\"urgent\">" + getContentTitle(nodeDescription) + "</h4>";
            } else {
                title = "<h4>" + getContentTitle(nodeDescription) + "</h4>";
            }
            description = title + nodeDescription.getDescription().replace("http://35.184.137.237:8915/MyImages/", "file:///android_asset/img/");
            return description;
        }
        description = nodeDescription.getDescription().replace("http://35.184.137.237:8915/MyImages/", "file:///android_asset/img/");

        return description;
    }

    private String getContentTitle(AlgorithmDescription nodeDescription) {
        if (nodeDescription == null || !nodeDescription.getHasTitle())
            return "";
        else {
            return nodeDescription.getTitle();
        }
    }

    private class Client extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            String pages =  url.replace("http://page/","")
                    .replace("file:///android_asset/styles/page/","");

            double page = Double.valueOf(pages.split("/")[0]);
//            selectedPageId.setValue(page);
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
}
