package com.rygital.feature_currency_list_impl.data.response

import androidx.annotation.Keep

@Keep
internal class CurrencyLatestResponse(
    val baseCurrency: String,
    val rates: Map<String, Double>
)
