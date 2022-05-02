package com.dooze.money

import android.view.ViewGroup
import com.example.base.ui.BaseAdapter
import com.example.base.ui.BaseBindingViewHolder
import com.example.base.ui.BaseViewHolder
import com.dooze.db.entries.Bill
import com.dooze.money.databinding.ItemBillsBinding

class BillsAdapter : BaseAdapter<Bill>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Bill> {
        return BillViewHolder(ItemBillsBinding.inflate(layoutInflater, parent, false), this)
    }
}

class BillViewHolder(vb: ItemBillsBinding, adapter: BillsAdapter) :
    BaseBindingViewHolder<Bill, ItemBillsBinding>(vb, adapter) {

    override fun bindUI(data: Bill, vb: ItemBillsBinding, position: Int) {
        vb.tvAmount.text = data.amount.toString()
        vb.tvDesc.text = data.descriptor
    }

}