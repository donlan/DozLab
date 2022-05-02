package com.dooze.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dooze.db.entries.RecognitionRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface RecognitionDao {


    @Query("select * from recognition_record")
    fun queryAllRecords(): Flow<List<RecognitionRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addRecords(records: List<RecognitionRecord>)



}