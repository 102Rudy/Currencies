package com.rygital.core

import android.app.Service
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

val FragmentActivity.componentFactory: ComponentFactory
    get() = (application as ComponentFactoryHolder).componentFactory

val Fragment.componentFactory: ComponentFactory
    get() = requireActivity().componentFactory

val Service.componentFactory: ComponentFactory
    get() = (application as ComponentFactoryHolder).componentFactory
