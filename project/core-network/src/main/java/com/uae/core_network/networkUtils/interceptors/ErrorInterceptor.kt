package com.uae.core_network.networkUtils.interceptors

import com.uae.core_common.utils.CheckInternetConnectivity
import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

class ErrorInterceptors @Inject constructor(val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val isInternet = CheckInternetConnectivity.isInternet(context)
        if( !isInternet )
        {
            throw IOException("No Connectivity! Please check your connection.")
        }

        val requestBuilder = chain.request().newBuilder()
        return chain.proceed(requestBuilder.build())
    }
}