package com.example.mondetail

import android.app.Application
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.example.base.extensions.setupAppContext
import com.example.base.log.DLog
import com.example.base.log.DebugLogTree

class App : Application(), LifecycleOwner {

    private val lifecycleRegistry = LifecycleRegistry(this)

    override fun onCreate() {
        DLog.addTree(DebugLogTree())
        setupAppContext(this)
        super.onCreate()
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
    }

    override fun getLifecycle(): Lifecycle = lifecycleRegistry

    companion object
}