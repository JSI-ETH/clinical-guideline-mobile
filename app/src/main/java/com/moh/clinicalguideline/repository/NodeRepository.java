package com.moh.clinicalguideline.repository;

import com.moh.clinicalguideline.data.entities.Node;
import com.moh.clinicalguideline.data.entities.NodeDescription;

import java.util.List;
import io.reactivex.Observable;

public interface NodeRepository {

    //getSymptom by NodeTypeCode
    Observable<List<NodeDescription>> getAdultSymptom();
    Observable<List<NodeDescription>> getChildSymptom();
    Observable<List<NodeDescription>> getUrgent(int nodeId);
    Observable<List<NodeDescription>> getNonUrgent(int nodeId);
    Observable<NodeDescription> getNode(int nodeId);
    Observable<List<NodeDescription>> getChildNode(int nodeId);

}
