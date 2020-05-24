package com.rygital.core_ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import javax.inject.Inject

abstract class BaseFragment<P : BasePresenter<in V>, V : MvpView> : Fragment(), MvpView {

    @Inject
    protected lateinit var presenter: P

    protected open fun performInject() = Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        performInject()
        super.onCreate(savedInstanceState)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onStart() {
        super.onStart()
        presenter.attachView(this as V)
    }

    override fun onStop() {
        presenter.detachView()
        super.onStop()
    }

    override fun onDestroy() {
        if (requireActivity().isFinishing) {
            presenter.destroy()
        }
        super.onDestroy()
    }
}
