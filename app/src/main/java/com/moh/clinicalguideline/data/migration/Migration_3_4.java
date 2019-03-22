package com.moh.clinicalguideline.data.migration;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.migration.Migration;
import android.support.annotation.NonNull;

public class Migration_3_4 extends Migration {
    /**
     * Creates a new migration between {@code startVersion} and {@code endVersion}.
     *
     * @param startVersion The start version of the database.
     * @param endVersion   The end version of the database after this migration is applied.
     */
    public Migration_3_4() {
        super(3, 4);
    }

    @Override
    public void migrate(@NonNull SupportSQLiteDatabase database) {

    }
}
