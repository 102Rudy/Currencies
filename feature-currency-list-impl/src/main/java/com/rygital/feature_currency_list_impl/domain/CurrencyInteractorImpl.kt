package com.rygital.feature_currency_list_impl.domain

import androidx.annotation.VisibleForTesting
import com.rygital.core_utils.rx.SchedulerProvider
import com.rygital.feature_currency_list_impl.domain.model.CurrencyRateModel
import com.rygital.feature_currency_list_impl.domain.model.ExchangeRatesModel
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

internal class CurrencyInteractorImpl @Inject constructor(
    private val schedulerProvider: SchedulerProvider,
    private val currencyRepository: CurrencyRepository
) : CurrencyInteractor {

    @VisibleForTesting
    var exchangeRatesCache: ExchangeRatesModel? = null

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
                ExchangeRatesModel(CurrencyRateModel(baseCurrency, value), rates)
            }

    override fun getRatesRelativeToBase(
        newBaseCurrency: String,
        value: Double
    ): Single<ExchangeRatesModel> =
        Single.fromCallable { exchangeRatesCache ?: getRates(newBaseCurrency, value).blockingGet() }
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.computation())
            .map { exchangeRates ->
                val baseCurrencyRate = CurrencyRateModel(newBaseCurrency, value)

                val rates =
                    if (newBaseCurrency == exchangeRates.baseCurrencyRate.code) {
                        convertWithSameBase(exchangeRates, value)
                    } else {
                        convertWithAnotherBase(exchangeRates, newBaseCurrency, value)
                    }

                ExchangeRatesModel(baseCurrencyRate, rates)
            }

    private fun convertWithSameBase(
        exchangeRates: ExchangeRatesModel,
        value: Double
    ): List<CurrencyRateModel> =
        exchangeRates.rates
            .map { CurrencyRateModel(it.code, it.rate * value) }

    private fun convertWithAnotherBase(
        exchangeRates: ExchangeRatesModel,
        newBaseCurrency: String,
        value: Double
    ): List<CurrencyRateModel> {
        val newBaseCurrencyRate = exchangeRates.rates
            .first { it.code == newBaseCurrency }
            .rate

        val oldBase = exchangeRates.baseCurrencyRate.code
        val rateModelToAdd = CurrencyRateModel(oldBase, (1 / newBaseCurrencyRate) * value)

        return exchangeRates.rates
            .filterNot { it.code == newBaseCurrency }
            .map { CurrencyRateModel(it.code, (it.rate / newBaseCurrencyRate) * value) }
            .toMutableList()
            .apply { add(rateModelToAdd) }
            .sortedBy { it.code }
    }
}
