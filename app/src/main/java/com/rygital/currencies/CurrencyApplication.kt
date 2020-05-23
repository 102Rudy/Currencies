package com.rygital.currencies

import android.app.Application
import com.rygital.core.ComponentFactory
import com.rygital.core.ComponentFactoryHolder
import com.rygital.core.LoggingTree
import com.rygital.currencies.di.DaggerApplicationComponent
import timber.log.Timber
import javax.inject.Inject

class CurrencyApplication : Application(), ComponentFactoryHolder {

    @Inject
    override lateinit var componentFactory: ComponentFactory

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(LoggingTree())
        }

        DaggerApplicationComponent.factory()
            .create()
            .inject(this)
    }
}
