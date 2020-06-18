package com.rygital.feature_currency_list_impl.presentation

import androidx.recyclerview.widget.DiffUtil
import com.rygital.core_ui.BasePresenter
import com.rygital.core_ui.MvpView
import com.rygital.feature_currency_list_impl.presentation.viewdata.CurrencyViewData

internal interface CurrencyListView : MvpView {
    fun setItems(list: List<CurrencyViewData>, diffResult: DiffUtil.DiffResult)
}

internal interface CurrencyListPresenter : BasePresenter<CurrencyListView> {
    fun setInitialValues(currencyCode: String, value: Double)
    fun saveInstanceState(saveCallback: (currencyCode: String, value: Double) -> Unit)
    fun selectItem(item: CurrencyViewData)
    fun setRate(item: CurrencyViewData, newRate: String)
    fun clearCachedItems()
}
