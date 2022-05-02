package com.dooze.db.entries

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Tag (
    @PrimaryKey val name: String = "",
    var parentTag: String? = null
)