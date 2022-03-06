package com.moh.clinicalguideline.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.moh.clinicalguideline.data.entities.NodeRelation;

import java.util.List;

@Dao
public interface NodeRelationDao {

    @Query("SELECT * FROM NodeRelation WHERE Id =:id LIMIT 1")
    NodeRelation getNodeRelation(int id);

    @Query("SELECT * FROM NodeRelation Where ParentNodeId =:parentNodeId")
    List<NodeRelation> getNodeRelations(int parentNodeId);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(NodeRelation... nodes);

}
