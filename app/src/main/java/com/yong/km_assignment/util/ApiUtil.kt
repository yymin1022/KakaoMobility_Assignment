package com.yong.km_assignment.util

import com.yong.km_assignment.BuildConfig
import com.yong.km_assignment.data.api.RouteApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Converter
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiUtil {
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor())
            .build()
    }

    @Provides
    @Singleton
    fun provideRouteApi(
        okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory
    ): RouteApi {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .addConverterFactory(converterFactory)
            .client(okHttpClient).build()
            .create(RouteApi::class.java)
    }
}

class AuthInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val requestBuilder = original.newBuilder().header("Authorization", BuildConfig.API_KEY)
        return chain.proceed(requestBuilder.build())
    }
}