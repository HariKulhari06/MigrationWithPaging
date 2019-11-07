package com.example.migrationwithpaging.data.network

import okhttp3.Interceptor
import okhttp3.Response

class HttpInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url()
        val url = originalUrl.newBuilder()
            .addQueryParameter("api_key","25d3da3734055bccdaf9e2c587d94271")
            .build()

        val requestBuilder = originalRequest.newBuilder().url(url)

        val request = requestBuilder.build()

        return chain.proceed(request)
    }
}