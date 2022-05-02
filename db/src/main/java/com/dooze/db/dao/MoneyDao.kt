package com.dooze.db.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.dooze.db.entries.Bill
import com.dooze.db.entries.Tag
import kotlinx.coroutines.flow.Flow

@Dao
interface MoneyDao {

    @Query("select * from Bill")
    fun queryAllBill(): List<Bill>

    @Query("select * from Bill")
    fun queryAllBillFlow(): Flow<List<Bill>>

    @Query("select * from Bill")
    fun queryAllBillWithPaging(): PagingSource<Int, Bill>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addBill(bill: Bill)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAllBills(bills: List<Bill>)

    @Delete()
    fun deleteBill(bill: Bill)

    @Insert
    fun addTag(tag: Tag)

    @Delete
    fun deleteTag(tag: Tag)

    @Query("select * from Tag")
    fun queryAllTags(): Flow<List<Tag>>
}
