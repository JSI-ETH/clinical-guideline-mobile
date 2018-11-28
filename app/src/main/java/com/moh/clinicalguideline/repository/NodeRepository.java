package com.moh.clinicalguideline.repository;

import com.moh.clinicalguideline.data.entities.Node;
import com.moh.clinicalguideline.data.entities.NodeDescription;

import java.util.List;
import io.reactivex.Observable;

public interface NodeRepository {

    //getSymptom by NodeTypeCode
    List<NodeDescription> getAdultSymptom();
    List<NodeDescription> getChildSymptom();
    List<NodeDescription> getUrgent(int nodeId);
    List<NodeDescription> getNonUrgent(int nodeId);
    NodeDescription getNode(int nodeId);
    List<NodeDescription> getChildNode(int nodeId);

}
