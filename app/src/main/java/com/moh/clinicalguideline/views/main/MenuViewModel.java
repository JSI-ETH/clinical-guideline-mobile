package com.moh.clinicalguideline.views.main;

import android.util.Log;

import com.moh.clinicalguideline.R;
import com.moh.clinicalguideline.core.AlgorithmDescription;
import com.moh.clinicalguideline.data.entities.NodeDescription;
import com.moh.clinicalguideline.helper.SimpleLayoutAdapter;
import com.moh.clinicalguideline.helper.ViewModel;
import com.moh.clinicalguideline.repository.NodeRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class MenuViewModel extends ViewModel<MenuNavigator> {

    private SimpleLayoutAdapter<AlgorithmDescription> adapter;
    private final NodeRepository nodeRepository;

    private boolean loading;
    @Inject
    public MenuViewModel(NodeRepository nodeRepository){

        this.nodeRepository = nodeRepository;
         this.adapter = new SimpleLayoutAdapter<>(R.layout.activity_menu_mlist, item -> {
                navigator.openSymptom(item.getId());
         });
    }
    public void loadAdultSymptom() {
        setLoading(true);
        nodeRepository.getAdultSymptom()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::OnAdultSymptomLoaded,this::onLoadError);
    }

    public void loadChildSymptom() {
        setLoading(true);
        nodeRepository.getChildSymptom()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::OnChildSymptomLoaded,this::onLoadError);
    }

    public SimpleLayoutAdapter<AlgorithmDescription> getAdapter(){
         return adapter;
    }

    private void OnAdultSymptomLoaded(List<AlgorithmDescription> nodeDescriptionList) {
        setLoading(false);
        adapter.setData(nodeDescriptionList);
        notifyChange();
    }

    private void OnChildSymptomLoaded(List<AlgorithmDescription> nodeDescriptionList) {
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
        notifyChange();
    }
}
