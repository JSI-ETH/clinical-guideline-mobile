package com.moh.clinicalguideline.data.migration;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.util.Log;
import com.moh.clinicalguideline.data.CgDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseCopier {
    private static final String TAG = DatabaseCopier.class.getSimpleName();
    private static final String DATABASE_NAME = "cg.db";

    private CgDatabase mAppDataBase;
    private static Context appContext;

    private static class Holder {
        private static final DatabaseCopier INSTANCE = new DatabaseCopier();
    }

    public static DatabaseCopier builder(Context context) {
        appContext = context;
        return Holder.INSTANCE;
    }
    private DatabaseCopier(){

    }
    public CgDatabase build() {
        //call method that check if database not exists and copy prepopulated file from assets
        copyAttachedDatabase(appContext, DATABASE_NAME);
        mAppDataBase = Room.databaseBuilder(appContext,
                CgDatabase.class, DATABASE_NAME)
                .addMigrations(new Migration_1_2())
                .build();
        return mAppDataBase;
    }

    public CgDatabase getRoomDatabase() {
        return mAppDataBase;
    }


    private void copyAttachedDatabase(Context context, String databaseName) {
        final File dbPath = context.getDatabasePath(databaseName);

        // If the database already exists, return
        if (dbPath.exists()) {
            return;
        }

        // Make sure we have a path to the file
        dbPath.getParentFile().mkdirs();

        // Try to copy database file
        try {
            final InputStream inputStream = context.getAssets().open("databases/" + databaseName);
            final OutputStream output = new FileOutputStream(dbPath);

            byte[] buffer = new byte[8192];
            int length;

            while ((length = inputStream.read(buffer, 0, 8192)) > 0) {
                output.write(buffer, 0, length);
            }

            output.flush();
            output.close();
            inputStream.close();
        }
        catch (IOException e) {
            Log.d(TAG, "Failed to open file", e);
            e.printStackTrace();
        }
    }

}
