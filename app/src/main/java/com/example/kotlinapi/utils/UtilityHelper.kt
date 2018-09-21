package com.example.kotlinapi.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.NetworkInfo
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager


/**
 * Created by siddharth on 5/6/18.
 */
class UtilityHelper {

    @SuppressLint("MissingPermission")
    companion object {
        fun isConnected(context: Context): Boolean {

            val cm = context
                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetworkInfo

            return activeNetwork != null && activeNetwork.isConnectedOrConnecting
        }
    }
}