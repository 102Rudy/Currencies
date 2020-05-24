package com.rygital.feature_currency_list_impl.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rygital.feature_currency_list_impl.databinding.ItemCurrencyBinding
import com.rygital.feature_currency_list_impl.presentation.viewdata.CurrencyViewData

internal class CurrencyListAdapter : RecyclerView.Adapter<CurrencyListAdapter.CurrencyHolder>() {

    private val items: MutableList<CurrencyViewData> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyHolder =
        CurrencyHolder(
            ItemCurrencyBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(
        holder: CurrencyHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            holder.bind(items[position], payloads)
        }
    }

    override fun onBindViewHolder(holder: CurrencyHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun setItems(newItems: List<CurrencyViewData>) {
        items.clear()
        items.addAll(newItems)
    }

    class CurrencyHolder(
        private val binding: ItemCurrencyBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CurrencyViewData) {
            binding.tvCurrencyCode.text = item.code
            binding.tvCurrencyRate.setText(item.rate)
        }

        fun bind(item: CurrencyViewData, payloads: MutableList<Any>) {
            (payloads.firstOrNull() as? Int?)?.let {
                if (it == CurrencyViewData.PAYLOAD_RATE_CHANGED) {
                    binding.tvCurrencyRate.setText(item.rate)
                }
            }
        }
    }
}
