package com.example.cloudhub.di

import com.example.cloudhub.data.remote.CloudHubApi
import com.example.cloudhub.data.repository.CloudRepoImpl
import com.example.cloudhub.domain.repository.CloudRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
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
    fun provideCloudHubApi(): CloudHubApi {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client: OkHttpClient =
            OkHttpClient
                .Builder()
                .addInterceptor(interceptor) //Logging
                .cookieJar(object : CookieJar { //Saving cookies, TODO: Refactor to spectated file
                    private val cookieStore: MutableMap<String, MutableList<Cookie>> = HashMap()

                    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                        cookieStore[url.host] = cookies.toMutableList()
                    }

                    override fun loadForRequest(url: HttpUrl): List<Cookie> {
                        val cookies = cookieStore[url.host]
                        return cookies ?: ArrayList()
                    }
                })
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

