package com.rygital.feature_currency_list_impl

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.rygital.core.rx.SchedulerProvider
import com.rygital.feature_currency_list_impl.domain.CurrencyInteractorImpl
import com.rygital.feature_currency_list_impl.domain.CurrencyRepository
import com.rygital.feature_currency_list_impl.domain.model.CurrencyRateModel
import com.rygital.feature_currency_list_impl.domain.model.ExchangeRatesModel
import com.rygital.feature_currency_list_impl.utils.TestSchedulerProviderImpl
import com.rygital.feature_currency_list_impl.utils.createExchangeRatesModel
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.observers.TestObserver
import org.junit.Before
import org.junit.Test

class CurrencyInteractorImplTest {

    private lateinit var currencyInteractor: CurrencyInteractorImpl

    private lateinit var schedulerProvider: SchedulerProvider
    private lateinit var currencyRepository: CurrencyRepository

    @Before
    fun setup() {
        schedulerProvider = TestSchedulerProviderImpl()
        currencyRepository = mock()

        currencyInteractor = CurrencyInteractorImpl(schedulerProvider, currencyRepository)
    }

    @Test
    fun `getRates for EUR and value = 1`() {
        // mock
        whenever(currencyRepository.getLatestRates("EUR"))
            .thenReturn(
                Single.just(
                    createExchangeRatesModel(
                        "EUR"
                    )
                )
            )

        val observer = TestObserver<ExchangeRatesModel>()

        // action
        currencyInteractor.getRates("EUR", 1.0)
            .subscribe(observer)

        // verify
        observer.assertValue(
            createExchangeRatesModel(
                "EUR"
            )
        )
        observer.assertComplete()
    }

    @Test
    fun `getRates for BGN and value = 2`() {
        // mock
        whenever(currencyRepository.getLatestRates("BGN"))
            .thenReturn(
                Single.just(
                    createExchangeRatesModel(
                        "BGN"
                    )
                )
            )

        val observer = TestObserver<ExchangeRatesModel>()

        // action
        currencyInteractor.getRates("BGN", 2.0)
            .subscribe(observer)

        // verify
        observer.assertValue(
            createExchangeRatesModel(
                "BGN",
                2.0
            )
        )
        observer.assertComplete()
    }

    @Test
    fun `getRatesRelativeToBase for BGN and value = 1 with base BRL`() {
        // mock
        currencyInteractor.exchangeRatesCache =
            createExchangeRatesModel(
                "BRL"
            )

        val observer = TestObserver<ExchangeRatesModel>()

        // action
        currencyInteractor.getRatesRelativeToBase("BGN", 1.0)
            .subscribe(observer)

        // verify
        val assertValue = ExchangeRatesModel(
            CurrencyRateModel("BGN", 1.0),
            listOf(
                CurrencyRateModel("AUD", 0.5),
                CurrencyRateModel("BRL", 0.5),
                CurrencyRateModel("EUR", 2.0)
            )
        )
        observer.assertValue(assertValue)
        observer.assertComplete()
    }
}
