package com.rygital.feature_currency_list_impl.data

import com.rygital.feature_currency_list_impl.data.response.CurrencyLatestResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

internal interface CurrencyService {

    @GET("latest")
    fun getLatestRates(@Query("base") baseCurrency: String): Single<CurrencyLatestResponse>
}
