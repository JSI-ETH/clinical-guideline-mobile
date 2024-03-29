package com.moh.clinicalguideline.data.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity(indices = {
    @Index(value = {"NodeTypeId"})},
        foreignKeys = {
    @ForeignKey(entity = NodeType.class,
        parentColumns = "Id",
        childColumns = "NodeTypeId")})
public class Node {

    @PrimaryKey(autoGenerate = false)
    private int Id;
    private String NodeName;
    private int NodeTypeId;
    private float Page;
    private UUID rowguid;

    public int getId() {
        return Id;
    }

    public void setId(int nodeId) {
        Id = nodeId;
    }

    public String getNodeName() {
        return NodeName;
    }

    public void setNodeName(String nodeName) {
        NodeName = nodeName;
    }

    public int getNodeTypeId() {
        return NodeTypeId;
    }

    public void setNodeTypeId(int nodeTypeId) {
        NodeTypeId = nodeTypeId;
    }

    public float getPage() {
        return Page;
    }

    public void setPage(float page) {
        Page = page;
    }

    public UUID getRowguid() {
        return rowguid;
    }

    public void setRowguid(UUID rowguid) {
        this.rowguid = rowguid;
    }
}
