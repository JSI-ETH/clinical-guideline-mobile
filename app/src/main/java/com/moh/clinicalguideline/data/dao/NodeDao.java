package com.moh.clinicalguideline.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.moh.clinicalguideline.data.entities.Node;

import java.util.List;

@Dao
public interface NodeDao {

    @Query("SELECT * FROM Node WHERE Id =:id LIMIT 1")
    Node getNode(int id);

    @Query("SELECT * FROM Node Where NodeTypeId =:nodeTypeId")
    List<Node> getNodes(int nodeTypeId);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Node... nodes);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    void update(Node... nodes);
}
