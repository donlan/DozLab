package com.example.base.extensions

import android.content.Context
import android.graphics.Outline
import android.view.View
import android.view.ViewOutlineProvider
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

fun View.roundedCorner(radius: Float = -1f) {
    outlineProvider = object : ViewOutlineProvider() {
        override fun getOutline(view: View?, outline: Outline?) {
            outline ?: return
            view ?: return
            val r = if (radius < 0) view.height / 2f else radius
            outline.setRoundRect(0, 0, view.width, view.height, r)
        }
    }
    clipToOutline = true
}

fun View.topRoundedCorner(radius: Float = -1f) {
    outlineProvider = object : ViewOutlineProvider() {
        override fun getOutline(view: View?, outline: Outline?) {
            outline ?: return
            view ?: return
            val r = if (radius < 0) view.height / 2f else radius
            outline.setRoundRect(0, 0, view.width, view.height + r.toInt(), r)
        }
    }
    clipToOutline = true
}

fun View.showSnack(@StringRes resId: Int, duration: Int = BaseTransientBottomBar.LENGTH_SHORT) {
    Snackbar.make(this, resId, duration).show()
}

fun View.showSnack(text: CharSequence, duration: Int = BaseTransientBottomBar.LENGTH_SHORT) {
    Snackbar.make(this, text, duration).show()
}


fun Context.getColorCompat(@ColorRes id: Int) = ContextCompat.getColor(this, id)