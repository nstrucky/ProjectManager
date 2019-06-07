package com.ventoray.projectmanager.api

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

/**
 * Singleton implementation as prescribed by Android framework: https://developer.android.com/training/volley/requestQueue
 * @param context: this should be an ApplicationContext, not Activity Context
 *
 */
class VolleySingleton constructor(context: Context) {
    companion object {
        @Volatile //writes to this field are immediately visible to other threads
        private var INSTANCE: VolleySingleton? = null
        fun getInstance(context: Context) = INSTANCE ?: synchronized(this) { //if INSTANCE not null, use that, else function
            INSTANCE ?: VolleySingleton(context).also { INSTANCE = it } //similar to apply
        }
    }

    //TODO see guide for ImageLoader code

    val requestQueue: RequestQueue by lazy {
        //applicationContext is key, it keeps you from leaking th Activity or BroadcastReceiver if passed in
        Volley.newRequestQueue(context.applicationContext)
    }

    fun <T> addToRequestQueue(req: Request<T>) {
        requestQueue.add(req)
    }
}