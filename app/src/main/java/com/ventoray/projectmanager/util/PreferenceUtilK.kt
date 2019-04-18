package com.ventoray.projectmanager.util

import android.content.Context
import android.content.SharedPreferences

object PreferenceUtilK {

    private val PREFERENCES_FILE = "com.ventoray.projectmanager_preferences"

    fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFERENCES_FILE, 0)
    }

    fun putString(context: Context, key: String, value: String) {
        getSharedPreferences(context).edit().putString(key, value).apply()

    }

    object ClientPasswordTokenPreferences {
        val KEY_CLIENT_PASSWORD_TOKEN = "client_password_token"

        fun putClientPasswordToken(context: Context, token: String) {
            //TODO encrypt token
            getSharedPreferences(context).edit().putString(KEY_CLIENT_PASSWORD_TOKEN, token).apply()
        }

        fun getClientPasswordToken(context: Context): String? {
            //TODO decrypt token
            return getSharedPreferences(context).getString(KEY_CLIENT_PASSWORD_TOKEN, null)
        }

        fun removeTokenPreferences(context: Context) {
            getSharedPreferences(context).edit().putString(KEY_CLIENT_PASSWORD_TOKEN, null).apply()
        }


    }

}