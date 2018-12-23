package com.moh.clinicalguideline.views.algorithm.timeline;

import com.moh.clinicalguideline.core.AlgorithmDescription;

public class TimeLineNodeViewModel {

    private AlgorithmDescription node;
    private int positionId;
    public TimeLineNodeViewModel(AlgorithmDescription node, int positionId){
        this.node = node;
        this.positionId = positionId;
    }

    public int getId(){

            return node.getId();
    }

    public String getTitle() {
        return node.getTitle();
    }

    public String getDescription() {
        return node.getDescription();
    }

    public boolean isActive() {return node.getHasDescription() || node.getChildCount()>1 || node.getFirstChildNodeId() == null;}

    public boolean isSymptom () {
        return node.getNodeTypeCode().equalsIgnoreCase("URGNT");

    }

    public int getPositionId() {
        return positionId;
    }

    public AlgorithmDescription getNode() {
        return node;
    }

}
