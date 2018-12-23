package com.moh.clinicalguideline.views.algorithm.answers;

import android.arch.lifecycle.MutableLiveData;

import com.moh.clinicalguideline.core.AlgorithmDescription;
import com.moh.clinicalguideline.helper.view.BaseViewModel;
import com.moh.clinicalguideline.repository.NodeRepository;
import com.moh.clinicalguideline.views.algorithm.AlgorithmCardViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class AnswersViewModel extends BaseViewModel {
    private final NodeRepository nodeRepository;
    private MutableLiveData<List<AlgorithmCardViewModel>> nodes;
    private MutableLiveData<Integer> selectedId;

    @Inject
    public AnswersViewModel(NodeRepository nodeRepository) {
        this.nodeRepository = nodeRepository;
        nodes = new MutableLiveData<>();
        selectedId = new MutableLiveData<>();
    }

    //region Bindable Properties
    public MutableLiveData<List<AlgorithmCardViewModel>> getNodes() {
        return nodes;
    }


    public MutableLiveData<Integer> getSelectedId() {
        return selectedId;
    }
    //endregion

    //region Events
    //region DataLoader
    public void loadNodes(int parentId) {
        nodeRepository.getChildNode(parentId, true)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onLoaded, this::onLoadError);
    }

    private void onLoaded(List<AlgorithmDescription> nodeDescriptionList) {
        List<AlgorithmCardViewModel> algorithmNodeViewModels = new ArrayList();
        for (AlgorithmDescription aNodeDescription : nodeDescriptionList) {
            algorithmNodeViewModels.add(new AlgorithmCardViewModel(aNodeDescription));
        }

        nodes.setValue(algorithmNodeViewModels);
    }

    //endregion
    public void selectNode(int nodeId) {
        selectedId.setValue(nodeId);
    }
    //endregion
}
