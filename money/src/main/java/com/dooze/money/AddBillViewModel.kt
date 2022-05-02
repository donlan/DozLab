package com.dooze.money

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dooze.db.RoomRepository
import com.dooze.db.entries.Tag
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class AddBillViewModel : ViewModel() {

    fun observeTags(context: Context, action: (tags: List<Tag>) -> Unit) {
        viewModelScope.launch {
            RoomRepository.get(context)
                .dao()
                .queryAllTags()
                .distinctUntilChanged()
                .collect {
                    action.invoke(it)
                }
        }

    }
}