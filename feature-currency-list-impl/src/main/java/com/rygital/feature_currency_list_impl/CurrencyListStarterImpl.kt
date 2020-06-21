package com.rygital.feature_currency_list_impl

import androidx.fragment.app.Fragment
import com.rygital.feature_currency_list_api.CurrencyListStarter
import com.rygital.feature_currency_list_impl.presentation.CurrencyListFragment
import javax.inject.Inject

internal class CurrencyListStarterImpl @Inject constructor() : CurrencyListStarter {

    override fun createFragment(): Fragment = CurrencyListFragment()
}
