package com.moh.clinicalguideline.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.moh.clinicalguideline.data.entities.Node;
import com.moh.clinicalguideline.data.entities.NodeType;

import java.util.List;

@Dao
public interface NodeTypeDao {

    @Query("SELECT * FROM NodeType WHERE Id =:id LIMIT 1")
    NodeType getNodeType(int id);

    @Query("SELECT * FROM NodeType")
    List<NodeType> getNodeTypes();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(NodeType... nodes);

}
