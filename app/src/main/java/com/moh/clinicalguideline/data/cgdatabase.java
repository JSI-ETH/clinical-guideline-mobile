package com.moh.clinicalguideline.data;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.moh.clinicalguideline.data.dao.NodeDao;
import com.moh.clinicalguideline.data.dao.NodeTypeDao;
import com.moh.clinicalguideline.data.entities.Node;
import com.moh.clinicalguideline.data.entities.NodeType;

@Database(entities = {
        Node.class,
        NodeType.class},
 exportSchema = false,
         version = 1)
@TypeConverters({converters.class})
public abstract class cgdatabase extends RoomDatabase {
    public abstract NodeDao NodeDao();
    public abstract NodeTypeDao NodeTypeDao();
}