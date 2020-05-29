package com.rygital.feature_currency_list_impl.domain.model

internal data class ExchangeRatesModel(
    val baseCurrency: String,
    val rates: List<CurrencyRateModel>
)
