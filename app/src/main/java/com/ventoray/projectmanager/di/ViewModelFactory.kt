package com.ventoray.projectmanager.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import dagger.MapKey
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton
import kotlin.reflect.KClass

//@Singleton
//class ProjectViewModelFactory @Inject constructor(
//    private val creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
//) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        val creator = creators[modelClass] ?: creators.entries.firstOrNull {
//            modelClass.isAssignableFrom(it.key)
//        }?.value ?: throw IllegalArgumentException("unknown model class $modelClass")
//        try {
//            @Suppress("UNCHECKED_CAST")
//            return creator.get() as T
//        } catch (e: Exception) {
//            throw RuntimeException(e)
//        }
//
//    }
//}

@Singleton
class ViewModelFactory @Inject constructor(private val viewModels: MutableMap<Class<out ViewModel>, Provider<ViewModel>>) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = viewModels[modelClass]?.get() as T
}

//@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
//@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
//@MapKey
//internal annotation class ViewModelKey(val value: KClass<out ViewModel>)