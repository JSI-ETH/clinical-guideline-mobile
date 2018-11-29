package com.moh.clinicalguideline.repository;

import com.moh.clinicalguideline.core.AlgorithmDescription;
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
         n2.setId(3);
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
         n1.setId(2);
         n1.setTitle("Stomach pain");
         list.add(n1);
         NodeDescription n2 = new NodeDescription();
         n2.setId(3);
         n2.setTitle("Cough");
         list.add(n2);

         return Observable.just(list);
      }).subscribeOn(Schedulers.io());
   }

   @Override
   public Observable<AlgorithmDescription> getNode(int nodeId) {
      return Observable.defer(() -> {
         AlgorithmDescription item = new AlgorithmDescription();
         item.setDescription("This is the description test");
         item.setTitle("Back Pain");
         item.setId(1);
         item.setNodeTypeCode("ASMPT");


         return Observable.just(item);
      }).subscribeOn(Schedulers.io());
   }

   @Override
   public Observable<List<AlgorithmDescription>> getChildNode(int parentNodeId) {
      return Observable.defer(() -> {
         List<AlgorithmDescription> list = nodeDao.getNodesWithDescriptionByParentId(parentNodeId);
         AlgorithmDescription item = new AlgorithmDescription();
         item.setDescription("A material metaphor is the unifying theory of a rationalized space and a system of motion." +
                 "The material is grounded in tactile reality, inspired by the study of paper and ink, yet " +
                 "technologically advanced and open to imagination and magic.\n" +
                 "Surfaces and edges of the material provide visual cues that are grounded in reality. The " +
                 "use of familiar tactile attributes helps users quickly understand affordances. Yet the " +
                 "flexibility of the material creates new affordances that supercede those in the physical " +
                 "world, without breaking the rules of physics.\n" +
                 "The fundamentals of light, surface, and movement are key to conveying how objects move, " +
                 "interact, and exist in space and in relation to each other. Realistic lighting shows " +
                 "seams, divides space, and indicates moving parts.\n\n");
         item.setTitle("Urgent");
         item.setId(1);
         item.setNodeTypeCode("URGNT");

         AlgorithmDescription item1 = new AlgorithmDescription();
         item1.setDescription("This is the description test");
         item1.setTitle("Not Urgent");
         item1.setId(1);
         item1.setNodeTypeCode("NTURG");
         list.add(item);
         list.add(item1);
         return Observable.just(list);
      }).subscribeOn(Schedulers.io());
   }
}
