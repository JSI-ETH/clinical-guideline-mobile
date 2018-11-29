package com.moh.clinicalguideline.helper;


import android.databinding.BindingAdapter;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;


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
}