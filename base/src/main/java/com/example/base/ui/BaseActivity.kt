package com.example.base.ui

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

abstract class BaseActivity : AppCompatActivity() {

    fun <T : ViewModel> viewModel(clazz: Class<T>): T {
        return ViewModelProvider(this)[clazz]
    }
}