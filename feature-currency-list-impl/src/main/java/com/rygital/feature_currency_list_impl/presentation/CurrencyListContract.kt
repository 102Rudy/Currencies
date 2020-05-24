package com.rygital.feature_currency_list_impl.presentation

import androidx.recyclerview.widget.DiffUtil
import com.rygital.core_ui.BasePresenter
import com.rygital.core_ui.MvpView
import com.rygital.feature_currency_list_impl.presentation.viewdata.CurrencyViewData

internal interface CurrencyListView : MvpView {
    fun setItems(list: List<CurrencyViewData>, diffResult: DiffUtil.DiffResult)
}

internal interface CurrencyListPresenter : BasePresenter<CurrencyListView> {
    fun startRatesUpdate()
}
