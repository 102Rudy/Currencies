package com.rygital.core_ui

import androidx.annotation.CallSuper
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

abstract class BasePresenterImpl<T : MvpView> : BasePresenter<T> {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    protected var view: T? = null
        private set

    @CallSuper
    override fun attachView(view: T) {
        this.view = view
    }

    @CallSuper
    override fun detachView() {
        view = null
        compositeDisposable.clear()
    }

    @CallSuper
    override fun destroy() {
        compositeDisposable.dispose()
    }

    protected fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }
}
