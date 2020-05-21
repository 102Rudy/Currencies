package com.rygital.core_network_impl

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.rygital.core_network_api.NetworkApi
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Component(
    modules = [
        NetworkModule::class
    ]
)
interface NetworkComponent : NetworkApi {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance endpointUrl: String): NetworkComponent
    }
}

@Module
internal class NetworkModule {

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    fun provideConverterFactory(gson: Gson): Converter.Factory =
        GsonConverterFactory.create(gson)

    @Provides
    fun provideCallAdapterFactory(): CallAdapter.Factory =
        RxJava2CallAdapterFactory.create()

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    fun provideRetrofit(
        endpointUrl: String,
        converterFactory: Converter.Factory,
        callAdapter: CallAdapter.Factory,
        okHttpClient: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(endpointUrl)
            .addConverterFactory(converterFactory)
            .addCallAdapterFactory(callAdapter)
            .client(okHttpClient)
            .build()
}
