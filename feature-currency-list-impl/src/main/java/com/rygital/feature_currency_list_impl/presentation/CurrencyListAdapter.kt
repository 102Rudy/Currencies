package com.rygital.feature_currency_list_impl.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rygital.feature_currency_list_impl.databinding.ItemCurrencyBinding
import com.rygital.feature_currency_list_impl.presentation.utils.SimpleTextWatcher
import com.rygital.feature_currency_list_impl.presentation.viewdata.CurrencyViewData
import javax.inject.Inject

internal class CurrencyListAdapter @Inject constructor(
    private val presenter: CurrencyListPresenter
) : RecyclerView.Adapter<CurrencyListAdapter.CurrencyHolder>() {

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

    inner class CurrencyHolder(
        private val binding: ItemCurrencyBinding
    ) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        private var currencyViewData: CurrencyViewData? = null

        init {
            binding.root.setOnClickListener(this)
            binding.etCurrencyRate.setOnFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    onClick(view)
                }
            }

            binding.etCurrencyRate.addTextChangedListener(
                object : SimpleTextWatcher() {
                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        if (!binding.etCurrencyRate.isFocused ||
                            adapterPosition == RecyclerView.NO_POSITION ||
                            s == null
                        ) {
                            return
                        }

                        currencyViewData?.let {
                            presenter.setRate(it, s.toString())
                        }
                    }
                })
        }

        fun bind(item: CurrencyViewData) {
            currencyViewData = item

            binding.tvCurrencyCode.text = item.code
            binding.etCurrencyRate.setText(item.rate)
        }

        fun bind(item: CurrencyViewData, payloads: MutableList<Any>) {
            if (binding.etCurrencyRate.isFocused) {
                return
            }

            currencyViewData = item

            (payloads.firstOrNull() as? Int?)?.let {
                if (it == CurrencyViewData.PAYLOAD_RATE_CHANGED) {
                    binding.etCurrencyRate.setText(item.rate)
                }
            }
        }

        override fun onClick(view: View?) {
            if (adapterPosition == RecyclerView.NO_POSITION) {
                return
            }

            binding.etCurrencyRate.requestFocus()

            currencyViewData?.let {
                presenter.selectItem(it)
            }
        }
    }
}
