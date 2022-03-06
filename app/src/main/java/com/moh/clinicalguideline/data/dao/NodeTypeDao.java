package com.moh.clinicalguideline.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

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
