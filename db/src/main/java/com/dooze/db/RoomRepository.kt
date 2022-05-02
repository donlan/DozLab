package com.dooze.db

import android.content.Context
import androidx.room.*
import com.dooze.db.converter.ArrayStringConverter
import com.dooze.db.dao.MoneyDao
import com.dooze.db.dao.RecognitionDao
import com.dooze.db.entries.Bill
import com.dooze.db.entries.RecognitionRecord
import com.dooze.db.entries.Tag
import com.dooze.db.migration.MIGRATION_1_2

@Database(
    entities = [Bill::class, Tag::class, RecognitionRecord::class],
    version = 1,
    exportSchema = true,
    //autoMigrations = [AutoMigration(from = 1, to = 2)]
)

@TypeConverters(ArrayStringConverter::class)
abstract class RoomRepository : RoomDatabase() {

    abstract fun dao(): MoneyDao

    abstract fun recognitionDao(): RecognitionDao

    companion object {
        private lateinit var DB: RoomRepository
        fun get(context: Context): RoomRepository {
            if (!::DB.isInitialized) {
                synchronized(this) {
                    if (!::DB.isInitialized) {
                        DB = Room.databaseBuilder(
                            context.applicationContext,
                            RoomRepository::class.java,
                            "db_money"
                        )
                            .build()
                    }
                }
            }
            return DB
        }
    }

}