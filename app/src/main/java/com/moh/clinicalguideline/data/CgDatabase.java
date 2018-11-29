package com.moh.clinicalguideline.data;


import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;
import android.support.annotation.NonNull;

import com.moh.clinicalguideline.data.dao.NodeDao;
import com.moh.clinicalguideline.data.dao.NodeRelationDao;
import com.moh.clinicalguideline.data.dao.NodeTypeDao;
import com.moh.clinicalguideline.data.entities.Node;
import com.moh.clinicalguideline.data.entities.NodeDescription;
import com.moh.clinicalguideline.data.entities.NodeRelation;
import com.moh.clinicalguideline.data.entities.NodeType;

@Database(entities = {
        Node.class,
        NodeType.class,
        NodeRelation.class,
        NodeDescription.class},
 exportSchema = false,
         version = 2)
@TypeConverters({Converters.class})
public abstract class CgDatabase extends RoomDatabase {
    public abstract NodeDao getNodeDao();
    public abstract NodeTypeDao getNodeTypeDao();
    public abstract NodeRelationDao getNodeRelationDao();


}
