package com.ventoray.projectmanager.di

import com.ventoray.projectmanager.ui.projects.ProjectsActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class MainActivityModule {
    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeMainActivity(): ProjectsActivity
}