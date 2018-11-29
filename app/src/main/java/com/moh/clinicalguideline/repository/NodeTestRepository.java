package com.moh.clinicalguideline.repository;

import com.moh.clinicalguideline.data.dao.NodeDao;
import com.moh.clinicalguideline.data.entities.NodeDescription;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

public class NodeTestRepository implements NodeRepository {
   public static String ADULT_SYMPTOM ="ASMPT";
   public static String CHILD_SYMPTOM ="CSMPT";
   public static String URGENT ="URGNT";
   public static String NOT_URGENT ="NTURG";
   public static String ALGORITHM ="ALGTM";
   private final NodeDao nodeDao;

   @Inject
   public NodeTestRepository(NodeDao nodeDao){
      this.nodeDao = nodeDao;
   }

   @Override
   public Observable<List<NodeDescription>> getAdultSymptom() {
      return Observable.defer(() -> {
         List<NodeDescription> list = new ArrayList<>();
         NodeDescription n1 = new NodeDescription();
         n1.setId(1);
         n1.setTitle("Back pain");
         list.add(n1);
         NodeDescription n2 = new NodeDescription();
         n2.setId(1);
         n2.setTitle("Abused Patient / Traumatised");
         list.add(n2);

         return Observable.just(list);
      }).subscribeOn(Schedulers.io());
   }
   @Override
   public Observable<List<NodeDescription>> getChildSymptom() {
      return Observable.defer(() -> {
         List<NodeDescription> list = new ArrayList<>();
         NodeDescription n1 = new NodeDescription();
         n1.setId(1);
         n1.setTitle("Stomach pain");
         list.add(n1);
         NodeDescription n2 = new NodeDescription();
         n2.setId(1);
         n2.setTitle("Cough");
         list.add(n2);

         return Observable.just(list);
      }).subscribeOn(Schedulers.io());
   }
   @Override
   public Observable<List<NodeDescription>> getUrgent(int parentNodeId) {
      return Observable.defer(() -> {
         List<NodeDescription> list = nodeDao.getNodesWithDescriptionByParentIdAndNodeTypeCode(parentNodeId,URGENT);
         return Observable.just(list);
      }).subscribeOn(Schedulers.io());
   }

   @Override
   public Observable<List<NodeDescription>>getNonUrgent(int parentNodeId) {
      return Observable.defer(() -> {
         List<NodeDescription> list = nodeDao.getNodesWithDescriptionByParentIdAndNodeTypeCode(parentNodeId,NOT_URGENT);
         return Observable.just(list);
      }).subscribeOn(Schedulers.io());
   }

   @Override
   public Observable<NodeDescription> getNode(int nodeId) {
      return Observable.defer(() -> {
         NodeDescription item = nodeDao.getNodesWithDescription(nodeId);
         return Observable.just(item);
      }).subscribeOn(Schedulers.io());
   }

   @Override
   public Observable<List<NodeDescription>> getChildNode(int parentNodeId) {
      return Observable.defer(() -> {
         List<NodeDescription> list = nodeDao.getNodesWithDescriptionByParentId(parentNodeId);
         return Observable.just(list);
      }).subscribeOn(Schedulers.io());
   }
}
