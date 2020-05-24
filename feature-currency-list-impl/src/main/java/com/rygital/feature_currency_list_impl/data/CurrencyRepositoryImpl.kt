package com.rygital.feature_currency_list_impl.data

import com.rygital.feature_currency_list_impl.domain.CurrencyRepository
import com.rygital.feature_currency_list_impl.domain.model.CurrencyRateModel
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

internal class CurrencyRepositoryImpl @Inject constructor(
    private val currencyService: CurrencyService
) : CurrencyRepository {

    override fun getLatestRates(baseCurrency: String): Single<List<CurrencyRateModel>> =
        currencyService.getLatestRates(baseCurrency)
            .map { currencyLatestResponse ->
                currencyLatestResponse.rates.entries.map { CurrencyRateModel(it.key, it.value) }
            }
}
