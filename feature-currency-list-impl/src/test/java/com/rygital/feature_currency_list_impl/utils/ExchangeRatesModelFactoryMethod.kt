package com.rygital.feature_currency_list_impl.utils

import com.rygital.feature_currency_list_impl.domain.model.CurrencyRateModel
import com.rygital.feature_currency_list_impl.domain.model.ExchangeRatesModel
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

internal fun createExchangeRatesModel(
    baseCurrency: String,
    valueMultiplier: Double = 1.0,
    baseCurrencyValue: Double = 1.0
): ExchangeRatesModel =
    ExchangeRatesModel(
        CurrencyRateModel(baseCurrency, baseCurrencyValue),
        listOf(
            CurrencyRateModel("AUD", 1.0 * valueMultiplier),
            CurrencyRateModel("BGN", 2.0 * valueMultiplier),
            CurrencyRateModel("BRL", 3.0 * valueMultiplier),
            CurrencyRateModel("EUR", 4.0 * valueMultiplier)
        ).filterNot { it.code == baseCurrency }
    )

class CreateExchangeRatesModelTest {
    @Test
    fun `createExchangeRatesModel returns expected results`() {
        assertEquals(
            ExchangeRatesModel(
                CurrencyRateModel("BGN", 1.0),
                listOf(
                    CurrencyRateModel("AUD", 1.0),
                    CurrencyRateModel("BRL", 3.0),
                    CurrencyRateModel("EUR", 4.0)
                )
            ),
            createExchangeRatesModel(
                "BGN"
            )
        )

        assertEquals(
            ExchangeRatesModel(
                CurrencyRateModel("EUR", 1.0),
                listOf(
                    CurrencyRateModel("AUD", 1.0),
                    CurrencyRateModel("BGN", 2.0),
                    CurrencyRateModel("BRL", 3.0)
                )
            ),
            createExchangeRatesModel(
                "EUR"
            )
        )

        assertEquals(
            ExchangeRatesModel(
                CurrencyRateModel("EUR", 1.0),
                listOf(
                    CurrencyRateModel("AUD", 3.5),
                    CurrencyRateModel("BGN", 7.0),
                    CurrencyRateModel("BRL", 10.5)
                )
            ),
            createExchangeRatesModel(
                "EUR",
                3.5
            )
        )

        assertNotEquals(
            ExchangeRatesModel(
                CurrencyRateModel("EUR", 1.0),
                listOf(
                    CurrencyRateModel("AUD", 1.0),
                    CurrencyRateModel("BRL", 3.0),
                    CurrencyRateModel("BGN", 2.0)
                )
            ),
            createExchangeRatesModel(
                "EUR"
            )
        )

        assertNotEquals(
            ExchangeRatesModel(
                CurrencyRateModel("EUR", 1.0),
                listOf(
                    CurrencyRateModel("AUD", 1.0),
                    CurrencyRateModel("BRL", 3.0),
                    CurrencyRateModel("BGN", 2.0)
                )
            ),
            createExchangeRatesModel(
                "EUR",
                2.0
            )
        )
    }
}
