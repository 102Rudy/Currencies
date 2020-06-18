package com.rygital.feature_currency_list_impl.presentation.viewdata

import androidx.annotation.DrawableRes

data class CurrencyViewData(
    val code: String,
    val title: String,
    val rate: String,
    @DrawableRes val icon: Int
) {
    companion object {
        const val PAYLOAD_RATE_CHANGED = 1
    }
}
