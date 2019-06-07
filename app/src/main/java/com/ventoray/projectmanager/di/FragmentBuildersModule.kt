package com.ventoray.projectmanager.di

import com.ventoray.projectmanager.ui.main_activity.ProjectListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeProjectListFragment(): ProjectListFragment
}