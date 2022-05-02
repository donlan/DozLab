package com.dooze.db.entries

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recognition_record")
data class RecognitionRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val text: String,
    @ColumnInfo(name = "created_at")
    val createdAt: Long
)