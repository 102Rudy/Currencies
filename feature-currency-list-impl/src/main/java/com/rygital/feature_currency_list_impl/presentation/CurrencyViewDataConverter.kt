package com.rygital.feature_currency_list_impl.presentation

import com.rygital.core.ResourceProvider
import com.rygital.feature_currency_list_impl.R
import com.rygital.feature_currency_list_impl.domain.model.CurrencyRateModel
import com.rygital.feature_currency_list_impl.presentation.viewdata.CurrencyViewData
import javax.inject.Inject

internal interface CurrencyViewDataConverter {
    fun convertToViewData(currencyRateModel: CurrencyRateModel): CurrencyViewData
}

internal class CurrencyViewDataConverterImpl @Inject constructor(
    private val resourceProvider: ResourceProvider
) : CurrencyViewDataConverter {

    companion object {
        private const val CURRENCY_RATE_FORMAT = "%.2f"
    }

    private val currencyTitles =
        mapOf(
            "AUD" to R.string.currency_title_aud,
            "BGN" to R.string.currency_title_bgn,
            "BRL" to R.string.currency_title_brl,
            "CAD" to R.string.currency_title_cad,
            "CHF" to R.string.currency_title_chf,
            "CNY" to R.string.currency_title_cny,
            "CZK" to R.string.currency_title_czk,
            "DKK" to R.string.currency_title_dkk,
            "EUR" to R.string.currency_title_eur,
            "GBP" to R.string.currency_title_gbp,
            "HKD" to R.string.currency_title_hkd,
            "HRK" to R.string.currency_title_hrk,
            "HUF" to R.string.currency_title_huf,
            "IDR" to R.string.currency_title_idr,
            "ILS" to R.string.currency_title_ils,
            "INR" to R.string.currency_title_inr,
            "ISK" to R.string.currency_title_isk,
            "JPY" to R.string.currency_title_jpy,
            "KRW" to R.string.currency_title_krw,
            "MXN" to R.string.currency_title_mxn,
            "MYR" to R.string.currency_title_myr,
            "NOK" to R.string.currency_title_nok,
            "NZD" to R.string.currency_title_nzd,
            "PHP" to R.string.currency_title_php,
            "PLN" to R.string.currency_title_pln,
            "RON" to R.string.currency_title_ron,
            "RUB" to R.string.currency_title_rub,
            "SEK" to R.string.currency_title_sek,
            "SGD" to R.string.currency_title_sgd,
            "THB" to R.string.currency_title_thb,
            "USD" to R.string.currency_title_usd,
            "ZAR" to R.string.currency_title_zar
        )

    override fun convertToViewData(currencyRateModel: CurrencyRateModel): CurrencyViewData =
        CurrencyViewData(
            currencyRateModel.code,
            getCurrencyTitle(currencyRateModel.code),
            String.format(CURRENCY_RATE_FORMAT, currencyRateModel.rate),
            0
        )

    private fun getCurrencyTitle(currencyCode: String): String {
        val titleRes = currencyTitles[currencyCode] ?: R.string.currency_title_undefined
        return resourceProvider.getString(titleRes)
    }
}
