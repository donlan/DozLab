package com.example.base.ui

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.base.extensions.lazyFast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

abstract class BaseFragment<T : ViewModel>(
    @LayoutRes layoutResId: Int,
    private val vm: Class<T>,
    private val activityModel: Boolean = false
) :
    Fragment(layoutResId) {

    protected val viewModel by lazyFast {
        ViewModelProvider(if (activityModel) this.requireActivity() else this)[vm]
    }


    protected fun launch(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ) {
        viewModel.viewModelScope.launch(context, start, block)
    }
}