package com.ventoray.projectmanager.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtil {

    private static final String PREFERENCES_FILE = "com.ventoray.projectmanager_preferences";

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREFERENCES_FILE, 0);
    }

    public static void putString(Context context, String key, String value) {
        getSharedPreferences(context).edit().putString(key, value).apply();

    }

    public static class ClientPasswordTokenPreferences {
        public static final String KEY_CLIENT_PASSWORD_TOKEN = "client_password_token";

        public static void putClientPasswordToken(Context context, String token) {
            //TODO encrypt token
            getSharedPreferences(context).edit().putString(KEY_CLIENT_PASSWORD_TOKEN, token).apply();
        }

        public static String getClientPasswordToken(Context context) {
            String token = getSharedPreferences(context).getString(KEY_CLIENT_PASSWORD_TOKEN, null);
            //TODO decrypt token
            return token;
        }

        public static void removeTokenPreferences(Context context) {
            getSharedPreferences(context).edit().putString(KEY_CLIENT_PASSWORD_TOKEN, null).apply();
        }


    }

}
