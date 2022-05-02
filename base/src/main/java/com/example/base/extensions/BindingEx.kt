package com.example.base.extensions

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

inline fun <F : Fragment, VB : ViewBinding> viewBinding(crossinline binding: (View) -> VB) =
    ViewBindingProperty { fragment: F ->
        binding(fragment.requireView())
    }

class ViewBindingProperty<in F : Fragment, out VB : ViewBinding>(private val bindingFun: (F) -> VB) :
    ReadOnlyProperty<F, VB> {

    private var binding: VB? = null
    override fun getValue(thisRef: F, property: KProperty<*>): VB {

        if (thisRef.lifecycle.currentState == Lifecycle.State.DESTROYED)
            throw RuntimeException("can't bind ViewBinding after onDestroy")

        return binding ?: bindingFun(thisRef).also {
            binding = it
        }
    }

}