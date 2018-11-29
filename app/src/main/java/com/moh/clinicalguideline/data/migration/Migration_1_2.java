package com.moh.clinicalguideline.data.migration;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.migration.Migration;
import android.support.annotation.NonNull;

public class Migration_1_2 extends Migration {

    public Migration_1_2(){
        super(1,2);
    }
    @Override
    public void migrate(@NonNull SupportSQLiteDatabase database) {

    }
}
