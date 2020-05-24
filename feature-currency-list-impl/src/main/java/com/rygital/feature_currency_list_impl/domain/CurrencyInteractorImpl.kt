package com.rygital.feature_currency_list_impl.domain

import com.rygital.core.rx.SchedulerProvider
import com.rygital.feature_currency_list_impl.domain.model.CurrencyRateModel
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

internal class CurrencyInteractorImpl @Inject constructor(
    private val schedulerProvider: SchedulerProvider,
    private val currencyRepository: CurrencyRepository
) : CurrencyInteractor {

    override fun getRates(baseCurrency: String, value: Double): Single<List<CurrencyRateModel>> =
        currencyRepository.getLatestRates(baseCurrency)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.computation())
            .doOnSuccess { items ->
                if (value != 1.0) {
                    items.forEach { it.rate *= value }
                }
            }
}
