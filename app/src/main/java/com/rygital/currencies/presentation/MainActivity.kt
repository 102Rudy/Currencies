package com.rygital.currencies.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rygital.core_utils.componentFactory
import com.rygital.currencies.R
import com.rygital.feature_currency_list_api.CurrencyListApi

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.CurrencyAppTheme)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            val fragment = componentFactory.get(CurrencyListApi::class)
                .currencyListStarter()
                .createFragment()

            supportFragmentManager
                .beginTransaction()
                .replace(R.id.mainContainer, fragment)
                .disallowAddToBackStack()
                .commit()
        }
    }
}
