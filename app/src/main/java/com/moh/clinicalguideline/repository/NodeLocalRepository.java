package com.moh.clinicalguideline.repository;

import com.moh.clinicalguideline.data.dao.NodeDao;
import com.moh.clinicalguideline.data.entities.NodeDescription;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

public class NodeLocalRepository implements NodeRepository {
        public static String ADULT_SYMPTOM ="ASMPT";
        public static String CHILD_SYMPTOM ="CSMPT";
        public static String URGENT ="URGNT";
        public static String NOT_URGENT ="NTURG";
        public static String ALGORITHM ="ALGTM";
    private final NodeDao nodeDao;

    @Inject
    public NodeLocalRepository(NodeDao nodeDao){
        this.nodeDao = nodeDao;
    }

    @Override
    public Observable<List<NodeDescription>> getAdultSymptom() {
        return nodeDao.getNodesWithDescriptionByNodeTypeCode(ADULT_SYMPTOM);
    }
    @Override
    public Observable<List<NodeDescription>> getChildSymptom() {
        return nodeDao.getNodesWithDescriptionByNodeTypeCode(CHILD_SYMPTOM);
    }
    @Override
    public Observable<List<NodeDescription>> getUrgent(int parentNodeId) {
        return nodeDao.getNodesWithDescriptionByParentIdAndNodeTypeCode(parentNodeId,URGENT);
    }

    @Override
    public Observable<List<NodeDescription>> getNonUrgent(int parentNodeId) {
        return nodeDao.getNodesWithDescriptionByParentIdAndNodeTypeCode(parentNodeId,NOT_URGENT);
    }

    @Override
    public Observable<NodeDescription> getNode(int nodeId) {
        return nodeDao.getNodesWithDescription(nodeId);
    }

    @Override
    public Observable<List<NodeDescription>> getChildNode(int parentNodeId) {
        return nodeDao.getNodesWithDescriptionByParentId(parentNodeId);
    }
}
