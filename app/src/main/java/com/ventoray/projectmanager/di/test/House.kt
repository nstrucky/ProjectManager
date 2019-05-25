package com.ventoray.projectmanager.di.test

import javax.inject.Inject

abstract class House() {
    open var name: String = ""
    open var isGood: Boolean = false
}

class HouseStark @Inject constructor(): House() {
    override var name: String = "Stark"
    override var isGood: Boolean = true

}

class HouseBolton @Inject constructor(): House()  {
    override var name: String = "Bolton"
    override var isGood: Boolean = false
}


