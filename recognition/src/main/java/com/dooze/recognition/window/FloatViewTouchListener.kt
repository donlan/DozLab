package com.dooze.recognition.window

import android.view.MotionEvent
import android.view.View
import android.view.WindowManager

class FloatViewTouchListener(
    private val windowManager: WindowManager,
    private val layoutParams: WindowManager.LayoutParams
) : View.OnTouchListener {

    private var curX = 0f
    private var curY = 0f

    override fun onTouch(v: View, event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                curX = event.rawX
                curY = event.rawY
                v.alpha = 1f
            }
            MotionEvent.ACTION_MOVE -> {
                val dx = event.rawX - curX
                val dy = event.rawY - curY
                curY = event.rawY
                curX = event.rawX
                layoutParams.x = (layoutParams.x + dx).toInt()
                layoutParams.y = (layoutParams.y + dy).toInt()
                windowManager.updateViewLayout(v, layoutParams)
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                val appContext = v.context.applicationContext
                v.alpha = 0.3f
                if (event.rawX < appContext.resources.displayMetrics.widthPixels / 2) {
                    layoutParams.x = -v.width / 2
                } else {
                    layoutParams.x = appContext.resources.displayMetrics.widthPixels - v.width / 2
                }
                windowManager.updateViewLayout(v, layoutParams)

            }
        }
        return false
    }
}