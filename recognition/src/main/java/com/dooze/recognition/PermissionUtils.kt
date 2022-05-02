package com.dooze.recognition

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import com.example.base.log.DLog

object PermissionUtils {
    /**
     * 判断悬浮窗权限权限
     */
    fun isOverlayEnable(context: Context): Boolean {
        var result = true
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                val clazz: Class<*> = Settings::class.java
                val canDrawOverlays =
                    clazz.getDeclaredMethod("canDrawOverlays", Context::class.java)
                result = canDrawOverlays.invoke(null, context) as Boolean
            } catch (e: Exception) {
                DLog.e("ServiceUtils", e)
            }
        }
        return result
    }

    @SuppressLint("InlinedApi")
    fun gotoOverlaySetting(context: Activity, reqCode: Int = 1001) {
        context.startActivityForResult(
            Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION).apply {
                data = Uri.parse("package:${context.packageName}")
            },
            reqCode
        )
    }
}

