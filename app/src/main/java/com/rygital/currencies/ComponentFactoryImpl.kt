package com.rygital.currencies

import com.rygital.core.ComponentFactory
import com.rygital.core_network_api.NetworkApi
import com.rygital.core_network_impl.DaggerNetworkComponent
import javax.inject.Inject
import kotlin.reflect.KClass

typealias ComponentHolder<T> = () -> T

class ComponentFactoryImpl @Inject constructor() : ComponentFactory {

    private val componentMap: Map<Any, ComponentHolder<Any>> =
        mapOf(
            NetworkApi::class to { getNetworkApi() }
        )

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> get(clazz: KClass<T>): T =
        componentMap[clazz]?.invoke() as? T
            ?: throw IllegalArgumentException("Wrong api class $clazz for component map")

    private fun getNetworkApi(): NetworkApi =
        DaggerNetworkComponent.factory().create("https://hiring.revolut.codes/api/android")
}
