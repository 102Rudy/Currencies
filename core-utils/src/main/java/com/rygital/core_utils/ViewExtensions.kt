package com.rygital.core_utils

import android.view.View

fun View.setVisibleOrGone(isVisible: Boolean) {
    visibility =
        if (isVisible) {
            View.VISIBLE
        } else {
            View.GONE
        }
}
