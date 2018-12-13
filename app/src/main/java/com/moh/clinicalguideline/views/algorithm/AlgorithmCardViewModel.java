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

    public Boolean getHasDescription() {
        return !algorithmDescription.getDescription().isEmpty();
    }



    public Boolean getHasContent(){

        return !(algorithmDescription.getDescription().isEmpty()&& algorithmDescription.getTitle().isEmpty());
        //or if the title is yes or no then no content!
        //
    }

    public Boolean getHasTitle(){
        return !algorithmDescription.getTitle().isEmpty();
    }

    public Boolean getUrgent () {
        Log.e("AlgorithimCardViewModel",algorithmDescription.getNodeTypeCode());
        return algorithmDescription.getNodeTypeCode().equalsIgnoreCase("URGNT");

    }
    public Boolean getHasConditional () {
        return algorithmDescription.getTitle().equalsIgnoreCase("Yes") || algorithmDescription.getTitle().equalsIgnoreCase("No") ;
    }

}
