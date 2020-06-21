package com.rygital.core_ui

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import javax.inject.Inject

abstract class BaseFragment<P : BasePresenter<in V>, V : MvpView> : Fragment(), MvpView {

    @Inject
    protected lateinit var presenter: P

    protected open fun performInject() = Unit

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        performInject()
        super.onCreate(savedInstanceState)
    }

    @Suppress("UNCHECKED_CAST")
    @CallSuper
    override fun onStart() {
        super.onStart()
        presenter.attachView(this as V)
    }

    @CallSuper
    override fun onStop() {
        presenter.detachView()
        super.onStop()
    }

    @CallSuper
    override fun onDestroy() {
        if (requireActivity().isFinishing) {
            presenter.destroy()
        }
        super.onDestroy()
    }
}
