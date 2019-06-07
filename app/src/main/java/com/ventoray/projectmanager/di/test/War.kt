package com.ventoray.projectmanager.di.test

class War(val houseOne: House, val houseTwo: House) {

    fun getWinner(): String {
        val winner = if (houseOne.isGood) houseOne.name else houseTwo.name
        return winner
    }
}
