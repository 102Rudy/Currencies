package com.rygital.feature_currency_list_impl.domain

import com.rygital.core.rx.SchedulerProvider
import com.rygital.feature_currency_list_impl.domain.model.CurrencyRateModel
import com.rygital.feature_currency_list_impl.domain.model.ExchangeRatesModel
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

internal class CurrencyInteractorImpl @Inject constructor(
    private val schedulerProvider: SchedulerProvider,
    private val currencyRepository: CurrencyRepository
) : CurrencyInteractor {

    private var exchangeRatesCache: ExchangeRatesModel? = null

    override fun getRates(baseCurrency: String, value: Double): Single<ExchangeRatesModel> =
        currencyRepository.getLatestRates(baseCurrency)
            .doOnSuccess { exchangeRatesCache = it }
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.computation())
            .map { exchangeRates ->
                if (value == 1.0) {
                    return@map exchangeRates
                }

                val rates = convertWithSameBase(exchangeRates, value)
                ExchangeRatesModel(exchangeRates.baseCurrency, rates)
            }

    override fun getRatesRelativeToBase(
        baseCurrency: String,
        value: Double
    ): Single<ExchangeRatesModel> =
        Single.fromCallable { exchangeRatesCache ?: getRates(baseCurrency).blockingGet() }
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.computation())
            .map { exchangeRates ->
                if (value == 1.0) {
                    return@map exchangeRates
                }

                val rates =
                    if (baseCurrency == exchangeRates.baseCurrency) {
                        convertWithSameBase(exchangeRates, value)
                    } else {
                        convertWithAnotherBase(exchangeRates, baseCurrency, value)
                    }

                ExchangeRatesModel(baseCurrency, rates)
            }

    private fun convertWithSameBase(
        exchangeRates: ExchangeRatesModel,
        value: Double
    ): List<CurrencyRateModel> =
        exchangeRates.rates
            .map { CurrencyRateModel(it.code, it.rate * value) }

    private fun convertWithAnotherBase(
        exchangeRates: ExchangeRatesModel,
        baseCurrency: String,
        value: Double
    ): List<CurrencyRateModel> {
        val baseCurrencyRate = exchangeRates.rates
            .first { it.code == baseCurrency }
            .rate

        return exchangeRates.rates
            .map { CurrencyRateModel(it.code, (it.rate / baseCurrencyRate) * value) }
    }
}
