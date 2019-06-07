package com.ventoray.projectmanager.di.test

import dagger.Component

@Component(modules = [TestModuleOne::class])
interface TestComponent {

    fun getTheWar(): War
}