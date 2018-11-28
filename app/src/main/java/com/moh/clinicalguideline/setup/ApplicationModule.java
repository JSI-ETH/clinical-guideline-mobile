package com.moh.clinicalguideline.setup;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
@Singleton
public abstract class ApplicationModule {
    @Binds
    abstract Context bindContext(Application application);

    @Provides
    @Singleton
    static PackageInfo providesPackageInfo(Application application) {
        try {
            return application.getPackageManager().getPackageInfo(application.getPackageName(), 0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
