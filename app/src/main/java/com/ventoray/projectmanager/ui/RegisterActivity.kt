package com.ventoray.projectmanager.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.ventoray.projectmanager.R

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        setSupportActionBar(findViewById(R.id.toolbar))

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar?.setDisplayShowTitleEnabled(false)

        var extras: Bundle? = intent.extras
        testExtras(extras)

    }


    //TODO remove this
    private fun testExtras(extras: Bundle?): Unit {
        val ex1: String? = extras?.getString("Test 1")
        val ex2: String? = extras?.getString("Test 2")
        val ex3: String? = extras?.getString("Test 3")
        val ex4: String? = extras?.getString("Test 4")
        var string: String = String.format("1: %s%n2: %s%n3: %s%n4: %s", ex1, ex2, ex3, ex4)
        Log.d("RegisterExtras", string)
    }
}
