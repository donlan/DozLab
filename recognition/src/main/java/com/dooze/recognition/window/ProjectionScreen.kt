package com.dooze.recognition.window

import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import androidx.fragment.app.Fragment

class ProjectionScreen() {


    private var mediaProjectionManager: MediaProjectionManager? = null
    private var mediaProjection: MediaProjection? = null

    fun init(fragment: Fragment) {
        val mpm = fragment.requireContext()
            .getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        mediaProjectionManager = mpm
        fragment.startActivityForResult(mpm.createScreenCaptureIntent(), reqCode)
    }


    companion object {
        private const val reqCode = 2022
        fun isProjectionReq(reqCode: Int): Boolean = this.reqCode == reqCode
    }
}