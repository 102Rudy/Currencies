package com.rygital.core

import android.content.Context
import com.rygital.core.rx.SchedulerProvider
import com.rygital.core.rx.SchedulerProviderImpl
import com.rygital.core.scope.ApplicationScope
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ApplicationContext

@ApplicationScope
@Component(
    modules = [
        CoreBindsModule::class
    ]
)
interface CoreComponent : CoreApi {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance @ApplicationContext context: Context): CoreComponent
    }
}

@Module
internal interface CoreBindsModule {

    @Binds
    @ApplicationScope
    fun bindSchedulerProvider(impl: SchedulerProviderImpl): SchedulerProvider

    @Binds
    @ApplicationScope
    fun bindResourceProvider(impl: ResourceProviderImpl): ResourceProvider
}
