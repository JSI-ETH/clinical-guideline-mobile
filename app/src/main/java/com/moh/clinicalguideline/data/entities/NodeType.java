package com.moh.clinicalguideline.data.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity(indices = {
    @Index(value = {"NodeTypeCode"})})
public class NodeType {

    @PrimaryKey(autoGenerate = false)
    public int Id;
    public String NodeName;
    public String Description;
    public String NodeTypeCode;
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

    public String getNodeTypeCode() {
        return NodeTypeCode;
    }

    public void setNodeTypeCode(String nodeTypeCode) {
        NodeTypeCode = nodeTypeCode;
    }

    public UUID getRowguid() {
        return rowguid;
    }

    public void setRowguid(UUID rowguid) {
        this.rowguid = rowguid;
    }
}
