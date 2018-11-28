package com.moh.clinicalguideline.data;

import android.arch.persistence.room.Room;
import android.content.Context;

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
}
