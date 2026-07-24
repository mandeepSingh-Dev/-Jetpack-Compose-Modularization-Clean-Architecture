package com.uae.feature_chat_with_us.di


import com.uae.feature_chat_with_us.domain.ChatWithUsRepository
import com.uae.feature_chat_with_us.remote.repository.ChatWithUsRepositoryImpl
import com.uae.feature_chat_with_us.remote.service.ChatWithUsService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit


@Module
@InstallIn(SingletonComponent::class)
object ChatWithUsModule {

    @Provides
    fun provideChatWithUsService(retrofit: Retrofit) : ChatWithUsService{
        return retrofit.create(ChatWithUsService::class.java)
    }

    @Provides
    fun provideAuthRepository(chatWithUsRepositoryImpl : ChatWithUsRepositoryImpl) : ChatWithUsRepository = chatWithUsRepositoryImpl

}