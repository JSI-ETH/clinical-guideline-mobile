package com.moh.clinicalguideline.data.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.io.NotActiveException;
import java.util.UUID;

@Entity(indices = {
    @Index(value = {"NodeTypeCode"})})
public class NodeType {

    @PrimaryKey(autoGenerate = false)
    public int Id;
    public String NodeName;
    public String Description;
    public int NodeTypeCode;
    private UUID rowguid;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public String getNodeName() {
        return NodeName;
    }

    public void setNodeName(String nodeName) {
        NodeName = nodeName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getNodeTypeCode() {
        return NodeTypeCode;
    }

    public void setNodeTypeCode(int nodeTypeCode) {
        NodeTypeCode = nodeTypeCode;
    }

    public UUID getRowguid() {
        return rowguid;
    }

    public void setRowguid(UUID rowguid) {
        this.rowguid = rowguid;
    }
}
