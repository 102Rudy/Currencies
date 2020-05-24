package com.rygital.feature_currency_list_impl.domain

import com.rygital.feature_currency_list_impl.domain.model.CurrencyRateModel
import io.reactivex.rxjava3.core.Single

internal interface CurrencyRepository {
    fun getLatestRates(baseCurrency: String): Single<List<CurrencyRateModel>>
}
