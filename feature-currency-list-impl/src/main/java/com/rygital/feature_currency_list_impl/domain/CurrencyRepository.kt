package com.rygital.feature_currency_list_impl.domain

import com.rygital.feature_currency_list_impl.domain.model.ExchangeRatesModel
import io.reactivex.rxjava3.core.Single

internal interface CurrencyRepository {
    fun getLatestRates(baseCurrency: String): Single<ExchangeRatesModel>
}
