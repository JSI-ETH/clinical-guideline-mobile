package com.moh.clinicalguideline.views.algorithm.timeline;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.Bindable;
import android.databinding.Observable;
import android.databinding.ObservableList;

import com.moh.clinicalguideline.core.AlgorithmDescription;
import com.moh.clinicalguideline.helper.view.BaseViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class TimelineViewModel extends BaseViewModel {
    private List<TimeLineNodeViewModel> nodes;
    private MutableLiveData<TimeLineNodeViewModel> node;
    private int cursor;
    @Inject
    public TimelineViewModel() {

        this.nodes = new ArrayList<>();
        this.node = new MutableLiveData<>();
        this.cursor = 0;
    }

    public void onNodeSelected(AlgorithmDescription node) {
        nodes.subList(this.cursor, this.nodes.size()).clear();
        nodes.add(new TimeLineNodeViewModel(node, cursor));
        this.cursor++;
        notifyChange();
    }

    public void selectNode(int position) {
        this.cursor = position+1;
        this.node.setValue(nodes.get(position));
    }

    public MutableLiveData<TimeLineNodeViewModel> getNode() {
        return this.node;
    }
    @Bindable
    public List<TimeLineNodeViewModel> getNodes() {
        return nodes;
    }

}
