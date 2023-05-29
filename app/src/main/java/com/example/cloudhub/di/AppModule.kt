package com.example.cloudhub.di

import com.example.cloudhub.common.CookieInterceptor
import android.app.Application
import android.content.Context
import com.example.cloudhub.data.remote.CloudHubApi
import com.example.cloudhub.data.repository.CloudRepoImpl
import com.example.cloudhub.domain.repository.CloudRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideCloudHubApi(context: Context): CloudHubApi {
        val loggingInterceptor = HttpLoggingInterceptor()
        val cookieInterceptor = CookieInterceptor(context)
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client: OkHttpClient =
            OkHttpClient
                .Builder()
                .addInterceptor(loggingInterceptor) //Logging
                .addInterceptor(cookieInterceptor)
                .build()


        return Retrofit.Builder()
            .baseUrl("http://192.168.0.166:1337/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(CloudHubApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCloudHubRepo(api: CloudHubApi): CloudRepository {
        return CloudRepoImpl(api)
    }
}

