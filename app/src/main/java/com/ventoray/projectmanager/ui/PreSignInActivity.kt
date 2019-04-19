package com.ventoray.projectmanager.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity;
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.ventoray.projectmanager.BuildConfig
import com.ventoray.projectmanager.R
import com.ventoray.projectmanager.util.PreferenceUtilK
import com.ventoray.projectmanager.web.APIv1
import com.ventoray.projectmanager.web.VolleySingleton

class PreSignInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pre_sign_in)
        //if not go to sign in activity
        if (!signIn()) {
            val intent: Intent = Intent()
            intent.setClass(this, SignInActivity::class.java)
            startActivity(intent)
        }
    }

    private fun signIn(): Boolean {
        val token: String? = PreferenceUtilK.getClientPasswordToken(applicationContext)
        token?.let {
            checkSignIn(token)
            return true
        }

        return false;
    }

    /**
     * @token: this is the password grant token saved when the user logs in
     *
     * The /api/v1/test route is protected by authentication and requires a password grant token.
     * If the token is active then the user can sign in...not the best method, but trying it out.
     */
    private fun checkSignIn(token: String) : Unit {
        val stringRequest = object : StringRequest(
            Request.Method.GET, APIv1.URL_TEST,
            Response.Listener<String> { response ->
                response?.let { Log.i("SignInActivity", response) }
                val intent: Intent = Intent()
                intent.setClass(this, MainActivity::class.java)
                startActivity(intent)
            },
            Response.ErrorListener {
                Toast.makeText(this, R.string.no_one_signed_in, Toast.LENGTH_SHORT).show()
                it?.let { Log.e("SignInActivity", it.message ?: "null message")  }

                val signInIntent: Intent = Intent()
                signInIntent.setClass(this, SignInActivity::class.java)
                startActivity(signInIntent)
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                return HashMap<String, String>().apply {  put(APIv1.HEADER_AUTHORIZATION, "Bearer $token") }
            }
        }
        VolleySingleton.getInstance(applicationContext).addToRequestQueue(stringRequest)
    }

}
