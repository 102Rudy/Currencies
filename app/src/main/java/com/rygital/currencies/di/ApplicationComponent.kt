package com.rygital.currencies.di

import android.content.Context
import com.rygital.core_utils.ApplicationContext
import com.rygital.core_utils.BaseInjector
import com.rygital.core_utils.ComponentFactory
import com.rygital.core_utils.scope.ApplicationScope
import com.rygital.currencies.ComponentFactoryImpl
import com.rygital.currencies.CurrencyApplication
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module

@ApplicationScope
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
        fun create(@BindsInstance @ApplicationContext context: Context): ApplicationComponent
    }
}

@Module
internal interface ApplicationBindsModule {
    @Binds
    @ApplicationScope
    fun bindComponentFactory(impl: ComponentFactoryImpl): ComponentFactory
}
