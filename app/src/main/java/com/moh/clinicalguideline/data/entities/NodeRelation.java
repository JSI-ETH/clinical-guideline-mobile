package com.moh.clinicalguideline.data.entities;


import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {
        @Index(value = {"ChildNodeId"}),
        @Index(value = {"ParentNodeId"})},
        foreignKeys =
        { @ForeignKey(entity = Node.class,
                parentColumns = "Id",
                childColumns = "ChildNodeId"
        ),@ForeignKey(entity = Node.class,
                parentColumns = "Id",
                childColumns = "ParentNodeId"
        )})
public class NodeRelation {

    @PrimaryKey(autoGenerate = false)
    private int Id;
    private int ChildNodeId;
    private int ParentNodeId;

    public int getId() {

        return Id;
    }

    public void setId(int id) {

        Id = id;
    }

    public int getChildNodeId() {
        return ChildNodeId;
    }

    public void setChildNodeId(int childNodeId) {
        ChildNodeId = childNodeId;
    }

    public int getParentNodeId() {
        return ParentNodeId;
    }

    public void setParentNodeId(int parentNodeId) {
        ParentNodeId = parentNodeId;
    }

}
