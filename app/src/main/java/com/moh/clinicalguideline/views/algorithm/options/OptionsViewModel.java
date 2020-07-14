package com.moh.clinicalguideline.views.algorithm.options;

import android.annotation.SuppressLint;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.Bindable;

import com.moh.clinicalguideline.BR;
import com.moh.clinicalguideline.helper.view.BaseViewModel;
import com.moh.clinicalguideline.repository.NodeRepository;

import com.moh.clinicalguideline.core.AlgorithmDescription;
import com.moh.clinicalguideline.views.algorithm.AlgorithmCardViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class OptionsViewModel extends BaseViewModel {
    private final NodeRepository nodeRepository;
    private MutableLiveData<List<AlgorithmCardViewModel>> nodes;
    private MutableLiveData<AlgorithmDescription> selectedNode;
    private boolean isSingleNode;

    @Inject
    public OptionsViewModel(NodeRepository nodeRepository) {
        this.nodeRepository = nodeRepository;
        nodes = new MutableLiveData<>();
        selectedNode = new MutableLiveData<>();
    }

    //region Bindable Properties
    public MutableLiveData<List<AlgorithmCardViewModel>> getNodes() {
        return nodes;
    }

    @Bindable
    public boolean getIsSingleNode() {
        return isSingleNode;
    }

    public void setSingleNode(boolean singleNode) {
        isSingleNode = singleNode;
        notifyPropertyChanged(BR.isSingleNode);
    }

    public MutableLiveData<AlgorithmDescription> getSelectedNode() {
        return selectedNode;
    }
    //endregion

    //region Events
    //region DataLoader
    @SuppressLint("CheckResult")
    public void loadNodes(int parentId) {
        nodeRepository.getChildNode(parentId, false)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onLoaded, this::onLoadError);
    }

    private void onLoaded(List<AlgorithmDescription> nodeDescriptionList) {

        List<AlgorithmCardViewModel> algorithmNodeViewModels = new ArrayList();
        for (AlgorithmDescription aNodeDescription : nodeDescriptionList) {
//            algorithmNodeViewModels.add(new AlgorithmCardViewModel(aNodeDescription));
        }

        nodes.setValue(algorithmNodeViewModels);
        setSingleNode(algorithmNodeViewModels.size()==1);
    }

    //endregion
    public void selectNode(AlgorithmDescription algorithmDescription) {
        selectedNode.setValue(algorithmDescription);
    }

    public void selectFirstNode() {
        if (nodes.getValue() != null && !nodes.getValue().isEmpty()) {
            AlgorithmDescription firstNode = nodes.getValue().get(0).getNode();
            selectNode(firstNode);
        }
    }
    //endregion
}
