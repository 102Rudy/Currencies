package com.rygital.currencies

import android.app.Application
import com.rygital.core.ComponentFactory
import com.rygital.core.ComponentFactoryHolder
import com.rygital.currencies.di.DaggerApplicationComponent
import javax.inject.Inject

class CurrencyApplication : Application(), ComponentFactoryHolder {

    @Inject
    override lateinit var componentFactory: ComponentFactory

    override fun onCreate() {
        super.onCreate()

        DaggerApplicationComponent.factory()
            .create()
            .inject(this)
    }
}
