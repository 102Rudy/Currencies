package com.rygital.feature_currency_list_impl.presentation.viewdata

data class CurrencyViewData(
    val code: String,
    val title: String,
    val rate: String,
    val icon: Int
) {
    companion object {
        const val PAYLOAD_RATE_CHANGED = 1
    }
}
