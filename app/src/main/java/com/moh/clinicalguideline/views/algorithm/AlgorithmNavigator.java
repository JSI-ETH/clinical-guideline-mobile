package com.moh.clinicalguideline.views.algorithm;

public interface AlgorithmNavigator {
    void openAlgorithm(int nodeId,int parentNodeId);

    void returnToPrevious(int parentNodeId);
    void returnHome();

}