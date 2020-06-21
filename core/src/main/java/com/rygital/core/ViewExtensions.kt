package com.rygital.core

import android.view.View

fun View.setVisibleOrGone(isVisible: Boolean) {
    visibility =
        if (isVisible) {
            View.VISIBLE
        } else {
            View.GONE
        }
}
