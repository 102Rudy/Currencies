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
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

internal class CurrencyListPresenterImpl @Inject constructor(
    private val schedulerProvider: SchedulerProvider,
    private val currencyInteractor: CurrencyInteractor
) : BasePresenterImpl<CurrencyListView>(), CurrencyListPresenter {

    companion object {
        private const val DEFAULT_CURRENCY_CODE = "EUR"
        private const val RATES_UPDATE_INTERVAL_MILLIS = 1000L
        private const val CURRENCY_RATE_FORMAT = "%.2f"
    }

    private val exchangeRatesSubject: BehaviorSubject<ExchangeRatesModel> = BehaviorSubject.create()

    private var currentCurrencyCode = DEFAULT_CURRENCY_CODE
    private var currentValue = 1.0

    override fun attachView(view: CurrencyListView) {
        super.attachView(view)

        val initialListAndDiffResult: Pair<List<CurrencyViewData>, DiffUtil.DiffResult?> =
            emptyList<CurrencyViewData>() to null

        addDisposable(exchangeRatesSubject
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.computation())
            .map { exchangeRates ->
                val firstItem = CurrencyRateModel(exchangeRates.baseCurrency, 1.0)

                mutableListOf<CurrencyViewData>().apply {
                    val firstViewData = CurrencyViewData(
                        firstItem.code,
                        "",
                        String.format(CURRENCY_RATE_FORMAT, firstItem.rate),
                        0
                    )

                    add(firstViewData)

                    exchangeRates.rates.forEach { currencyRate ->
                        if (currencyRate.code != firstItem.code) {
                            val currencyViewData = CurrencyViewData(
                                currencyRate.code,
                                "",
                                String.format(CURRENCY_RATE_FORMAT, currencyRate.rate),
                                0
                            )

                            add(currencyViewData)
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

    override fun startRatesUpdate() {
        addDisposable(
            Flowable.interval(RATES_UPDATE_INTERVAL_MILLIS, TimeUnit.MILLISECONDS)
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
        Timber.i("select $item")
        currentCurrencyCode = item.code
        currentValue = item.rate.toDouble()

        addDisposable(
            currencyInteractor.getRatesRelativeToBase(currentCurrencyCode, currentValue)
                .subscribeOn(schedulerProvider.io())
                .subscribe(
                    { items -> exchangeRatesSubject.onNext(items) },
                    { throwable -> throwable.printStackTrace() }
                )
        )
    }

    override fun setRate(item: CurrencyViewData, newRate: String) {
    }
}
