package com.ventoray.projectmanager.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.*
import com.ventoray.projectmanager.BuildConfig
import com.ventoray.projectmanager.R
import com.ventoray.projectmanager.util.PreferenceUtilK
import com.ventoray.projectmanager.api.APIv1
import com.ventoray.projectmanager.api.VolleySingleton

import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.nav_header_main.*
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
        startActivity(intent)
    }

    //TODO Replace this whole flow with retrofit2, NetworkBoundResource, and Repository
    private fun signIn(): Unit {

        val clientId: String = BuildConfig.TEST_CLIENT_ID
        val clientSecret: String = BuildConfig.TEST_API_SECRET
        val volley: VolleySingleton = VolleySingleton.getInstance(applicationContext)
        val requestJson: JSONObject = JSONObject().apply {
            put(APIv1.PARAM_USERNAME, inputUsername.text)
            put(APIv1.PARAM_PASSWORD, inputPassword.text)
            put(APIv1.PARAM_CLIENT_ID, clientId)
            put(APIv1.PARAM_CLIENT_SECRET, clientSecret)
            put(APIv1.PARAM_GRANT_TYPE, APIv1.VALUE_GRANT_TYPE)
        }

        val requestBody: String = requestJson.toString()

        val stringRequest: StringRequest = object : StringRequest(
            Request.Method.POST,
            APIv1.URL_TOKEN_REQUEST,
            Response.Listener<String> { response ->
                Log.d("SignIn response", response)
                val jsonObject: JSONObject = JSONObject(response)
//                val userId: Int = jsonObject.optInt()
                val token: String = jsonObject.optString(APIv1.RESPONSE_KEY_ACCESS_TOKEN)
                PreferenceUtilK.putClientPasswordToken(applicationContext, token)
                Log.d("TOKEN", token)
                //TODO Error Checking for Token first
                val signInIntent: Intent = Intent()
                signInIntent.setClass(this, PreSignInActivity::class.java)
                startActivity(signInIntent)

            },
            Response.ErrorListener {  }
        ) {
            override fun getBodyContentType(): String {
                return APIv1.CONTENT_TYPE_BODY
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray(Charset.forName("utf-8"))
            }
        }
        volley.addToRequestQueue(stringRequest)
    }
}
