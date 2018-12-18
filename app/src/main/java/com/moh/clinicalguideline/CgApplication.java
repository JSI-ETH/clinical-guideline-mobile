package com.moh.clinicalguideline;


import com.moh.clinicalguideline.setup.AppComponent;
import com.moh.clinicalguideline.setup.DaggerAppComponent;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;

public class CgApplication extends DaggerApplication {


    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        AppComponent appComponent = DaggerAppComponent.builder().application(this).build();
        appComponent.inject(this);
        return appComponent;
    }
}


