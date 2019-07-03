package com.moh.clinicalguideline.repository;

import com.moh.clinicalguideline.core.AlgorithmDescription;
import com.moh.clinicalguideline.data.entities.Node;
import com.moh.clinicalguideline.data.entities.NodeDescription;

import java.util.List;
import io.reactivex.Observable;

public interface NodeRepository {

    //getSymptom by NodeTypeCode
   Observable<List<AlgorithmDescription>> getAdultSymptom();
   Observable<List<AlgorithmDescription>> getChildSymptom();
   Observable<List<AlgorithmDescription>> getChronicCare();
   Observable<List<AlgorithmDescription>> getAllSymptoms();
   Observable<List<AlgorithmDescription>> getChildNode(int nodeId);

    Observable<AlgorithmDescription> getNodeByPage(double pageId);

    Observable<List<AlgorithmDescription>> getChildNode(int nodeId, boolean isConditional);
   Observable<AlgorithmDescription>getNode(int nodeId);

}
