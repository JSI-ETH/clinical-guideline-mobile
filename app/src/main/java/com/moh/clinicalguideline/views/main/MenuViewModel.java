package com.moh.clinicalguideline.views.main;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.moh.clinicalguideline.R;
import com.moh.clinicalguideline.core.AlgorithmDescription;
import com.moh.clinicalguideline.helper.FooterReader;
import com.moh.clinicalguideline.helper.recyclerview.FilterableLayoutAdapter;
import com.moh.clinicalguideline.helper.view.BaseViewModel;
import com.moh.clinicalguideline.repository.NodeRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class MenuViewModel extends BaseViewModel<MenuNavigator> {

    private FilterableLayoutAdapter<AlgorithmDescription> adapter;
    private final NodeRepository nodeRepository;
    public static MutableLiveData<HashMap<Integer, Integer>> footersList = null;

    private boolean loading;
    @Inject
    public MenuViewModel(NodeRepository nodeRepository){

        this.nodeRepository = nodeRepository;
         this.adapter = new FilterableLayoutAdapter<>(R.layout.activity_menu_mlist, item -> {
                navigator.openSymptom(item.getId());
         });
    }
    public void loadAdultSymptom() {
        setLoading(true);
        nodeRepository.getAdultSymptom()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::OnLoaded,this::onLoadError);
    }

    public void loadChildSymptom() {
        setLoading(true);
        nodeRepository.getChildSymptom()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::OnLoaded,this::onLoadError);
    }
    public void loadChronic() {
        setLoading(true);
        nodeRepository.getChronicCare()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::OnLoaded,this::onLoadError);
    }

    public void loadAll() {
        setLoading(true);
        nodeRepository.getAllSymptoms()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::OnLoaded,this::onLoadError);
    }


    public void createFooterList(Context context) {
        if (footersList == null){
            FooterReader footerReader = new FooterReader();
            footersList = footerReader.createFooterList(context);
        }
    }

    public MutableLiveData<HashMap<Integer, Integer>>  getFooterList() {
        return footersList;
    }

    public FilterableLayoutAdapter<AlgorithmDescription> getAdapter(){
         return adapter;
    }

    private void OnLoaded(List<AlgorithmDescription> nodeDescriptionList) {
        setLoading(false);
        adapter.setData(nodeDescriptionList);
        notifyChange();
    }

    public void onLoadError(Throwable throwable) {
        Log.e("Error Fetching data", throwable.getMessage());
        setLoading(false);
    }
    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
        //notifyChange();
    }
    @BindingAdapter("itemDecoration")
    public static void setItemDecoration(RecyclerView view, RecyclerView.ItemDecoration old, RecyclerView.ItemDecoration newVal) {
        if (old != null) {
            view.removeItemDecoration(old);
        }
        if (newVal != null) {
            view.addItemDecoration(newVal);
        }
    }

}
