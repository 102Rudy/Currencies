package com.rygital.core

import kotlin.reflect.KClass

interface ComponentFactory {
    fun <T : Any> get(clazz: KClass<T>): T
}
