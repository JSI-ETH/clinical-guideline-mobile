package com.moh.clinicalguideline.helper;


import android.databinding.BindingAdapter;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.moh.clinicalguideline.R;


public class DataBindingAdapters {
    @BindingAdapter("android:src")
    public static void setImageResource(ImageView imageView, int resource) {
        imageView.setImageResource(resource);
    }

    @BindingAdapter("android:itemDecoration")
    public static void setItemDecoration(RecyclerView view, RecyclerView.ItemDecoration old,
                                         RecyclerView.ItemDecoration newVal) {
        if (old != null) {
            view.removeItemDecoration(old);
        }
        if (newVal != null) {
            view.addItemDecoration(newVal);
        }
    }

    @BindingAdapter("android:error")
    public static void setErrorTextMessage(TextInputLayout view, String errorMessage) {

        view.setError(errorMessage);
    }

    @BindingAdapter("android:vpadapter")
    public static void setPagerAdapter(ViewPager view, FragmentPagerAdapter fragmentPagerAdapter) {
        view.setAdapter(fragmentPagerAdapter);
    }

    @BindingAdapter("android:onPageChangeListener")
    public static void setOnPageChangeListener(ViewPager viewPager, ViewPager.OnPageChangeListener listener) {
        viewPager.clearOnPageChangeListeners();
        viewPager.addOnPageChangeListener(listener);
    }
    @BindingAdapter({ "android:setWebViewClient" })
    public static void setWebViewClient(WebView view, WebViewClient client) {
        view.setWebViewClient(client);
        view.getSettings().setJavaScriptEnabled(true);
        view.getSettings().setDomStorageEnabled(true);
        view.getSettings().setDefaultTextEncodingName("utf-8");
    }

    @BindingAdapter({ "android:load" })
    public static void load(WebView view, String data) {
        String header = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?> <html><head> " +
                //"<link rel=\"stylesheet\" type=\"text/css\" href=\"bootstrap.min.css\" />" +
                "<link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\" />" +
                "</head><body>";
        String footer = "</body><html>";
        String html = header+data+footer;


        view.loadDataWithBaseURL("file:///android_asset/styles/",html, "text/html;","utf-8", null);
    }
}