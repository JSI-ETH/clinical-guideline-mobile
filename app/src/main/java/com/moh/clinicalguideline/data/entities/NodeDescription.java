package com.moh.clinicalguideline.data.entities;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

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
    private UUID rowguid;
    private boolean IsFavorite;

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

    public UUID getRowguid() {
        return rowguid;
    }

    public void setRowguid(UUID rowguid) {
        this.rowguid = rowguid;
    }

    public boolean isFavorite() {
        return IsFavorite;
    }

    public void setFavorite(boolean favorite) {
        IsFavorite = favorite;
    }
}
