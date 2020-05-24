package com.rygital.feature_currency_list_impl.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.rygital.core_ui.BaseFragment
import com.rygital.feature_currency_list_impl.databinding.FragmentCurrencyListBinding
import com.rygital.feature_currency_list_impl.di.CurrencyListScreenComponent
import com.rygital.feature_currency_list_impl.presentation.viewdata.CurrencyViewData
import javax.inject.Inject

internal class CurrencyListFragment : BaseFragment<CurrencyListPresenter, CurrencyListView>(),
    CurrencyListView {

    @Inject
    lateinit var adapter: CurrencyListAdapter

    private lateinit var binding: FragmentCurrencyListBinding

    override fun performInject() {
        CurrencyListScreenComponent.getAndInject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        FragmentCurrencyListBinding.inflate(inflater).also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvCurrencyList.run {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = this@CurrencyListFragment.adapter
        }

        presenter.startRatesUpdate()
    }

    override fun onDestroy() {
        if (requireActivity().isFinishing) {
            CurrencyListScreenComponent.clearComponent()
        }
        super.onDestroy()
    }

    override fun setItems(list: List<CurrencyViewData>, diffResult: DiffUtil.DiffResult) {
        adapter.setItems(list)
        diffResult.dispatchUpdatesTo(adapter)
    }
}
