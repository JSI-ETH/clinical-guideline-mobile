package com.moh.clinicalguideline.setup;

import android.app.Application;

import com.moh.clinicalguideline.CgApplication;
import com.moh.clinicalguideline.views.ActivityBindingModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {
        ApplicationModule.class,
        ActivityBindingModule.class,
        AndroidSupportInjectionModule.class
})
public interface AppComponent extends AndroidInjector<CgApplication> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder addApplication(Application application);//this binds new components to the

        AppComponent build();
    }
}