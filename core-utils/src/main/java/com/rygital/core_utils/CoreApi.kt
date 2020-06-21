package com.rygital.core_utils

import com.rygital.core_utils.rx.SchedulerProvider

interface CoreApi {
    fun schedulerProvider(): SchedulerProvider
    fun resourceProvider(): ResourceProvider
}
