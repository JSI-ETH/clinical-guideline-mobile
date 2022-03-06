package com.moh.clinicalguideline.setup;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.moh.clinicalguideline.helper.view.ViewModelFactory;
import com.moh.clinicalguideline.setup.scope.ViewModelKey;
import com.moh.clinicalguideline.views.algorithm.AlgorithmViewModel;
import com.moh.clinicalguideline.views.algorithm.answers.AnswersViewModel;
import com.moh.clinicalguideline.views.algorithm.content.ContentViewModel;
import com.moh.clinicalguideline.views.algorithm.options.OptionsViewModel;
import com.moh.clinicalguideline.views.algorithm.timeline.TimelineViewModel;

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
    @IntoMap
    @ViewModelKey(AnswersViewModel.class)
    abstract ViewModel bindAnswersViewModel(AnswersViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ContentViewModel.class)
    abstract ViewModel bindContentViewModel(ContentViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(TimelineViewModel.class)
    abstract ViewModel bindTimelineViewModel(TimelineViewModel viewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory viewModelFactory);
}
