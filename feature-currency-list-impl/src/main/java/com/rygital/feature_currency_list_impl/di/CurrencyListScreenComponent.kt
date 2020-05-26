package com.rygital.feature_currency_list_impl.di

import com.rygital.core.BaseInjector
import com.rygital.core.componentFactory
import com.rygital.core.rx.SchedulerProvider
import com.rygital.core.rx.SchedulerProviderImpl
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
        NetworkApi::class
    ]
)
internal interface CurrencyListScreenComponent : BaseInjector<CurrencyListFragment> {

    @Component.Factory
    interface Factory {
        fun create(networkApi: NetworkApi): CurrencyListScreenComponent
    }

    companion object {
        private var component: CurrencyListScreenComponent? = null

        fun getAndInject(fragment: CurrencyListFragment) {
            if (component == null) {
                component = DaggerCurrencyListScreenComponent.factory()
                    .create(fragment.componentFactory.get(NetworkApi::class))
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
    @ScreenScope
    fun bindSchedulerProvider(impl: SchedulerProviderImpl): SchedulerProvider

    @Binds
    fun bindCurrencyRepository(impl: CurrencyRepositoryImpl): CurrencyRepository

    @Binds
    fun bindCurrencyInteractor(impl: CurrencyInteractorImpl): CurrencyInteractor

    @Binds
    @ScreenScope
    fun bindCurrencyListPresenter(impl: CurrencyListPresenterImpl): CurrencyListPresenter
}
