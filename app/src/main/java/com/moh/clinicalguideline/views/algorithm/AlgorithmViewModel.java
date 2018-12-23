package com.moh.clinicalguideline.views.algorithm;

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

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class AlgorithmViewModel extends BaseViewModel<AlgorithmNavigator> {

    private final NodeRepository nodeRepository;
    private MutableLiveData<AlgorithmDescription> algorithmNodeDescription;
    private MutableLiveData<Integer> selectedItemId = new MutableLiveData<>();

    @Inject
    public AlgorithmViewModel(NodeRepository nodeRepository){
         this.nodeRepository = nodeRepository;
         algorithmNodeDescription = new MutableLiveData<>();
    }
    public void loadNode(int nodeId) {
        this.selectedItemId.setValue(nodeId);
        nodeRepository.getNode(nodeId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::OnAlgorithmNodeLoaded,this::onLoadError);
    }
    public String getTitle(){
        return "Symptom Title ";
    }
    public void loadNodeByPage(int pageId){
        nodeRepository.getNodeByPage(pageId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::OnAlgorithmNodeLoaded,this::onLoadError);
    }
    private void OnAlgorithmNodeLoaded(AlgorithmDescription nodeDescription) {
        this.algorithmNodeDescription.setValue(nodeDescription);
    }
    public MutableLiveData<AlgorithmDescription> getAlgorithmNodeDescription() {
        return algorithmNodeDescription;
    }
    public void selectedNewNode(int id){
        navigator.openAlgorithm(id);
    }
    //region Properties
    public MutableLiveData<Integer> getSelectedItemId() {
        return selectedItemId;
    }
    //endregion


}
