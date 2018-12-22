package com.moh.clinicalguideline.setup;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.moh.clinicalguideline.helper.view.ViewModelFactory;
import com.moh.clinicalguideline.setup.scope.ViewModelKey;
import com.moh.clinicalguideline.views.algorithm.AlgorithmViewModel;
import com.moh.clinicalguideline.views.algorithm.options.OptionsViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(AlgorithmViewModel.class)
    abstract ViewModel bindAlgorithmViewModel(AlgorithmViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(OptionsViewModel.class)
    abstract ViewModel bindOptionsViewModel(OptionsViewModel viewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory viewModelFactory);
}
