package com.dooze.db.converter

import androidx.room.TypeConverter

class ArrayStringConverter {
    @TypeConverter
    fun arrayToString(list: Array<String>): String {
        return list.joinToString("_")
    }

    @TypeConverter
    fun stringToArray(tagString: String): Array<String> {
        return tagString.split("_").toTypedArray()
    }

}