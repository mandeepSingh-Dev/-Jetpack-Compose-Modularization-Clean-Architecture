package com.uae.core_network.networkUtils.interceptors

import com.uae.core_common.UserManager
import com.uae.core_common.datastore.DatastoreManager
import com.uae.core_network.networkUtils.ApiConstants
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthorizationInterceptor @Inject constructor(
    private val userManager: UserManager,
    private val appDatastore: DatastoreManager
) : Interceptor{

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
        val newRequest = chain.request().newBuilder()

        runBlocking{
//            appDatastore.read(DatastoreConstants.USER_DATA,"").first().let {
                //If user token is not empty then we will paas token to header otherwise return from try block
                try {
                    val token = userManager.getUserToken()
                    if (token.isEmpty() || token.isBlank() )
                        return@runBlocking

                    Log.i("okhttp", "${ApiConstants.AUTH_TOKEN} ${token}")
                    newRequest.addHeader(ApiConstants.AUTH_TOKEN, "$token")

                }catch (e:Exception){
                    //
                }
            }
//       }

        return chain.proceed(newRequest.build())
    }
}




