package com.moh.clinicalguideline.setup;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.moh.clinicalguideline.helper.view.ViewModelFactory;
import com.moh.clinicalguideline.setup.scope.ViewModelKey;
import com.moh.clinicalguideline.views.algorithm.AlgorithmViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(AlgorithmViewModel.class)
    abstract ViewModel bindUserViewModel(AlgorithmViewModel algorithmViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory viewModelFactory);
}
