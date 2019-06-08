package com.ventoray.projectmanager.ui.util

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import com.ventoray.projectmanager.di.ViewModelFactory
import javax.inject.Inject

/**
 * Extension functions for Fragments
 */

fun <T : ViewModel> Fragment.getViewModel(viewModelClass: Class<T>, viewModelFactory: ViewModelFactory): T {
    return ViewModelProviders.of(this, viewModelFactory).get(viewModelClass)
}