package com.moh.clinicalguideline.views.algorithm;

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

public class AlgorithmViewModel extends ViewModel<AlgorithmNavigator> {

    private CardAdapter<AlgorithmDescription> adapter;
    private final NodeRepository nodeRepository;
    private AlgorithmDescription nodeDescription;
    private List<AlgorithmDescription> nodeDescriptions;
    private boolean loading;
    private int parentId;

    @Inject
    public AlgorithmViewModel(NodeRepository nodeRepository){

        this.nodeRepository = nodeRepository;
         this.adapter = new CardAdapter<>(R.layout.activity_algorithm_list, item -> {
                navigator.openAlgorithm(item.getId(),nodeDescription.getId());
         });
    }
    public void loadNode(int nodeId,int parentId) {
        setLoading(true);
        parentId = parentId;
        loadAlgorithmDescription(nodeId);
        loadChildNode(nodeId);
    }
    public void loadAlgorithmDescription(int nodeId){
        setLoading(true);
        nodeRepository.getNode(nodeId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::OnAlgorithmNodeLoaded,this::onLoadError);
    }

    public void loadChildNode(int nodeId) {
        setLoading(true);
        nodeRepository.getChildNode(nodeId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::OnChildNodeLoaded,this::onLoadError);
    }


    public CardAdapter<AlgorithmDescription> getAdapter(){
         return adapter;
    }

    private void OnAlgorithmNodeLoaded(AlgorithmDescription nodeDescription) {
        setLoading(false);
        this.nodeDescription = nodeDescription;
        notifyChange();
    }
    private void OnChildNodeLoaded(List<AlgorithmDescription> nodeDescriptionList) {
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

    public String getTitle(){
        if(nodeDescription==null)
            return "";
        return nodeDescription.getTitle();
    }
    public String getDescription(){
        if(nodeDescription==null)
            return "";
        return nodeDescription.getDescription();
    }
    public int getParentId() {
        return parentId;
    }
}
