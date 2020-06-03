package com.rygital.feature_currency_list_impl.di

import com.rygital.core.BaseInjector
import com.rygital.core.CoreApi
import com.rygital.core.componentFactory
import com.rygital.core.scope.ScreenScope
import com.rygital.core_network_api.NetworkApi
import com.rygital.feature_currency_list_impl.data.CurrencyRepositoryImpl
import com.rygital.feature_currency_list_impl.data.CurrencyService
import com.rygital.feature_currency_list_impl.domain.CurrencyInteractor
import com.rygital.feature_currency_list_impl.domain.CurrencyInteractorImpl
import com.rygital.feature_currency_list_impl.domain.CurrencyRepository
import com.rygital.feature_currency_list_impl.presentation.CurrencyListFragment
import com.rygital.feature_currency_list_impl.presentation.CurrencyListPresenter
import com.rygital.feature_currency_list_impl.presentation.CurrencyListPresenterImpl
import com.rygital.feature_currency_list_impl.presentation.CurrencyViewDataConverter
import com.rygital.feature_currency_list_impl.presentation.CurrencyViewDataConverterImpl
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@ScreenScope
@Component(
    modules = [
        CurrencyListScreenProvidesModule::class,
        CurrencyListScreenBindsModule::class
    ],
    dependencies = [
        CoreApi::class,
        NetworkApi::class
    ]
)
internal interface CurrencyListScreenComponent : BaseInjector<CurrencyListFragment> {

    @Component.Factory
    interface Factory {
        fun create(
            coreApi: CoreApi,
            networkApi: NetworkApi
        ): CurrencyListScreenComponent
    }

    companion object {
        private var component: CurrencyListScreenComponent? = null

        fun getAndInject(fragment: CurrencyListFragment) {
            if (component == null) {
                val componentFactory = fragment.componentFactory

                component = DaggerCurrencyListScreenComponent.factory()
                    .create(
                        componentFactory.get(CoreApi::class),
                        componentFactory.get(NetworkApi::class)
                    )
            }
            component!!.inject(fragment)
        }

        fun clearComponent() {
            component = null
        }
    }
}

@Module
internal class CurrencyListScreenProvidesModule {

    @Provides
    fun provideCurrenciesService(retrofit: Retrofit): CurrencyService =
        retrofit.create(CurrencyService::class.java)
}

@Module
internal interface CurrencyListScreenBindsModule {

    @Binds
    fun bindCurrencyRepository(impl: CurrencyRepositoryImpl): CurrencyRepository

    @Binds
    fun bindCurrencyInteractor(impl: CurrencyInteractorImpl): CurrencyInteractor

    @Binds
    fun bindCurrencyViewDataConverter(
        impl: CurrencyViewDataConverterImpl
    ): CurrencyViewDataConverter

    @Binds
    @ScreenScope
    fun bindCurrencyListPresenter(impl: CurrencyListPresenterImpl): CurrencyListPresenter
}
