package com.rygital.feature_currency_list_impl.presentation.utils

import android.animation.Animator
import android.animation.AnimatorInflater
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ScrollView
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.rygital.core_utils.findViewRecursive
import com.rygital.feature_currency_list_impl.R

class AutoElevationAppBarLayout(context: Context, attrs: AttributeSet) :
    AppBarLayout(context, attrs) {

    private var currentScrollLayout: ViewGroup? = null

    private val viewTreeObserverOnScrollChangedListener: ViewTreeObserver.OnScrollChangedListener
        by lazy {
            ViewTreeObserver.OnScrollChangedListener {
                currentScrollLayout?.let { internalOnScrollAction(it) }
            }
        }

    private val nestedScrollViewOnScrollChangeListener: NestedScrollView.OnScrollChangeListener
        by lazy {
            NestedScrollView.OnScrollChangeListener { _, _, _, _, _ ->
                currentScrollLayout?.let { internalOnScrollAction(it) }
            }
        }

    private val recyclerViewOnScrollListener: RecyclerView.OnScrollListener by lazy {
        object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                currentScrollLayout?.let { internalOnScrollAction(it) }
            }
        }
    }

    private var prevTopState: Boolean? = null
    private var currentAnimator: Animator? = null
    private val addElevationAnimator: Animator =
        AnimatorInflater.loadAnimator(context, R.animator.appbar_elevation_up)
    private var removeElevationAnimator: Animator =
        AnimatorInflater.loadAnimator(context, R.animator.appbar_elevation_down)

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        initAppBar()
    }

    private fun initAppBar() {
        prevTopState = null

        removeElevationAnimator.run {
            setTarget(this@AutoElevationAppBarLayout)
            duration = 0
            start()
        }

        initScrollLayout(parent as? ViewGroup)
    }

    private fun initScrollLayout(parent: ViewGroup?) {
        currentScrollLayout?.let {
            removeScrollListener(it)
            currentScrollLayout = null
        }

        findScrollContainer(parent)?.let {
            addScrollListener(it)
            internalOnScrollAction(it)
            currentScrollLayout = it
        }
    }

    private fun removeScrollListener(scrollView: ViewGroup) {
        when (scrollView) {
            is ScrollView -> scrollView.viewTreeObserver.removeOnScrollChangedListener(
                viewTreeObserverOnScrollChangedListener
            )
            is RecyclerView -> scrollView.removeOnScrollListener(recyclerViewOnScrollListener)
        }
    }

    private fun addScrollListener(scrollView: ViewGroup) {
        when (scrollView) {
            is ScrollView -> scrollView.viewTreeObserver.addOnScrollChangedListener(
                viewTreeObserverOnScrollChangedListener
            )
            is NestedScrollView -> scrollView.setOnScrollChangeListener(
                nestedScrollViewOnScrollChangeListener
            )
            is RecyclerView -> scrollView.addOnScrollListener(recyclerViewOnScrollListener)
        }
    }

    private fun internalOnScrollAction(view: View) {
        val needTopState = view.canScrollVertically(-1)
        val newAnimator = if (needTopState) addElevationAnimator else removeElevationAnimator

        newAnimator.let {
            if (prevTopState != needTopState) {
                currentAnimator?.cancel()
                currentAnimator = it
                prevTopState = needTopState

                it.apply {
                    setTarget(this@AutoElevationAppBarLayout)
                    start()
                }
            }
        }
    }

    private fun findScrollContainer(viewGroup: ViewGroup?): ViewGroup? =
        viewGroup?.findViewRecursive {
            it is ScrollView || it is NestedScrollView || it is RecyclerView
        } as? ViewGroup
}
