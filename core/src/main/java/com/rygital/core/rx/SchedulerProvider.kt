package com.rygital.core.rx

import io.reactivex.rxjava3.core.Scheduler

interface SchedulerProvider {
    fun io(): Scheduler
    fun computation(): Scheduler
    fun ui(): Scheduler
}
