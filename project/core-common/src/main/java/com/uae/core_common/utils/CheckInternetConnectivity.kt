package com.uae.core_common.utils

import android.content.Context
import android.net.ConnectivityManager

class CheckInternetConnectivity {

    companion object {
        fun isInternet(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return connectivityManager.activeNetwork!=null
        }
    }

}