package com.uae.feature_home.di


import com.uae.feature_home.domain.HomeRepository
import com.uae.feature_home.remote.repository.HomeRepositoryImpl
import com.uae.feature_home.remote.service.HomeService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit


@Module
@InstallIn(SingletonComponent::class)
object HomeModule {

    @Provides
    fun provideAuthService(retrofit: Retrofit) : HomeService{
        return retrofit.create(HomeService::class.java)
    }

    @Provides
    fun provideAuthRepository(authRepositoryImpl : HomeRepositoryImpl) : HomeRepository = authRepositoryImpl

}