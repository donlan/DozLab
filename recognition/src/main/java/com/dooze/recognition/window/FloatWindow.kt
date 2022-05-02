package com.dooze.recognition.window

import android.content.Context
import android.graphics.PixelFormat
import android.hardware.display.VirtualDisplay
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.dooze.recognition.R
import com.example.base.extensions.dp
import com.example.base.extensions.roundedCorner

class FloatWindow(
    private val context: Context,
    lifecycleOwner: LifecycleOwner,
    val startAction: (isStart: Boolean) -> Unit
) :
    View.OnClickListener {

    private var windowManager: WindowManager? = null
    private var rootView: View? = null
    private var isStarted = false

    init {
        lifecycleOwner.lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                when (event) {
                    Lifecycle.Event.ON_CREATE -> {
                        attachWindow()
                    }
                    Lifecycle.Event.ON_DESTROY -> {
                        detachWindow()
                    }
                }
            }
        })
    }

    private fun attachWindow() {
        var wm = windowManager
        if (wm != null) return
        wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager = wm
        val view = rootView ?: kotlin.run {
            val view =
                LayoutInflater.from(context).inflate(R.layout.view_float_recognition, null, false)
            view.roundedCorner()
            rootView = view
            view.alpha = 0.3f
            view.findViewById<View>(R.id.btnCapture).setOnClickListener(this)
            view
        }
        val layoutParams = WindowManager.LayoutParams().apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    layoutInDisplayCutoutMode =
                        WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
                }
            } else {
                type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
            }
            gravity = Gravity.START

            flags =
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            width = WindowManager.LayoutParams.WRAP_CONTENT
            height = WindowManager.LayoutParams.WRAP_CONTENT
            x = context.resources.displayMetrics.widthPixels -
                    (view.measuredWidth / 2).coerceAtLeast(24.dp().toInt())
            format = PixelFormat.TRANSPARENT
        }
        view.setOnTouchListener(FloatViewTouchListener(wm, layoutParams))
        wm.addView(view, layoutParams)

    }

    private fun detachWindow() {
        windowManager?.removeView(requireNotNull(rootView))
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnCapture -> {
                v.isEnabled = false
                startAction.invoke(!isStarted)
                isStarted = !isStarted
            }
        }
    }


}