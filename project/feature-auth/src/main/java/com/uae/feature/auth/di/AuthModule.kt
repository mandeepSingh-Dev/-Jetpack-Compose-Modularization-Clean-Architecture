package com.uae.feature.auth.di

import com.uae.feature.auth.domain.AuthRepository
import com.uae.feature.auth.remote.repository.AuthRepositoryImpl
import com.uae.feature.auth.remote.service.AuthService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit


@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    fun provideAuthService(retrofit: Retrofit) : AuthService{
        return retrofit.create(AuthService::class.java)
    }

    @Provides
    fun provideAuthRepository(authRepositoryImpl : AuthRepositoryImpl) : AuthRepository = authRepositoryImpl

}