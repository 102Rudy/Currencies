package com.rygital.feature_currency_list_impl.domain

import com.rygital.feature_currency_list_impl.domain.model.CurrencyRateModel
import io.reactivex.rxjava3.core.Single

internal interface CurrencyInteractor {
    fun getRates(baseCurrency: String = "EUR", value: Double = 1.0): Single<List<CurrencyRateModel>>
}
