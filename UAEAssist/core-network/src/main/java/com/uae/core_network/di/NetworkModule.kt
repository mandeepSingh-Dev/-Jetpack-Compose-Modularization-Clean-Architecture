package com.uae.core_network.di

import com.uae.core_common.UserManager
import com.uae.core_common.datastore.DatastoreManager
import com.uae.core_network.networkUtils.ApiConstants
import com.uae.core_network.networkUtils.ApiConstants.CONNECTION_TIME
import com.uae.core_network.networkUtils.ApiConstants.WRITE_TIME
import com.uae.core_network.networkUtils.interceptors.AuthorizationInterceptor
import com.uae.core_network.networkUtils.interceptors.CacheInterceptors
import com.uae.core_network.networkUtils.interceptors.ErrorInterceptors
import com.uae.core_network.networkUtils.interceptors.UnAuthorizeInterceptor
import android.content.Context
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideLoggingInterceptors():HttpLoggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    @Provides
    fun provideErrorInterceptors(@ApplicationContext context: Context) : ErrorInterceptors =
        ErrorInterceptors(context)

    @Provides
    fun provideAuthInterceptors(userManager: UserManager, appDatastore: DatastoreManager) : AuthorizationInterceptor = AuthorizationInterceptor(userManager = userManager, appDatastore = appDatastore)


    @Provides
    @Singleton
//    @Named(DIConstants.APP_RETROFIT)
    fun provideOkHttpClient(@ApplicationContext context : Context, loggingInterceptor: HttpLoggingInterceptor, cacheInterceptors: CacheInterceptors, errorInterceptors: ErrorInterceptors, authorizationInterceptor: AuthorizationInterceptor, unAuthorizeInterceptor: UnAuthorizeInterceptor /* unreadCountsApiInterceptor: UnreadCountsApiInterceptor,*/): OkHttpClient = OkHttpClient.Builder()
        .writeTimeout(WRITE_TIME,TimeUnit.SECONDS)
        .readTimeout(ApiConstants.READ_TIME,TimeUnit.SECONDS)
        .connectTimeout(CONNECTION_TIME,TimeUnit.SECONDS)
        .let {
            if(com.uae.core_network.BuildConfig.BUILD_TYPE.lowercase() == "debug") {
                it.addInterceptor(loggingInterceptor)
            }else{
                it
            }
        }
        .addInterceptor(authorizationInterceptor)
        .addInterceptor(unAuthorizeInterceptor)
//        .addInterceptor(errorInterceptors)
//        .addInterceptor(unreadCountsApiInterceptor)
//        .addInterceptor(cacheInterceptors)
        //10 MB cache size.
        .cache(Cache(context.cacheDir,(10*10*1024).toLong()))
        .build()

    @Provides
    @Singleton
    fun provideRetrofitInstance(/*@Named(DIConstants.APP_RETROFIT)*/ okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(com.uae.core_network.BuildConfig.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().disableHtmlEscaping().create()))
        .client(okHttpClient)
        .build()
}