package com.rygital.feature_currency_list_impl.presentation

import androidx.recyclerview.widget.DiffUtil
import com.rygital.feature_currency_list_impl.presentation.viewdata.CurrencyViewData

internal class CurrenciesDiffUtilCallback(
    private val oldList: List<CurrencyViewData>,
    private val newList: List<CurrencyViewData>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.count()

    override fun getNewListSize(): Int = newList.count()

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return oldItem.code == newItem.code
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return oldItem == newItem
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        if (oldItem.rate != newItem.rate) {
            return CurrencyViewData.PAYLOAD_RATE_CHANGED
        }

        return null
    }
}
