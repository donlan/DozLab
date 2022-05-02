package com.dooze.db.entries

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Bill(
    @PrimaryKey(autoGenerate = true)
    val uid: Long = 0L,
    var amount: Float = 0f,
    @ColumnInfo(name = "currency_type") var currencyType: Int = Currency.CN.type,
    var descriptor: String? = null,
    var tags: Array<String>? = null,
    @ColumnInfo(name = "created_at") var createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "date") var date: Long = createdAt,
)


enum class Currency(val type: Int) {
    CN(0)
}

