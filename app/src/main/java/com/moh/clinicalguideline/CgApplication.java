package com.moh.clinicalguideline;

import android.app.Activity;
import android.app.Application;
import android.app.Service;

import com.moh.clinicalguideline.setup.AppComponent;
import com.moh.clinicalguideline.setup.DaggerAppComponent;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.HasServiceInjector;

public class CgApplication extends Application implements HasActivityInjector,HasServiceInjector {

    @Inject
    DispatchingAndroidInjector<Service> serviceDispatchingAndroidInjector;
    @Inject
    DispatchingAndroidInjector<Activity> activityDispatchingAndroidInjector;
    AppComponent appComponent;
    @Override
    public void onCreate(){
        appComponent = DaggerAppComponent
                            .builder()
                            .addApplication(this)
                            .build();
        appComponent.inject(this);
        super.onCreate();
    }
    @Override
    public AndroidInjector<Activity> activityInjector() {
       return activityDispatchingAndroidInjector;
    }

    @Override
    public AndroidInjector<Service> serviceInjector() {
        return serviceDispatchingAndroidInjector;
    }
}


