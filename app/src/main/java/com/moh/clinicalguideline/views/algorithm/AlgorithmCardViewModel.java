package com.moh.clinicalguideline.views.algorithm;

import android.util.Log;

import com.moh.clinicalguideline.core.AlgorithmDescription;

public class AlgorithmCardViewModel {

    private AlgorithmDescription algorithmDescription;

    public AlgorithmCardViewModel(AlgorithmDescription algorithmDescription){
        this.algorithmDescription = algorithmDescription;
    }

    public int getId(){
        if(getHasDescription() || getChildCount()>1 || getFirstChildNodeId() == null)
        {
            return algorithmDescription.getId();
        }
        return algorithmDescription.getFirstChildNodeId();
    }

    public String getTitle() {
        return algorithmDescription.getTitle();
    }

    public String getDescription() {
        return algorithmDescription.getDescription();
    }

    public boolean getHasDescription() {
        return algorithmDescription.getHasDescription();
    }

    public int getChildCount(){
         return algorithmDescription.getChildCount();
    }

    private Integer getFirstChildNodeId(){
        return algorithmDescription.getFirstChildNodeId();
    }

    public boolean getHasTitle(){
        return algorithmDescription.getHasTitle();
    }

    public boolean getUrgent () {
        return algorithmDescription.getNodeTypeCode().equalsIgnoreCase("URGNT");

    }
}
