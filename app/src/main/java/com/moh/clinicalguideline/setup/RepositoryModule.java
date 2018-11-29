package com.moh.clinicalguideline.setup;

import com.moh.clinicalguideline.repository.NodeLocalRepository;
import com.moh.clinicalguideline.repository.NodeRepository;
import com.moh.clinicalguideline.repository.NodeTestRepository;
import com.moh.clinicalguideline.views.main.MenuViewModel;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class RepositoryModule {
    @Binds
    public abstract NodeRepository getNodeRepository(NodeLocalRepository nodeLocalRepository);
//   @Binds
//    public abstract NodeRepository getNodeRepository(NodeTestRepository nodeLocalRepository);

}

