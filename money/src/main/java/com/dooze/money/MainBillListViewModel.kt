package com.dooze.money

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dooze.db.RoomRepository
import com.example.base.utils.DateUtils
import com.dooze.db.entries.Bill
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStreamReader
import kotlin.concurrent.thread
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.startCoroutine

class MainBillListViewModel : ViewModel() {


    fun observerBills(context: Context, action: (bills: List<Bill>) -> Unit) {
        viewModelScope.launch(Dispatchers.Main) {
            RoomRepository.get(context)
                .dao()
                .queryAllBillFlow()
                .distinctUntilChanged()
                .collect {
                    action.invoke(it)
                }
        }.cancel()
        viewModelScope.cancel()


        suspend {

        }.startCoroutine(object :Continuation<Any>{
            override val context: CoroutineContext
                get() = EmptyCoroutineContext

            override fun resumeWith(result: Result<Any>) {
                TODO("Not yet implemented")
            }

        })
    }

    fun importQianJi(context: Context) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                context.assets.open("qianji.txt").use { inputStream ->
                    val list: ArrayList<Qianji> = GsonBuilder().create()
                        .fromJson<ArrayList<Qianji>?>(
                            InputStreamReader(inputStream),
                            object : TypeToken<ArrayList<Qianji>>() {}.type
                        )
                    RoomRepository.get(context).dao().addAllBills(list.map {
                        Bill(
                            amount = it.money.toFloat(),
                            descriptor = it.remark,
                            tags = arrayOf(it.category ?: "日常"),
                            // 2022-04-01 05:37
                            createdAt = DateUtils.stringToDate(it.date ?: "", "yyyy-MM-dd hh:mm").time
                        )
                    })

                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}