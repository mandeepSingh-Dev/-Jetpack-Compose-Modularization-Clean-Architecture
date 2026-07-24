package com.uae.feature_profile.di

import com.uae.feature_profile.domain.ProfileRepository
import com.uae.feature_profile.remote.repository.ProfileRepositoryImpl
import com.uae.feature_profile.remote.service.ProfileService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit


@Module
@InstallIn(SingletonComponent::class)
object ProfileModule {

    @Provides
    fun provideProfileService(retrofit: Retrofit) : ProfileService{
        return retrofit.create(ProfileService::class.java)
    }

    @Provides
    fun provideProfileRepository(profileRepositoryImpl : ProfileRepositoryImpl) : ProfileRepository = profileRepositoryImpl

}