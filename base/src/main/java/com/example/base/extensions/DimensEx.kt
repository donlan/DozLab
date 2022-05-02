package com.example.base.extensions

import android.content.Context

private var applicationContext: Context? = null

fun setupAppContext(appContext: Context) {
    applicationContext = appContext.applicationContext
}

fun Int.dp(context: Context? = applicationContext): Float {
    val c = context ?: requireNotNull(applicationContext)
    return c.resources.displayMetrics.density * this
}