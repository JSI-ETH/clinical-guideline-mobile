package com.moh.clinicalguideline.views.algorithm.fragment;

import android.arch.lifecycle.MutableLiveData;

import com.moh.clinicalguideline.core.AlgorithmDescription;
import com.moh.clinicalguideline.helper.view.BaseViewModel;
import com.moh.clinicalguideline.repository.NodeRepository;
import com.moh.clinicalguideline.views.algorithm.AlgorithmCardViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class OptionsViewModel extends BaseViewModel {
    private final NodeRepository nodeRepository;
    private MutableLiveData<List<AlgorithmCardViewModel>> nodes;
    private MutableLiveData<Integer> childNodeCount;
    @Inject
    public OptionsViewModel (NodeRepository nodeRepository){
        this.nodeRepository = nodeRepository;
        nodes = new MutableLiveData<>();
    }

    //region Bindable Properties
    public MutableLiveData<List<AlgorithmCardViewModel>> getNodes(){
        return nodes;
    }
    //endregion

    //region DataLoader
    public void loadNodes(int parentId) {
        nodeRepository.getChildNode(parentId,false)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onLoaded,this::onLoadError);
    }
    private void onLoaded(List<AlgorithmDescription> nodeDescriptionList) {

        List<AlgorithmCardViewModel> algorithmNodeViewModels = new ArrayList();
        for (AlgorithmDescription aNodeDescription: nodeDescriptionList) {
            algorithmNodeViewModels.add(new AlgorithmCardViewModel(aNodeDescription));
        }
        nodes.setValue(algorithmNodeViewModels);

    }
    //endregion
}
