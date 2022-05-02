package com.example.base.extensions

fun <T> lazyFast(initializer: () -> T): Lazy<T> =
    lazy(LazyThreadSafetyMode.NONE, initializer)