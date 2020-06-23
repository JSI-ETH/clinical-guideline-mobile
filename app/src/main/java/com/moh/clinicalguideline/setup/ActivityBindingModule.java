package com.moh.clinicalguideline.setup;


import com.moh.clinicalguideline.setup.scope.ActivityScoped;
import com.moh.clinicalguideline.views.algorithm.AlgorithmActivity;
import com.moh.clinicalguideline.views.algorithm.answers.AnswersFragment;
import com.moh.clinicalguideline.views.algorithm.content.ContentFragment;
import com.moh.clinicalguideline.views.algorithm.options.OptionsFragment;
import com.moh.clinicalguideline.views.algorithm.timeline.TimeLineFragment;
import com.moh.clinicalguideline.views.main.MenuActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * We want Dagger.Android to create a Subcomponent which has a parent Component of whichever module MainActivityBindingModule is on,
 * in our case that will be AppComponent. The beautiful part about this setup is that you never need to tell AppComponent that it is going to have all these subcomponents
 * nor do you need to tell these subcomponents that AppComponent exists.
 * We are also telling Dagger.Android that this generated SubComponent needs to include the specified modules and be aware of a scope annotation @ActivityScoped
 * When Dagger.Android annotation processor runs it will create 4 subcomponents for us.
 */

@Module
public abstract class ActivityBindingModule {
    @ActivityScoped
    @ContributesAndroidInjector
    abstract MenuActivity menuActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract AlgorithmActivity algorithmActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract OptionsFragment optionsFragment();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract AnswersFragment answersFragment();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract ContentFragment contentFragment();

//    @ActivityScoped
//    @ContributesAndroidInjector
//    abstract TimeLineFragment timeLineFragment();
}