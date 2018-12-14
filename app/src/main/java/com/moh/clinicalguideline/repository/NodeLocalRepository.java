package com.moh.clinicalguideline.repository;

import com.moh.clinicalguideline.core.AlgorithmDescription;
import com.moh.clinicalguideline.data.dao.NodeDao;
import com.moh.clinicalguideline.data.entities.NodeDescription;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

public class NodeLocalRepository implements NodeRepository {
        public static String ADULT_SYMPTOM ="ASMPT";
        public static String CHILD_SYMPTOM ="CHRNC";
        public static String URGENT ="URGNT";
        public static String NOT_URGENT ="NTURG";
        public static String ALGORITHM ="ALGTM";
    private final NodeDao nodeDao;

    @Inject
    public NodeLocalRepository(NodeDao nodeDao){
        this.nodeDao = nodeDao;
    }

    @Override
    public Observable<List<AlgorithmDescription>> getAdultSymptom() {
        return Observable.defer(() -> {
            List<AlgorithmDescription> list = nodeDao.getNodesWithDescriptionByNodeTypeCode(ADULT_SYMPTOM);
            return Observable.just(list);
        }).subscribeOn(Schedulers.io());
    }
    @Override
    public Observable<List<AlgorithmDescription>> getChildSymptom() {
        return Observable.defer(() -> {
            List<AlgorithmDescription> list = nodeDao.getNodesWithDescriptionByNodeTypeCode(CHILD_SYMPTOM);
            return Observable.just(list);
            }).subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<List<AlgorithmDescription>> getChronicCare() {
        return Observable.defer(() -> {
            List<AlgorithmDescription> list = nodeDao.getNodesWithDescriptionByNodeTypeCode(CHILD_SYMPTOM);
            return Observable.just(list);
        }).subscribeOn(Schedulers.io());
    }
    @Override
    public Observable<AlgorithmDescription> getNode(int nodeId) {
        return Observable.defer(() -> {
            AlgorithmDescription item = nodeDao.getNodesWithDescription(nodeId);
            return Observable.just(item);
        }).subscribeOn(Schedulers.io());
    }
    @Override
    public Observable<AlgorithmDescription> getNodeByPage(int pageId) {
        return Observable.defer(() -> {
            AlgorithmDescription item = nodeDao.getNodesWithDescriptionByPage(pageId);
            return Observable.just(item);
        }).subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<List<AlgorithmDescription>> getChildNode(int parentNodeId,boolean isConditional) {
        return Observable.defer(() -> {
            List<AlgorithmDescription> list = nodeDao.getNodesWithDescriptionByParentId(parentNodeId,isConditional);
            return Observable.just(list);
        }).subscribeOn(Schedulers.io());
    }
    @Override
    public Observable<List<AlgorithmDescription>> getChildNode(int parentNodeId) {
        return Observable.defer(() -> {
            List<AlgorithmDescription> list = nodeDao.getNodesWithDescriptionByParentId(parentNodeId);
            return Observable.just(list);
        }).subscribeOn(Schedulers.io());
    }
}
