package com.ventoray.projectmanager.util

import android.content.Context
import android.widget.Toast

object MessageUtil {


    fun makeToast(context: Context, message: String) : Unit {
        Toast.makeText(context, message, Toast.LENGTH_SHORT)
            .show()
    }
}