package com.rygital.feature_currency_list_impl.domain

import com.rygital.feature_currency_list_impl.domain.model.ExchangeRatesModel
import io.reactivex.rxjava3.core.Single

internal interface CurrencyInteractor {
    fun getRates(baseCurrency: String, value: Double): Single<ExchangeRatesModel>
    fun getRatesRelativeToBase(newBaseCurrency: String, value: Double): Single<ExchangeRatesModel>
}
