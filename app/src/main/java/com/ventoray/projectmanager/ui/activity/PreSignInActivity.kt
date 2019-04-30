package com.ventoray.projectmanager.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity;
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.ventoray.projectmanager.BaseActivity
import com.ventoray.projectmanager.R
import com.ventoray.projectmanager.util.MessageUtil.makeToast
import com.ventoray.projectmanager.util.PreferenceUtilK
import com.ventoray.projectmanager.web.APIv1
import com.ventoray.projectmanager.web.VolleySingleton

class PreSignInActivity : BaseActivity() {

    private var signInIntent: Intent = Intent()
    private var mainIntent: Intent = Intent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pre_sign_in)

        signInIntent.setClass(this, SignInActivity::class.java)
        mainIntent.setClass(this, MainActivity::class.java)

        //if not go to sign in activity
        if (shouldSignIn()) {
            startActivity(signInIntent)
        }
    }

    /**
     * Determines if the user should sign in or not
     */
    private fun shouldSignIn(): Boolean {
        val token: String? = PreferenceUtilK.getClientPasswordToken(applicationContext)
        token?.let {
            checkSignIn(token)
            return false
        }

        if (PreferenceUtilK.getUserSignedIn(this) && !connected) {
            return false
        }

        return true
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

                //User successfully verified, set logged in
                PreferenceUtilK.setUserSignedIn(this)

                //Determine if it's the first signin
                if (PreferenceUtilK.getFirstTimeAppOpened(this)) {
                    makeToast(this, "First Time Signing In!")

                    PreferenceUtilK.setAppOpened(this)
                    //TODO make this a walkthrough or something
                    startActivity(mainIntent)
                } else {
                    startActivity(mainIntent)
                }


            },
            Response.ErrorListener {
                Toast.makeText(this, R.string.no_one_signed_in, Toast.LENGTH_SHORT).show()
                it?.let { Log.e("SignInActivity", it.message ?: "null message")  }

                if (PreferenceUtilK.getUserSignedIn(this) && !connected) {
                    startActivity(mainIntent)
                } else {
                    startActivity(signInIntent)
                }


            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                return HashMap<String, String>().apply {  put(APIv1.HEADER_AUTHORIZATION, "Bearer $token") }
            }
        }
        VolleySingleton.getInstance(applicationContext).addToRequestQueue(stringRequest)
    }

}
