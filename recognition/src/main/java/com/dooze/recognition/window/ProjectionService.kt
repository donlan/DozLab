package com.dooze.recognition.window

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageFormat
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.Image
import android.media.ImageReader
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.*


class ProjectionService : Service() {

    private val handlerThread: HandlerThread = HandlerThread("Projection")
    private val handler: Handler

    private var mediaProjection: MediaProjection? = null
    private var virtualDisplay: VirtualDisplay? = null
    private var imageReader: ImageReader? = null


    init {
        handlerThread.start()
        handler = Handler(handlerThread.looper)
    }

    private val projectionCallback by lazy {
        object : MediaProjection.Callback() {
            override fun onStop() {
                super.onStop()
                handler.post {
                    stop()
                }
            }
        }
    }

    private fun stop() {
        mediaProjection?.unregisterCallback(projectionCallback)
        virtualDisplay?.release()
        imageReader?.close()
        mediaProjection = null
        virtualDisplay = null
        imageReader = null
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.run {
            val resultCode = getIntExtra("resultCode", -1)
            val data = getParcelableExtra<Intent>("data") ?: return@run
            bindNotification()
            val mpm = getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
            val mediaProjection = mpm.getMediaProjection(resultCode, data)
            this@ProjectionService.mediaProjection = mediaProjection
            val width = resources.displayMetrics.widthPixels
            val height = resources.displayMetrics.heightPixels

            val imageReader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 2)
            this@ProjectionService.imageReader = imageReader
            virtualDisplay = mediaProjection.createVirtualDisplay(
                "projection",
                width,
                height,
                resources.displayMetrics.densityDpi,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR or DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC,
                imageReader.surface,
                null,
                handler
            )
            imageReader.setOnImageAvailableListener({ reader ->

                var image: Image? = null
                var fos: FileOutputStream? = null
                var bitmap: Bitmap? = null

                try {
                    image = reader.acquireLatestImage()
                    if (image != null) {
                        val planes = image.planes
                        val buffer = planes[0].buffer
                        val pixelStride = planes[0].pixelStride
                        val rowStride = planes[0].rowStride
                        val rowPadding = rowStride - pixelStride * width
                        bitmap = Bitmap.createBitmap(
                            width + rowPadding / pixelStride,
                            height, Bitmap.Config.ARGB_8888
                        )
                        bitmap.copyPixelsFromBuffer(buffer)
                        fos = FileOutputStream(
                            File(
                                filesDir,
                                UUID.randomUUID().toString()
                            ).absoluteFile
                        )
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    if (fos != null) {
                        try {
                            fos.close()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                    bitmap?.recycle()
                    if (image != null) {
                        image.close()
                    }
                }
                mediaProjection.registerCallback(projectionCallback, handler)
                mediaProjection.stop()
            }, handler)


        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun bindNotification() {
        val notification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(this, "projectionService")
        } else {
            Notification.Builder(this)
        }.setContentTitle("Projection Recognition")
            .setContentText("Projecting...")
            .setSmallIcon(com.example.base.R.mipmap.ic_launcher)
            .build()
        //前台服务notification适配
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(
                "projectionService",
                "recognition_projection",
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
        }
        startForeground(2022, notification)
    }

    companion object {
        fun start(context: Context, resultCode: Int, data: Intent) {
            val intent = Intent(context, ProjectionService::class.java)
            intent.putExtra("resultCode", resultCode)
            intent.putExtra("data", data)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

    }
}