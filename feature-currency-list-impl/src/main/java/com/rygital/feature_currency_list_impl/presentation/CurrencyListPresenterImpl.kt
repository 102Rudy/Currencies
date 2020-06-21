package com.rygital.feature_currency_list_impl.presentation

import androidx.recyclerview.widget.DiffUtil
import com.rygital.core_utils.rx.SchedulerProvider
import com.rygital.core_ui.BasePresenterImpl
import com.rygital.feature_currency_list_impl.domain.CurrencyInteractor
import com.rygital.feature_currency_list_impl.domain.model.CurrencyRateModel
import com.rygital.feature_currency_list_impl.domain.model.ExchangeRatesModel
import com.rygital.feature_currency_list_impl.presentation.viewdata.CurrencyViewData
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Notification
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private sealed class UiResult {
    class ListAndDiff(
        val currencyViewDataList: List<CurrencyViewData>,
        val diffResult: DiffUtil.DiffResult?
    ) : UiResult()

    object Error : UiResult()
}

internal class CurrencyListPresenterImpl @Inject constructor(
    private val schedulerProvider: SchedulerProvider,
    private val currencyInteractor: CurrencyInteractor,
    private val currencyViewDataConverter: CurrencyViewDataConverter
) : BasePresenterImpl<CurrencyListView>(), CurrencyListPresenter {

    companion object {
        private const val DEFAULT_CURRENCY_VALUE = 1.0
        private const val ZERO_CURRENCY_VALUE = 0.0

        private const val DEFAULT_CURRENCY_CODE = "EUR"
        private const val RATES_UPDATE_INTERVAL_MILLIS = 1000L
    }

    private val exchangeRatesSubject: BehaviorSubject<Notification<ExchangeRatesModel>> =
        BehaviorSubject.create()

    private var currentCurrencyCode = DEFAULT_CURRENCY_CODE
    private var currentValue = DEFAULT_CURRENCY_VALUE

    private var currentViewDataList: List<CurrencyViewData> = emptyList()

    override fun attachView(view: CurrencyListView) {
        super.attachView(view)

        addDisposable(
            exchangeRatesSubject
                .subscribeOn(schedulerProvider.io())
                .concatMap { notification ->
                    if (notification.isOnNext) {
                        convertToListAndDiff(notification.value)
                    } else {
                        Observable.just(UiResult.Error)
                    }
                }
                .observeOn(schedulerProvider.ui())
                .subscribe(
                    { result ->
                        when (result) {
                            is UiResult.ListAndDiff -> displayCurrencyList(result)
                            is UiResult.Error -> displayError()
                        }
                    },
                    { it.printStackTrace() }
                ))

        startRatesUpdate()
    }

    private fun convertToListAndDiff(rates: ExchangeRatesModel): Observable<UiResult.ListAndDiff> =
        Observable.just(rates)
            .subscribeOn(schedulerProvider.computation())
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
            .scan(UiResult.ListAndDiff(currentViewDataList, null)) { prev, next ->
                val diffUtilCallback = CurrenciesDiffUtilCallback(prev.currencyViewDataList, next)
                val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(diffUtilCallback)

                currentViewDataList = next

                return@scan UiResult.ListAndDiff(next, diffResult)
            }
            .skip(1)

    private fun displayCurrencyList(listAndDiff: UiResult.ListAndDiff) {
        listAndDiff.diffResult ?: throw IllegalStateException("diffResult can not be null here")

        view?.run {
            setShimmerVisibility(false)
            setErrorVisibility(false)
            setItems(listAndDiff.currencyViewDataList, listAndDiff.diffResult)
        }
    }

    private fun displayError() {
        view?.run {
            hideItems()
            clearCachedItems()

            setShimmerVisibility(false)
            setErrorVisibility(true)
        }
    }

    private fun startRatesUpdate() {
        addDisposable(
            Flowable.interval(0, RATES_UPDATE_INTERVAL_MILLIS, TimeUnit.MILLISECONDS)
                .subscribeOn(schedulerProvider.io())
                .flatMap {
                    currencyInteractor.getRates(currentCurrencyCode, currentValue).toFlowable()
                }
                .materialize()
                .subscribe(
                    { items -> exchangeRatesSubject.onNext(items) },
                    exchangeRatesSubject::onError
                )
        )
    }

    // region CurrencyListPresenter
    override fun setInitialValues(currencyCode: String, value: Double) {
        currentCurrencyCode = currencyCode
        currentValue = value
    }

    override fun onBtnTryAgainClick() {
        startRatesUpdate()
    }

    override fun saveInstanceState(saveCallback: (currencyCode: String, value: Double) -> Unit) {
        saveCallback(currentCurrencyCode, currentValue)
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

    override fun clearCachedItems() {
        currentViewDataList = emptyList()
    }

    private fun updateRatesRelativeToCurrent() {
        addDisposable(
            currencyInteractor.getRatesRelativeToBase(currentCurrencyCode, currentValue)
                .subscribeOn(schedulerProvider.io())
                .materialize()
                .subscribe(
                    { items -> exchangeRatesSubject.onNext(items) },
                    exchangeRatesSubject::onError
                )
        )
    }
    // endregion
}
