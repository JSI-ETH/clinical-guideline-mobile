package com.moh.clinicalguideline.core;

import java.util.UUID;

public class AlgorithmDescription {
    private int Id;
    private String Title;
    private String Description;

    private String NodeTypeCode;
    private UUID rowguid;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
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
