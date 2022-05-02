package com.example.base.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    fun stringToDate(dateString: String, pattern: String, format: SimpleDateFormat? = null): Date {
        return (format ?: SimpleDateFormat(pattern, Locale.getDefault()))
            .parse(dateString) ?: Date()
    }
}