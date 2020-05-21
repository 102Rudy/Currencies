package com.rygital.currencies.di

import com.rygital.core.BaseInjector
import com.rygital.core.ComponentFactory
import com.rygital.currencies.ComponentFactoryImpl
import com.rygital.currencies.CurrencyApplication
import dagger.Binds
import dagger.Component
import dagger.Module

@Component(
    modules = [
        ApplicationBindsModule::class
    ],
    dependencies = [
    ]
)
internal interface ApplicationComponent : BaseInjector<CurrencyApplication> {

    @Component.Factory
    interface Factory {
        fun create(): ApplicationComponent
    }
}

@Module
internal interface ApplicationBindsModule {
    @Binds
    fun bindComponentFactory(impl: ComponentFactoryImpl): ComponentFactory
}
