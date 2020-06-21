package com.rygital.feature_currency_list_impl.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.rygital.core_utils.setVisibleOrGone
import com.rygital.core_ui.BaseFragment
import com.rygital.feature_currency_list_impl.R
import com.rygital.feature_currency_list_impl.databinding.FragmentCurrencyListBinding
import com.rygital.feature_currency_list_impl.di.CurrencyListScreenComponent
import com.rygital.feature_currency_list_impl.presentation.viewdata.CurrencyViewData
import javax.inject.Inject

internal class CurrencyListFragment : BaseFragment<CurrencyListPresenter, CurrencyListView>(),
    CurrencyListView {

    companion object {
        private const val BUNDLE_KEY_CURRENCY_CODE = "bundle_key_currency_code"
        private const val BUNDLE_KEY_CURRENCY_VALUE = "bundle_key_currency_value"
    }

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

        binding.toolbar.setTitle(R.string.title_rates)

        binding.rvCurrencyList.run {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = this@CurrencyListFragment.adapter
        }

        binding.errorState.btnTryAgain.setOnClickListener {
            presenter.onBtnTryAgainClick()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        savedInstanceState?.let {
            val currencyCode = it.getString(BUNDLE_KEY_CURRENCY_CODE) ?: return@let
            val value = it.getDouble(BUNDLE_KEY_CURRENCY_VALUE)
            presenter.setInitialValues(currencyCode, value)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        presenter.saveInstanceState { currencyCode, value ->
            outState.putString(BUNDLE_KEY_CURRENCY_CODE, currencyCode)
            outState.putDouble(BUNDLE_KEY_CURRENCY_VALUE, value)
        }
    }

    override fun onDestroyView() {
        presenter.clearCachedItems()
        binding.rvCurrencyList.adapter = null
        super.onDestroyView()
    }

    override fun onDestroy() {
        if (requireActivity().isFinishing) {
            CurrencyListScreenComponent.clearComponent()
        }
        super.onDestroy()
    }

    // region CurrencyListView
    override fun setItems(list: List<CurrencyViewData>, diffResult: DiffUtil.DiffResult) {
        binding.rvCurrencyList.setVisibleOrGone(true)
        adapter.setItems(list)
        diffResult.dispatchUpdatesTo(adapter)
    }

    override fun hideItems() {
        adapter.setItems(emptyList())
        adapter.notifyDataSetChanged()
        binding.rvCurrencyList.setVisibleOrGone(false)
    }

    override fun setShimmerVisibility(isVisible: Boolean) {
        binding.shimmerLayout.root.run {
            if (isVisible) {
                showShimmer(true)
            } else {
                hideShimmer()
            }

            setVisibleOrGone(isVisible)
        }
    }

    override fun setErrorVisibility(isVisible: Boolean) {
        binding.errorState.root.setVisibleOrGone(isVisible)
    }
    // endregion
}
