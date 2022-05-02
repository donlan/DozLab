package com.example.base.log

import android.util.Log
import com.example.base.BuildConfig

object DLog : DLogTree {

    private val logTree = ArrayList<DLogTree>(0)

    override fun v(tag: String, content: String) {
        logTree.forEach { it.d(tag, content) }
    }

    fun v(tag: String, content: () -> String) {
        logTree.forEach { it.d(tag, content.invoke()) }
    }

    override fun d(tag: String, content: String) {
        logTree.forEach { it.d(tag, content) }
    }

    fun d(tag: String, content: () -> String) {
        logTree.forEach { it.d(tag, content.invoke()) }
    }

    override fun i(tag: String, content: String) {
        logTree.forEach { it.i(tag, content) }
    }

    fun i(tag: String, content: () -> String) {
        logTree.forEach { it.i(tag, content.invoke()) }
    }

    override fun w(tag: String, content: String) {
        logTree.forEach { it.w(tag, content) }
    }

    fun w(tag: String, content: () -> String) {
        logTree.forEach { it.w(tag, content.invoke()) }
    }

    override fun e(tag: String, t: Throwable, content: String) {
        logTree.forEach { it.e(tag, t, content) }
    }

    fun e(tag: String, t: Throwable, content: () -> String) {
        logTree.forEach { it.e(tag, t, content.invoke()) }
    }

    override fun e(tag: String, t: Throwable) {
        logTree.forEach { it.e(tag, t) }
    }


    fun addTree(tree: DLogTree) {
        logTree.add(tree)
    }
}

interface DLogTree {
    fun v(tag: String, content: String)

    fun d(tag: String, content: String)

    fun i(tag: String, content: String)

    fun w(tag: String, content: String)

    fun e(tag: String, t: Throwable, content: String)

    fun e(tag: String, t: Throwable)
}

class DebugLogTree : DLogTree {
    override fun v(tag: String, content: String) {
        if (!BuildConfig.DEBUG) return
        Log.v(tag, content)
    }

    override fun d(tag: String, content: String) {
        if (!BuildConfig.DEBUG) return
        Log.d(tag, content)
    }

    override fun i(tag: String, content: String) {
        if (!BuildConfig.DEBUG) return
        Log.i(tag, content)
    }

    override fun w(tag: String, content: String) {
        if (!BuildConfig.DEBUG) return
        Log.w(tag, content)
    }

    override fun e(tag: String, t: Throwable, content: String) {
        if (!BuildConfig.DEBUG) return
        Log.e(tag, content, t)
    }

    override fun e(tag: String, t: Throwable) {
        if (!BuildConfig.DEBUG) return
        Log.e(tag, Log.getStackTraceString(t))
    }

}