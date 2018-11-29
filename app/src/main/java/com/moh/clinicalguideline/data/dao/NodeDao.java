package com.moh.clinicalguideline.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.moh.clinicalguideline.core.AlgorithmDescription;
import com.moh.clinicalguideline.data.entities.Node;
import com.moh.clinicalguideline.data.entities.NodeDescription;

import java.util.List;

import io.reactivex.Observable;

@Dao
public interface NodeDao {

    @Query("Select nd.* \n" +
            "    from node n \n" +
            "    Join nodetype nt on n.NodeTypeId = nt.Id\n" +
            "    join nodeDescription nd on nd.id = n.Id\n" +
            "    Where nt.NodeTypeCode =:code ")
    List<NodeDescription> getNodesWithDescriptionByNodeTypeCode(String code);

    @Query("Select nd.* \n" +
            "    From noderelation nr \n" +
            "       Join node n on n.Id = nr.ChildNodeId " +
            "       Join nodetype nt on n.NodeTypeId = nt.Id\n" +
            "       Join nodeDescription nd on nd.id = n.Id\n" +
            "    Where nt.NodeTypeCode =:code and nr.ParentNodeId =:parentId")
    List<NodeDescription> getNodesWithDescriptionByParentIdAndNodeTypeCode(int parentId,String code);

    @Query("Select nd.*,nt.NodeTypeCode \n" +
            "    From noderelation nr \n" +
            "       Join nodeDescription nd on nd.id = nr.ChildNodeId\n" +
            "       Join node n on n.id = nr.ChildNodeId" +
            "       Join nodeType nt on nt.id = n.NodeTypeId" +
            "    Where nr.ParentNodeId =:parentId")
    List<AlgorithmDescription> getNodesWithDescriptionByParentId(int parentId);

    @Query("Select nd.*,nt.NodeTypeCode\n" +
            "    From nodeDescription nd \n" +
            "       Join node n on n.id = nd.Id" +
            "       Join nodeType nt on nt.id = n.NodeTypeId" +
            "    Where nd.Id =:parentId")
    AlgorithmDescription getNodesWithDescription(int parentId);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Node... nodes);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    void update(Node... nodes);
}
