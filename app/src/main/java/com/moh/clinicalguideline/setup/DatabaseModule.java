package com.moh.clinicalguideline.setup;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.moh.clinicalguideline.data.CgDatabase;
import com.moh.clinicalguideline.data.dao.NodeDao;
import com.moh.clinicalguideline.data.migration.DatabaseCopier;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DatabaseModule {

    private static CgDatabase db = null;

    @Provides

    @Singleton
    public static CgDatabase provideDatabase(Context context){
        if(db==null){
            db = DatabaseCopier.builder(context).build();
        }
        return db;
    }

    @Provides

    @Singleton
    static NodeDao providesLookupDao(CgDatabase db)
    {
        return db.getNodeDao();
    }

}
