package com.rygital.feature_currency_list_impl.presentation

import androidx.recyclerview.widget.DiffUtil
import com.rygital.core.rx.SchedulerProvider
import com.rygital.core_ui.BasePresenterImpl
import com.rygital.feature_currency_list_impl.domain.CurrencyInteractor
import com.rygital.feature_currency_list_impl.domain.model.CurrencyRateModel
import com.rygital.feature_currency_list_impl.domain.model.ExchangeRatesModel
import com.rygital.feature_currency_list_impl.presentation.viewdata.CurrencyViewData
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

internal class CurrencyListPresenterImpl @Inject constructor(
    private val schedulerProvider: SchedulerProvider,
    private val currencyInteractor: CurrencyInteractor,
    private val currencyViewDataConverter: CurrencyViewDataConverter
) : BasePresenterImpl<CurrencyListView>(), CurrencyListPresenter {

    companion object {
        private const val DEFAULT_CURRENCY_VALUE = 1.0
        private const val ZERO_CURRENCY_VALUE = 0.0

        private const val DEFAULT_CURRENCY_CODE = "EUR"
        private const val RATES_UPDATE_INTERVAL_MILLIS = 10000L
    }

    private val exchangeRatesSubject: BehaviorSubject<ExchangeRatesModel> = BehaviorSubject.create()

    private var currentCurrencyCode = DEFAULT_CURRENCY_CODE
    private var currentValue = DEFAULT_CURRENCY_VALUE

    override fun attachView(view: CurrencyListView) {
        super.attachView(view)

        val initialListAndDiffResult: Pair<List<CurrencyViewData>, DiffUtil.DiffResult?> =
            emptyList<CurrencyViewData>() to null

        addDisposable(exchangeRatesSubject
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.computation())
            .map { exchangeRates ->
                val firstItem = CurrencyRateModel(
                    exchangeRates.baseCurrencyRate.code,
                    exchangeRates.baseCurrencyRate.rate
                )

                mutableListOf<CurrencyViewData>().apply {
                    add(currencyViewDataConverter.convertToViewData(firstItem))

                    exchangeRates.rates.forEach { currencyRate ->
                        if (currencyRate.code != firstItem.code) {
                            add(currencyViewDataConverter.convertToViewData(currencyRate))
                        }
                    }
                }
            }
            .scan(initialListAndDiffResult) { prev, next ->
                val diffUtilCallback = CurrenciesDiffUtilCallback(prev.first, next)
                val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(diffUtilCallback)

                return@scan next to diffResult
            }
            .skip(1)
            .observeOn(schedulerProvider.ui())
            .subscribe(
                { (items, diffResult) -> this.view?.setItems(items, diffResult!!) },
                { throwable -> throwable.printStackTrace() }
            ))
    }

    // region CurrencyListPresenter
    override fun setInitialValues(currencyCode: String, value: Double) {
        currentCurrencyCode = currencyCode
        currentValue = value
    }

    override fun saveInstanceState(saveCallback: (currencyCode: String, value: Double) -> Unit) {
        saveCallback(currentCurrencyCode, currentValue)
    }

    override fun startRatesUpdate() {
        addDisposable(
            Flowable.interval(0, RATES_UPDATE_INTERVAL_MILLIS, TimeUnit.MILLISECONDS)
                .subscribeOn(schedulerProvider.io())
                .flatMap {
                    currencyInteractor.getRates(currentCurrencyCode, currentValue).toFlowable()
                }
                .subscribe(
                    { items -> exchangeRatesSubject.onNext(items) },
                    { throwable -> throwable.printStackTrace() }
                )
        )
    }

    override fun selectItem(item: CurrencyViewData) {
        currentCurrencyCode = item.code
        currentValue = item.rate.toDouble()

        updateRatesRelativeToCurrent()
    }

    override fun setRate(item: CurrencyViewData, newRate: String) {
        currentCurrencyCode = item.code
        currentValue =
            if (newRate.isBlank()) {
                ZERO_CURRENCY_VALUE
            } else {
                newRate.toDouble()
            }

        updateRatesRelativeToCurrent()
    }

    private fun updateRatesRelativeToCurrent() {
        addDisposable(
            currencyInteractor.getRatesRelativeToBase(currentCurrencyCode, currentValue)
                .subscribeOn(schedulerProvider.io())
                .subscribe(
                    { items -> exchangeRatesSubject.onNext(items) },
                    { throwable -> throwable.printStackTrace() }
                )
        )
    }
    // endregion
}
