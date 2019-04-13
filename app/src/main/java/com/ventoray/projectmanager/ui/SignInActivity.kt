package com.ventoray.projectmanager.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.*
import com.ventoray.projectmanager.BuildConfig
import com.ventoray.projectmanager.R

import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity(), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        signInButton.setOnClickListener(this)
        registerButton.setOnClickListener(this)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN;
    }


    override fun onClick(view: View?) {
        when(view?.id) {
            signInButton.id -> {/*do nothing for now*/}
            registerButton.id -> register()
        }
    }

    private fun register() : Unit {
        var intent: Intent = Intent();
        intent.setClass(this, RegisterActivity::class.java )
        testExtras(intent)
        startActivity(intent)
    }









    //TODO remove this
    private fun testExtras(intent: Intent) {
        intent.putExtra("Test 1", "test 1 extra").putExtra("Test 4", "test 4 extra")
        // fun <T> T.apply(f: T() -> Unit): T {f() return this;}
        intent.apply {
            putExtra("Test 2", "test 2 extra")
            putExtra("Test 3", "test 3 extra")
        }
    }



}
