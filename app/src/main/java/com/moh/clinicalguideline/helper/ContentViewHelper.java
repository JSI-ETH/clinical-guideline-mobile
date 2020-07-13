package com.moh.clinicalguideline.helper;

import android.content.Context;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.moh.clinicalguideline.core.AlgorithmDescription;
import com.moh.clinicalguideline.views.algorithm.AlgorithmViewModel;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public WebViewClient getWebViewClient(Context context, AlgorithmViewModel algorithmViewModel) {
        return new Client(context, algorithmViewModel);
    }

    private class Client extends WebViewClient {
        private Context context;
        private AlgorithmViewModel algorithmViewModel;

        public Client(Context context, AlgorithmViewModel algorithmViewModel) {
            this.context = context;
            this.algorithmViewModel = algorithmViewModel;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            String pages = url.replace("http://page/", "")
                    .replace("file:///android_asset/styles/page/", "");
            double page = Double.valueOf(pages.split("/")[0]);
//            Toast.makeText(context, "" + page, Toast.LENGTH_SHORT).show();
            algorithmViewModel.setSelectedPageId(page);
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

    public List<AlgorithmDescription> removeDuplicateNodes(List<AlgorithmDescription> nodes) {
        Set<Integer> seen = new HashSet<>();
        AlgorithmDescription aldToBeRemoved = null;
        for (AlgorithmDescription ald : nodes) {
            if (!seen.add(ald.getId())) {
                aldToBeRemoved = ald;
            }
        }
        nodes.remove(aldToBeRemoved);
        return nodes;
    }

}
