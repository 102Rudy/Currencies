package com.rygital.core_utils

import android.view.View
import android.view.ViewGroup

fun View.setVisibleOrGone(isVisible: Boolean) {
    visibility =
        if (isVisible) {
            View.VISIBLE
        } else {
            View.GONE
        }
}

fun ViewGroup.findViewRecursive(predicate: (View) -> Boolean): View? {
    children.forEach { view ->
        if (predicate(view)) {
            return view
        }

        if (view is ViewGroup) {
            view.findViewRecursive(predicate)?.let {
                return it
            }
        }
    }

    return null
}

private val ViewGroup.children: Sequence<View>
    get() = object : Sequence<View> {
        override fun iterator(): Iterator<View> = object : Iterator<View> {
            private var index = 0
            override fun hasNext(): Boolean = index < childCount
            override fun next(): View = getChildAt(index++)
        }
    }
