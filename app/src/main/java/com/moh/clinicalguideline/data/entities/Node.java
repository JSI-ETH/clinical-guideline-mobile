package com.moh.clinicalguideline.data.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

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
    private int Page;
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

    public int getPage() {
        return Page;
    }

    public void setPage(int page) {
        Page = page;
    }

    public UUID getRowguid() {
        return rowguid;
    }

    public void setRowguid(UUID rowguid) {
        this.rowguid = rowguid;
    }
}
