package com.ventoray.projectmanager.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.*
import com.ventoray.projectmanager.BuildConfig
import com.ventoray.projectmanager.R
import com.ventoray.projectmanager.web.VolleySingleton

import kotlinx.android.synthetic.main.activity_sign_in.*
import org.json.JSONObject
import java.nio.charset.Charset

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
            signInButton.id -> signIn()
            registerButton.id -> register()
        }
    }

    private fun register() : Unit {
        var intent: Intent = Intent();
        intent.setClass(this, RegisterActivity::class.java )
        testExtras(intent)
        startActivity(intent)
    }



    private fun signIn(): Unit {
        val username: String = inputUsername.text.toString()
        val password: String = inputPassword.text.toString()
        val client_id: String = BuildConfig.TEST_CLIENT_ID
        val client_secret: String = BuildConfig.TEST_API_SECRET

        val volley: VolleySingleton = VolleySingleton.getInstance(applicationContext)

        val requestJson: JSONObject = JSONObject().apply {
            put("username", username)
            put("password", password)
            put("client_id", client_id)
            put("client_secret", client_secret)
            put("grant_type", "password")
        }


        val requestBody: String = requestJson.toString()


        val stringRequest: StringRequest = object : StringRequest(
            Request.Method.POST,
            "http://" + BuildConfig.TEST_SERVER_IP_ADDRESS + "/oauth/token",
            Response.Listener<String> { response ->
                Log.d("SignInActivity", response)
            },
            Response.ErrorListener {  }
        ) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray(Charset.forName("utf-8"))
            }
        }


        volley.addToRequestQueue(stringRequest)
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
