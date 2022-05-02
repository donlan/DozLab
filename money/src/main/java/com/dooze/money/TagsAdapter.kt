package com.dooze.money

import android.view.ViewGroup
import com.example.base.extensions.roundedCorner
import com.example.base.ui.BaseAdapter
import com.example.base.ui.BaseBindingViewHolder
import com.example.base.ui.BaseViewHolder
import com.dooze.db.entries.Tag
import com.dooze.money.databinding.ItemTagBinding

class TagsAdapter : BaseAdapter<Tag>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Tag> {
        return TagViewHolder(ItemTagBinding.inflate(layoutInflater, parent, false), this)
    }
}

class TagViewHolder(vb: ItemTagBinding, adapter: TagsAdapter) :
    BaseBindingViewHolder<Tag, ItemTagBinding>(vb, adapter) {

    init {
        vb.root.roundedCorner()
        addClickListener(vb.tvTag)
    }

    override fun bindUI(data: Tag, vb: ItemTagBinding, position: Int) {
        vb.tvTag.text = data.name
    }
}