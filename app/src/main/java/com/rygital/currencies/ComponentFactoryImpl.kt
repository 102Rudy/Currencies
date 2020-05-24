package com.rygital.currencies

import com.rygital.core.ComponentFactory
import com.rygital.core_network_api.NetworkApi
import com.rygital.core_network_impl.DaggerNetworkComponent
import com.rygital.feature_currency_list_api.CurrencyListApi
import com.rygital.feature_currency_list_impl.di.DaggerCurrencyListComponent
import javax.inject.Inject
import kotlin.reflect.KClass

typealias ComponentHolder<T> = () -> T

class ComponentFactoryImpl @Inject constructor() : ComponentFactory {

    @Volatile
    private var networkApi: NetworkApi? = null

    private val componentMap: Map<Any, ComponentHolder<Any>> =
        mapOf(
            NetworkApi::class to { getNetworkApi() },
            CurrencyListApi::class to { getCurrencyListApi() }
        )

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> get(clazz: KClass<T>): T =
        componentMap[clazz]?.invoke() as? T
            ?: throw IllegalArgumentException("Wrong api class $clazz for component map")

    private fun getNetworkApi(): NetworkApi =
        networkApi ?: synchronized(this) {
            networkApi ?: DaggerNetworkComponent.factory().create().also { networkApi = it }
        }

    private fun getCurrencyListApi(): CurrencyListApi =
        DaggerCurrencyListComponent.factory().create()
}
