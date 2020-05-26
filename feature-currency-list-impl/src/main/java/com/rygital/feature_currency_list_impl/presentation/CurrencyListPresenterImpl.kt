package com.rygital.feature_currency_list_impl.presentation

import androidx.recyclerview.widget.DiffUtil
import com.rygital.core.rx.SchedulerProvider
import com.rygital.core_ui.BasePresenterImpl
import com.rygital.feature_currency_list_impl.domain.CurrencyInteractor
import com.rygital.feature_currency_list_impl.domain.model.CurrencyRateModel
import com.rygital.feature_currency_list_impl.presentation.viewdata.CurrencyViewData
import java.util.concurrent.TimeUnit
import javax.inject.Inject

internal class CurrencyListPresenterImpl @Inject constructor(
    private val schedulerProvider: SchedulerProvider,
    private val currencyInteractor: CurrencyInteractor
) : BasePresenterImpl<CurrencyListView>(), CurrencyListPresenter {

    private var initialListAndDiffResult: Pair<List<CurrencyViewData>, DiffUtil.DiffResult?> =
        emptyList<CurrencyViewData>() to null

    override fun startRatesUpdate() {
        addDisposable(currencyInteractor.getRates()
            .subscribeOn(schedulerProvider.io())
            .delay(1000, TimeUnit.MILLISECONDS)
            .repeat()
            .observeOn(schedulerProvider.computation())
            .map { exchangeRates ->
                val firstItem = CurrencyRateModel(exchangeRates.baseCurrency, 1.0)

                mutableListOf<CurrencyViewData>().apply {
                    add(CurrencyViewData(firstItem.code, "", firstItem.rate.toString(), 0))

                    exchangeRates.rates.forEach { currencyRate ->
                        if (currencyRate.code != firstItem.code) {
                            val currencyViewData = CurrencyViewData(
                                currencyRate.code,
                                "",
                                currencyRate.rate.toString(),
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

                initialListAndDiffResult = next to null

                return@scan next to diffResult
            }
            .skip(1)
            .observeOn(schedulerProvider.ui())
            .subscribe(
                { (items, diffResult) -> view?.setItems(items, diffResult!!) }, // todo temp !!
                { throwable -> throwable.printStackTrace() }
            )
        )
    }

    override fun selectItem(item: CurrencyViewData) {
    }

    override fun setRate(item: CurrencyViewData, newRate: String) {
    }
}
