package com.moh.clinicalguideline.views.algorithm;


import com.moh.clinicalguideline.core.AlgorithmDescription;

public class AlgorithmCardViewModel {

    private AlgorithmDescription node;

    public AlgorithmCardViewModel(AlgorithmDescription node){
        this.node = node;
    }

    public int getId(){
        if(getHasDescription() || getChildCount()>1 || getFirstChildNodeId() == null)
        {
            return node.getId();
        }
        return node.getFirstChildNodeId();
    }

    public String getTitle() {
        return node.getTitle();
    }

    public String getDescription() {
        return node.getDescription();
    }

    public boolean getHasDescription() {
        return node.getHasDescription();
    }

    public int getChildCount(){
         return node.getChildCount();
    }

    private Integer getFirstChildNodeId(){
        return node.getFirstChildNodeId();
    }

    public boolean getHasTitle(){
        return node.getHasTitle();
    }

    public boolean getUrgent () {
        return node.getNodeTypeCode().equalsIgnoreCase("URGNT");

    }
    public boolean getUrgentOrNonUrgent () {
        return node.isNonUrgent() || node.isUrgent();

    }
    public AlgorithmDescription getNode() {
        return node;
    }
}
