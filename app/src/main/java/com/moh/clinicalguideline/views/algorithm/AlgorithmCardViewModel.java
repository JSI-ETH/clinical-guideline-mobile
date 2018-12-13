package com.moh.clinicalguideline.views.algorithm;

import android.util.Log;

import com.moh.clinicalguideline.core.AlgorithmDescription;

public class AlgorithmCardViewModel {

    private AlgorithmDescription algorithmDescription;

    public AlgorithmCardViewModel(AlgorithmDescription algorithmDescription){
        this.algorithmDescription = algorithmDescription;
    }

    public int getId(){
        return algorithmDescription.getId();
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

    public boolean getHasContent(){
        return (algorithmDescription.getHasTitle() && algorithmDescription.getHasDescription());
    }

    public boolean getHasTitle(){
        return algorithmDescription.getHasTitle();
    }

    public boolean getUrgent () {
        Log.e("AlgorithimCardViewModel",algorithmDescription.getNodeTypeCode());
        return algorithmDescription.getNodeTypeCode().equalsIgnoreCase("URGNT");

    }

    public boolean getHasConditional () {
        return algorithmDescription.getIsCondition() ;
    }

}
