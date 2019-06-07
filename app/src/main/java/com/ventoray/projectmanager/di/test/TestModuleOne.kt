package com.ventoray.projectmanager.di.test

import dagger.Module
import dagger.Provides


@Module
class TestModuleOne {

    @Provides
    fun houseStark(): HouseStark {
        return HouseStark()
    }

    @Provides
    fun houseBolton(): HouseBolton {
        return HouseBolton()
    }

    @Provides
    fun theWar(house: HouseStark, houseTwo: HouseBolton): War {
        return War(house, houseTwo)
    }
}

