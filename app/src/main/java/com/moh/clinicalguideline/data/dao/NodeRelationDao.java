package com.moh.clinicalguideline.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.moh.clinicalguideline.data.entities.Node;
import com.moh.clinicalguideline.data.entities.NodeRelation;

import java.util.List;

@Dao
public interface NodeRelationDao {

    @Query("SELECT * FROM NodeRelation WHERE Id =:id LIMIT 1")
    NodeRelation getNodeRelation(int id);

    @Query("SELECT * FROM NodeRelation Where ParentNodeId =:parentNodeId")
    List<NodeRelation> getNodeRelations(int parentNodeId);
}
