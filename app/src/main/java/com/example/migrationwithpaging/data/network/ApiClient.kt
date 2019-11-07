package com.example.migrationwithpaging.data.network

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @Details
 * @Author Ranosys Technologies
 * @Date 15-Jul-2019
 */

object ApiClient{

    fun getApiService(): ApiService {
        val responseParserFactory = GsonBuilder().create()
        return Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org")
            .client( OkHttpClient.Builder().addNetworkInterceptor(HttpInterceptor()).addInterceptor(
                provideLoggingInterceptor()
            ).build())
            .addConverterFactory(GsonConverterFactory.create(responseParserFactory))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build().create(ApiService::class.java)
    }

    /**
     * @method use for provide Http Logging Interceptor
     */
    private fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }


}