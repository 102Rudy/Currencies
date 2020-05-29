package com.rygital.feature_currency_list_impl.domain.model

internal data class ExchangeRatesModel(
    val baseCurrencyRate: CurrencyRateModel,
    val rates: List<CurrencyRateModel>
)
