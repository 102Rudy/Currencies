package com.rygital.core

import com.rygital.core.rx.SchedulerProvider

interface CoreApi {
    fun schedulerProvider(): SchedulerProvider
    fun resourceProvider(): ResourceProvider
}
