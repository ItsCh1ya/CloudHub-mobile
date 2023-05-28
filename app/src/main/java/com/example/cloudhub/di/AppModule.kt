package com.example.cloudhub.di

import com.example.cloudhub.data.remote.CloudHubApi
import com.example.cloudhub.data.repository.CloudRepoImpl
import com.example.cloudhub.domain.repository.CloudRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideCloudHubApi(): CloudHubApi {
        return Retrofit.Builder()
            .baseUrl("https://chiya.ru/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CloudHubApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCloudHubRepo(api: CloudHubApi): CloudRepository {
        return CloudRepoImpl(api)
    }
}

