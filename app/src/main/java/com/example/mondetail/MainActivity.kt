package com.example.mondetail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.HandlerThread
import android.view.LayoutInflater
import com.dooze.money.MoneyMainActivity
import com.dooze.recognition.MainRecognitionActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this, MainRecognitionActivity::class.java))
        finish()
    }
}