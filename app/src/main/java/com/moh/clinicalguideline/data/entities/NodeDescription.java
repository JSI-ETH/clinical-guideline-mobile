package com.moh.clinicalguideline.data.entities;


import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.annotation.Nullable;

import java.util.UUID;

@Entity(indices = {
        @Index(value = {"Id"}),
        },
        foreignKeys =
                { @ForeignKey(entity = Node.class,
                        parentColumns = "Id",
                        childColumns = "Id"
                )})
public class NodeDescription {
    @PrimaryKey(autoGenerate = false)
    private int Id;
    private String Title;
    private String Description;
    private boolean IsCondition;
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

    public boolean getIsCondition() {
        return IsCondition;
    }

    public void setIsCondition(boolean condition) {
        IsCondition = condition;
    }
    public UUID getRowguid() {
        return rowguid;
    }

    public void setRowguid(UUID rowguid) {
        this.rowguid = rowguid;
    }

}
