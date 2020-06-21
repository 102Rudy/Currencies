package com.rygital.core_network_impl

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.rygital.core_utils.scope.ApplicationScope
import com.rygital.core_network_api.NetworkApi
import dagger.Component
import dagger.Module
import dagger.Provides
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@ApplicationScope
@Component(
    modules = [
        NetworkModule::class
    ]
)
interface NetworkComponent : NetworkApi {
    @Component.Factory
    interface Factory {
        fun create(): NetworkComponent
    }
}

@Module
internal class NetworkModule {

    companion object {
        private const val CURRENCY_ENDPOINT = "https://hiring.revolut.codes/api/android/"
    }

    @Provides
    fun provideEndpoint(): String = CURRENCY_ENDPOINT

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    fun provideConverterFactory(gson: Gson): Converter.Factory =
        GsonConverterFactory.create(gson)

    @Provides
    fun provideCallAdapterFactory(): CallAdapter.Factory =
        RxJava3CallAdapterFactory.create()

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @ApplicationScope
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
