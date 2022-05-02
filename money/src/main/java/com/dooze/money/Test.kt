package com.dooze.money

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

class Test:ViewModel() {

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val a = 4
                launch {
                    5
                }
                6
            }
            val aS = async {
                delay(100L)
                7
            }
        }
    }
}