package com.moh.clinicalguideline.data;


import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.annotation.NonNull;

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
         version = 5)
@TypeConverters({Converters.class})
public abstract class CgDatabase extends RoomDatabase {
    public abstract NodeDao getNodeDao();
    public abstract NodeTypeDao getNodeTypeDao();
    public abstract NodeRelationDao getNodeRelationDao();


}
