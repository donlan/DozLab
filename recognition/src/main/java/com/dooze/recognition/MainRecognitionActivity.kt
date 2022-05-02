package com.dooze.recognition

import android.content.Intent
import android.os.Bundle
import com.example.base.ui.BaseActivity

class MainRecognitionActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_recognition)
        detectIntentFromSend(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        detectIntentFromSend(intent)
    }

    private fun detectIntentFromSend(intent: Intent?) {
        intent?.run {
            if (action == Intent.ACTION_SEND && type?.startsWith("image") == true) {
                val item = clipData?.getItemAt(0) ?: return@run
                viewModel(ImportImageRecognitionViewModel::class.java).recognitionImage(
                    applicationContext,
                    item.uri
                )
            }
        }
    }
}