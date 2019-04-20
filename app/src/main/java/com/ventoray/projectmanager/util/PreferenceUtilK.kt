package com.ventoray.projectmanager.util

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

object PreferenceUtilK {

    private const val PREFERENCES_FILE = "com.ventoray.projectmanager_preferences"

    fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFERENCES_FILE, 0)
    }

    fun putString(context: Context, key: String, value: String) {
        getSharedPreferences(context).edit().putString(key, value).apply()

    }


    private const val KEY_CLIENT_PASSWORD_TOKEN = "client_password_token"

    fun putClientPasswordToken(context: Context, token: String) {
        Log.i("Token", "Saving to preferences: $token")
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


    private const val KEY_FIRST_INSTALL = "first_install_pref"

    fun setFirstTimeOpened(context: Context) {
        Log.i("FirstInstall", "Saving to preferences: true")
        getSharedPreferences(context).edit().putBoolean(KEY_FIRST_INSTALL, true).apply()
    }

    fun setAppOpened(context: Context) {
        Log.i("FirstInstall", "Saving to preferences: false")
        getSharedPreferences(context).edit().putBoolean(KEY_FIRST_INSTALL, false).apply()
    }

    fun getFirstTimeAppOpened(context: Context): Boolean {
        return getSharedPreferences(context).getBoolean(KEY_FIRST_INSTALL, true)
    }


}