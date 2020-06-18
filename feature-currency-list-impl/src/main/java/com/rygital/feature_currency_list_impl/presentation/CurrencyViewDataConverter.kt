package com.rygital.feature_currency_list_impl.presentation

import androidx.annotation.DrawableRes
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

        private const val CURRENCY_CODE_AUD = "AUD"
        private const val CURRENCY_CODE_BGN = "BGN"
        private const val CURRENCY_CODE_BRL = "BRL"
        private const val CURRENCY_CODE_CAD = "CAD"
        private const val CURRENCY_CODE_CHF = "CHF"
        private const val CURRENCY_CODE_CNY = "CNY"
        private const val CURRENCY_CODE_CZK = "CZK"
        private const val CURRENCY_CODE_DKK = "DKK"
        private const val CURRENCY_CODE_EUR = "EUR"
        private const val CURRENCY_CODE_GBP = "GBP"
        private const val CURRENCY_CODE_HKD = "HKD"
        private const val CURRENCY_CODE_HRK = "HRK"
        private const val CURRENCY_CODE_HUF = "HUF"
        private const val CURRENCY_CODE_IDR = "IDR"
        private const val CURRENCY_CODE_ILS = "ILS"
        private const val CURRENCY_CODE_INR = "INR"
        private const val CURRENCY_CODE_ISK = "ISK"
        private const val CURRENCY_CODE_JPY = "JPY"
        private const val CURRENCY_CODE_KRW = "KRW"
        private const val CURRENCY_CODE_MXN = "MXN"
        private const val CURRENCY_CODE_MYR = "MYR"
        private const val CURRENCY_CODE_NOK = "NOK"
        private const val CURRENCY_CODE_NZD = "NZD"
        private const val CURRENCY_CODE_PHP = "PHP"
        private const val CURRENCY_CODE_PLN = "PLN"
        private const val CURRENCY_CODE_RON = "RON"
        private const val CURRENCY_CODE_RUB = "RUB"
        private const val CURRENCY_CODE_SEK = "SEK"
        private const val CURRENCY_CODE_SGD = "SGD"
        private const val CURRENCY_CODE_THB = "THB"
        private const val CURRENCY_CODE_USD = "USD"
        private const val CURRENCY_CODE_ZAR = "ZAR"
    }

    private val currencyTitles =
        mapOf(
            CURRENCY_CODE_AUD to R.string.currency_title_aud,
            CURRENCY_CODE_BGN to R.string.currency_title_bgn,
            CURRENCY_CODE_BRL to R.string.currency_title_brl,
            CURRENCY_CODE_CAD to R.string.currency_title_cad,
            CURRENCY_CODE_CHF to R.string.currency_title_chf,
            CURRENCY_CODE_CNY to R.string.currency_title_cny,
            CURRENCY_CODE_CZK to R.string.currency_title_czk,
            CURRENCY_CODE_DKK to R.string.currency_title_dkk,
            CURRENCY_CODE_EUR to R.string.currency_title_eur,
            CURRENCY_CODE_GBP to R.string.currency_title_gbp,
            CURRENCY_CODE_HKD to R.string.currency_title_hkd,
            CURRENCY_CODE_HRK to R.string.currency_title_hrk,
            CURRENCY_CODE_HUF to R.string.currency_title_huf,
            CURRENCY_CODE_IDR to R.string.currency_title_idr,
            CURRENCY_CODE_ILS to R.string.currency_title_ils,
            CURRENCY_CODE_INR to R.string.currency_title_inr,
            CURRENCY_CODE_ISK to R.string.currency_title_isk,
            CURRENCY_CODE_JPY to R.string.currency_title_jpy,
            CURRENCY_CODE_KRW to R.string.currency_title_krw,
            CURRENCY_CODE_MXN to R.string.currency_title_mxn,
            CURRENCY_CODE_MYR to R.string.currency_title_myr,
            CURRENCY_CODE_NOK to R.string.currency_title_nok,
            CURRENCY_CODE_NZD to R.string.currency_title_nzd,
            CURRENCY_CODE_PHP to R.string.currency_title_php,
            CURRENCY_CODE_PLN to R.string.currency_title_pln,
            CURRENCY_CODE_RON to R.string.currency_title_ron,
            CURRENCY_CODE_RUB to R.string.currency_title_rub,
            CURRENCY_CODE_SEK to R.string.currency_title_sek,
            CURRENCY_CODE_SGD to R.string.currency_title_sgd,
            CURRENCY_CODE_THB to R.string.currency_title_thb,
            CURRENCY_CODE_USD to R.string.currency_title_usd,
            CURRENCY_CODE_ZAR to R.string.currency_title_zar
        )

    private val currencyIcons =
        mapOf(
            CURRENCY_CODE_AUD to R.drawable.ic_aud,
            CURRENCY_CODE_BGN to R.drawable.ic_bgn,
            CURRENCY_CODE_BRL to R.drawable.ic_brl,
            CURRENCY_CODE_CAD to R.drawable.ic_cad,
            CURRENCY_CODE_CHF to R.drawable.ic_chf,
            CURRENCY_CODE_CNY to R.drawable.ic_cny,
            CURRENCY_CODE_CZK to R.drawable.ic_czk,
            CURRENCY_CODE_DKK to R.drawable.ic_dkk,
            CURRENCY_CODE_EUR to R.drawable.ic_eur,
            CURRENCY_CODE_GBP to R.drawable.ic_gbp,
            CURRENCY_CODE_HKD to R.drawable.ic_hkd,
            CURRENCY_CODE_HRK to R.drawable.ic_hrk,
            CURRENCY_CODE_HUF to R.drawable.ic_huf,
            CURRENCY_CODE_IDR to R.drawable.ic_idr,
            CURRENCY_CODE_ILS to R.drawable.ic_ils,
            CURRENCY_CODE_INR to R.drawable.ic_inr,
            CURRENCY_CODE_JPY to R.drawable.ic_jpy,
            CURRENCY_CODE_KRW to R.drawable.ic_krw,
            CURRENCY_CODE_MXN to R.drawable.ic_mxn,
            CURRENCY_CODE_MYR to R.drawable.ic_myr,
            CURRENCY_CODE_NOK to R.drawable.ic_nok,
            CURRENCY_CODE_NZD to R.drawable.ic_nzd,
            CURRENCY_CODE_PHP to R.drawable.ic_php,
            CURRENCY_CODE_PLN to R.drawable.ic_pln,
            CURRENCY_CODE_RON to R.drawable.ic_ron,
            CURRENCY_CODE_RUB to R.drawable.ic_rub,
            CURRENCY_CODE_SEK to R.drawable.ic_sek,
            CURRENCY_CODE_SGD to R.drawable.ic_sgd,
            CURRENCY_CODE_THB to R.drawable.ic_thb,
            CURRENCY_CODE_USD to R.drawable.ic_usd,
            CURRENCY_CODE_ZAR to R.drawable.ic_zar
        )

    override fun convertToViewData(currencyRateModel: CurrencyRateModel): CurrencyViewData =
        CurrencyViewData(
            currencyRateModel.code,
            getCurrencyTitle(currencyRateModel.code),
            String.format(CURRENCY_RATE_FORMAT, currencyRateModel.rate),
            getCurrencyImageRes(currencyRateModel.code)
        )

    private fun getCurrencyTitle(currencyCode: String): String {
        val titleRes = currencyTitles[currencyCode] ?: R.string.currency_title_undefined
        return resourceProvider.getString(titleRes)
    }

    @DrawableRes
    private fun getCurrencyImageRes(currencyCode: String): Int =
        currencyIcons[currencyCode] ?: R.drawable.ic_question
}
