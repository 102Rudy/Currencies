package com.rygital.currencies

import android.app.Application
import com.rygital.core_utils.ComponentFactory
import com.rygital.core_utils.ComponentFactoryHolder
import com.rygital.core_utils.LoggingTree
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
            .create(applicationContext)
            .inject(this)
    }
}
