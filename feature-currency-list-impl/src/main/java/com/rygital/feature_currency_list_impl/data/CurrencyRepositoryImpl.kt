package com.rygital.feature_currency_list_impl.data

import com.rygital.feature_currency_list_impl.domain.CurrencyRepository
import com.rygital.feature_currency_list_impl.domain.model.CurrencyRateModel
import com.rygital.feature_currency_list_impl.domain.model.ExchangeRatesModel
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

internal class CurrencyRepositoryImpl @Inject constructor(
    private val currencyService: CurrencyService
) : CurrencyRepository {

    override fun getLatestRates(baseCurrency: String): Single<ExchangeRatesModel> =
        currencyService.getLatestRates(baseCurrency)
            .map { currencyLatestResponse ->
                val baseCurrencyRate = CurrencyRateModel(currencyLatestResponse.baseCurrency, 1.0)

                val rates = currencyLatestResponse.rates.entries
                    .map { CurrencyRateModel(it.key, it.value) }

                ExchangeRatesModel(baseCurrencyRate, rates.take(6))
            }
}
