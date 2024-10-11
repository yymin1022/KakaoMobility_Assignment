package com.yong.km_assignment.util

import com.yong.km_assignment.BuildConfig
import com.yong.km_assignment.data.api.RouteApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiUtil {
    fun getRouteApi(): RouteApi {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor())
            .build()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .addConverterFactory(getConverterFactory())
            .client(okHttpClient).build()
            .create(RouteApi::class.java)
    }

    private fun getConverterFactory(): Converter.Factory {
        return GsonConverterFactory.create()
    }
}

class AuthInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val requestBuilder = original.newBuilder().header("Authorization", BuildConfig.API_KEY)
        return chain.proceed(requestBuilder.build())
    }
}