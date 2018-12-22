package com.moh.clinicalguideline.views.algorithm;

public interface AlgorithmNavigator {
    void openAlgorithm(int nodeId);
    void openAlgorithmByPage(int pageId,int parentNodeId);
    void returnToPrevious(int parentNodeId);
    void returnHome();

}