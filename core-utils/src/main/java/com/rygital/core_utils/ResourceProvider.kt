package com.rygital.core_utils

import android.content.Context
import androidx.annotation.StringRes
import javax.inject.Inject

interface ResourceProvider {
    fun getString(@StringRes stringRes: Int): String
}

internal class ResourceProviderImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ResourceProvider {

    override fun getString(@StringRes stringRes: Int): String =
        context.getString(stringRes)
}
