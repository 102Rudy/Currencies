package com.rygital.feature_currency_list_impl.domain.model

internal class ExchangeRatesModel(
    val baseCurrency: String,
    val rates: List<CurrencyRateModel>
)
