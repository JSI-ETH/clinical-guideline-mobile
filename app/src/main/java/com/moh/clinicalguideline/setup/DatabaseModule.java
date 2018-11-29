package com.moh.clinicalguideline.setup;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.moh.clinicalguideline.data.CgDatabase;
import com.moh.clinicalguideline.data.dao.NodeDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
@Singleton
public class DatabaseModule {

    private static CgDatabase db = null;

    @Provides
    public static CgDatabase provideDatabase(Context context){
        if(db==null){
            db = Room.databaseBuilder(context,CgDatabase.class,"cg-db")
                    .build();
        }
        return db;
    }

    @Provides
    static NodeDao providesLookupDao(CgDatabase db)
    {
        return db.getNodeDao();
    }

}
