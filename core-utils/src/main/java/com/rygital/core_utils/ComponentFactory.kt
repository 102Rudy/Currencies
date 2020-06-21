package com.rygital.core_utils

import kotlin.reflect.KClass

interface ComponentFactory {
    fun <T : Any> get(clazz: KClass<T>): T
}
