package com.ventoray.projectmanager.ui

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.DiskBasedCache
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.StringRequest
import com.ventoray.projectmanager.BuildConfig
import com.ventoray.projectmanager.R
import com.ventoray.projectmanager.web.VolleySingleton

import kotlinx.android.synthetic.main.activity_pre_sign_in.*

class PreSignInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pre_sign_in)
        //TODO check if user is signed in
        //if not go to sign in activity
        makeConnection()

    }

    private fun makeConnection(): Unit {
        val url = "http://" + BuildConfig.TEST_SERVER_IP_ADDRESS + "/api/v1/test"
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                response?.let { Log.i("SignInActivity", response) }
                val intent: Intent = Intent()
                intent.setClass(this, SignInActivity::class.java)
                startActivity(intent)
            },
            Response.ErrorListener {
                Toast.makeText(this, "that didn't work", Toast.LENGTH_SHORT).show()
                it?.let { Log.e("SignInActivity", it.message ?: "null message")  }
            }
        )
        VolleySingleton.getInstance(applicationContext).addToRequestQueue(stringRequest)
    }

}
