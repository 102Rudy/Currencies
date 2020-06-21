package com.rygital.currencies

import android.content.Context
import com.rygital.core_utils.ApplicationContext
import com.rygital.core_utils.ComponentFactory
import com.rygital.core_utils.CoreApi
import com.rygital.core_utils.DaggerCoreComponent
import com.rygital.core_network_api.NetworkApi
import com.rygital.core_network_impl.DaggerNetworkComponent
import com.rygital.feature_currency_list_api.CurrencyListApi
import com.rygital.feature_currency_list_impl.di.DaggerCurrencyListComponent
import javax.inject.Inject
import kotlin.reflect.KClass

typealias ComponentHolder<T> = () -> T

class ComponentFactoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ComponentFactory {

    @Volatile
    private var coreApi: CoreApi? = null

    @Volatile
    private var networkApi: NetworkApi? = null

    private val componentMap: Map<Any, ComponentHolder<Any>> =
        mapOf(
            CoreApi::class to { getCoreApi() },
            NetworkApi::class to { getNetworkApi() },
            CurrencyListApi::class to { getCurrencyListApi() }
        )

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> get(clazz: KClass<T>): T =
        componentMap[clazz]?.invoke() as? T
            ?: throw IllegalArgumentException("Wrong api class $clazz for component map")

    private fun getCoreApi(): CoreApi =
        coreApi ?: synchronized(this) {
            coreApi ?: DaggerCoreComponent.factory().create(context).also { coreApi = it }
        }

    private fun getNetworkApi(): NetworkApi =
        networkApi ?: synchronized(this) {
            networkApi ?: DaggerNetworkComponent.factory().create().also { networkApi = it }
        }

    private fun getCurrencyListApi(): CurrencyListApi =
        DaggerCurrencyListComponent.factory().create()
}
