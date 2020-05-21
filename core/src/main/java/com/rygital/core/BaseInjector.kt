package com.rygital.core

interface BaseInjector<T> {
    fun inject(injected: T)
}
