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

    @Query("Select n.Id,\n" +
            "  ifnull(nd.Title,n.NodeName) Title,\n" +
            "  ifnull(nd.Description,'') Description,\n" +
            "  ifnull(nd.IsCondition,0) IsCondition,\n" +
            "  nd.rowguid, \n" +
            "  nt.NodeTypeCode \n," +
            " CASE When ifnull(nd.Title, '') = ''  Then 1 else 0 END HasTitle,  \n"+
            " CASE When ifnull(nd.Description, '') = ''  Then 1 else 0 END HasDescription  \n"+
            "    from node n \n" +
                "    Join nodetype nt on n.NodeTypeId = nt.Id\n" +
                "    Left join nodeDescription nd on nd.id = n.Id\n" +
            "    Where nt.NodeTypeCode =:code " +
            "Order by ifnull(nd.Title,n.NodeName)")
    List<AlgorithmDescription> getNodesWithDescriptionByNodeTypeCode(String code);

    @Query("Select  n.Id,\n" +
            "  ifnull(nd.Title,n.NodeName) Title,\n" +
            "  ifnull(nd.Description,'') Description,\n" +
            "  ifnull(nd.IsCondition,0) IsCondition,\n" +
            "  nd.rowguid, \n" +
            "nt.NodeTypeCode, \n" +
            " CASE When ifnull(nd.Title, '') = ''  Then 1 else 0 END HasTitle,  \n"+
            " CASE When ifnull(nd.Description, '') = ''  Then 1 else 0 END HasDescription  \n"+
            "    From noderelation nr \n" +
            "       Join node n on n.id = nr.ChildNodeId" +
            "       Join nodeType nt on nt.id = n.NodeTypeId" +
            "       left Join nodeDescription nd on nd.id = nr.ChildNodeId\n" +
            "    Where nr.ParentNodeId =:parentId and nd.IsCondition =:isConditional")
    List<AlgorithmDescription> getNodesWithDescriptionByParentId(int parentId,boolean isConditional);

    @Query("Select  n.Id,\n" +
            "  ifnull(nd.Title,n.NodeName) Title,\n" +
            "  ifnull(nd.Description,'') Description,\n" +
            "  ifnull(nd.IsCondition,0) IsCondition,\n" +
            "  nd.rowguid, \n" +
            "  nt.NodeTypeCode, \n"  +
            " CASE When ifnull(nd.Title, '') = ''  Then 1 else 0 END HasTitle,  \n"+
            " CASE When ifnull(nd.Description, '') = ''  Then 1 else 0 END HasDescription  \n"+
            "    From noderelation nr \n" +
            "       Join node n on n.id = nr.ChildNodeId" +
            "       Join nodeType nt on nt.id = n.NodeTypeId" +
            "       left Join nodeDescription nd on nd.id = nr.ChildNodeId\n" +
            "    Where nr.ParentNodeId =:parentId ")
    List<AlgorithmDescription> getNodesWithDescriptionByParentId(int parentId);

    @Query("Select  n.Id,\n" +
            "  ifnull(nd.Title,n.NodeName) Title,\n" +
            "  ifnull(nd.Description,'') Description,\n" +
            "  ifnull(nd.IsCondition,0) IsCondition,\n" +
            "  nd.rowguid, \n" +
            "  nt.NodeTypeCode, \n" +
            " CASE When ifnull(nd.Title, '') = ''  Then 1 else 0 END HasTitle,  \n"+
            " CASE When ifnull(nd.Description, '') = ''  Then 1 else 0 END HasDescription  \n" +
            "    From node n \n" +
            "       Join nodeType nt on nt.id = n.NodeTypeId" +
            "       Left Join nodeDescription nd on n.id = nd.Id" +
            "    Where n.Id =:parentId")
    AlgorithmDescription getNodesWithDescription(int parentId);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Node... nodes);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    void update(Node... nodes);
}
