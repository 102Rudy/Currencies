package com.rygital.core_utils

import timber.log.Timber

class LoggingTree : Timber.DebugTree() {

    override fun createStackElementTag(element: StackTraceElement): String? =
        findCaller(Thread.currentThread()).let {
            "Currencies (${it.fileName}:${it.lineNumber})"
        }

    private fun findCaller(thread: Thread): StackTraceElement {
        val loggerClassName = Timber::class.java.name
        var lastCallerIsLoggerClass = false
        for (caller in thread.stackTrace) {
            val isLoggerClass = caller.className.startsWith(loggerClassName, ignoreCase = true)
            if (lastCallerIsLoggerClass && !isLoggerClass) {
                return caller
            }
            lastCallerIsLoggerClass = isLoggerClass
        }

        // If we did not find logger class in stack (something reasons)
        // then take fourth element from stack (probably it is correct place where logger was called)
        return thread.stackTrace[4]
    }
}
