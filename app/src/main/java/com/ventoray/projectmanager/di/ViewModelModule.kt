package com.ventoray.projectmanager.di

import android.arch.lifecycle.ViewModel
import com.ventoray.projectmanager.ui.main_activity.ProjectViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ProjectViewModel::class)
    abstract fun bindProjectViewModel(userViewModel: ProjectViewModel): ViewModel

//    @Binds
//    abstract fun bindViewModelFactory(factory: GithubViewModelFactory): ViewModelProvider.Factory
}