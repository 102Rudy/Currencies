package com.rygital.feature_currency_list_impl.di

import com.rygital.core.scope.FeatureScope
import com.rygital.feature_currency_list_api.CurrencyListApi
import com.rygital.feature_currency_list_api.CurrencyListStarter
import com.rygital.feature_currency_list_impl.CurrencyListStarterImpl
import dagger.Binds
import dagger.Component
import dagger.Module

@FeatureScope
@Component(
    modules = [
        CurrencyListBindsModule::class
    ]
)
interface CurrencyListComponent : CurrencyListApi {

    @Component.Factory
    interface Factory {
        fun create(): CurrencyListComponent
    }
}

@Module
internal interface CurrencyListBindsModule {

    @Binds
    @FeatureScope
    fun bindCurrencyListStarter(impl: CurrencyListStarterImpl): CurrencyListStarter
}
