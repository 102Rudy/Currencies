package com.rygital.core_ui

interface BasePresenter<V : MvpView> {
    fun attachView(view: V)
    fun detachView()
    fun destroy()
}
