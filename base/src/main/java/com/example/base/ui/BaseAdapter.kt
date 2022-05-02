package com.example.base.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.base.extensions.lazyFast

abstract class BaseAdapter<T>(private var datas: List<T> = emptyList()) :
    RecyclerView.Adapter<BaseViewHolder<T>>() {

    private lateinit var recyclerView: RecyclerView

    var itemClickListener: ItemClickListener? = null

    var itemLongClickListener: ItemLongClickListener? = null

    protected val layoutInflater by lazyFast { LayoutInflater.from(recyclerView.context) }

    @SuppressLint("NotifyDataSetChanged")
    fun refresh(datas: List<T>) {
        this.datas = datas
        notifyDataSetChanged()
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    fun getItem(position: Int) = datas[position]

    @CallSuper
    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        holder.bindUI(getItem(position), position)
    }

    override fun getItemCount(): Int {
        return datas.size
    }
}


abstract class BaseViewHolder<T>(view: View, private val adapter: BaseAdapter<T>) :
    RecyclerView.ViewHolder(view) {

    private val clickListener by lazyFast {
        View.OnClickListener {
            adapter.itemClickListener?.onItemClicked(adapter, it, layoutPosition)
        }
    }

    private val longClickListener by lazyFast {
        View.OnLongClickListener {
            adapter.itemLongClickListener?.onItemLongClicked(adapter, it, layoutPosition)
            true
        }
    }

    protected fun addClickListener(view: View) {
        view.setOnClickListener(clickListener)
    }

    protected fun addLongClickListener(view: View) {
        view.setOnLongClickListener(longClickListener)
    }

    abstract fun bindUI(data: T, position: Int)
}

abstract class BaseBindingViewHolder<T, VB : ViewBinding>(
    private val vb: VB,
    adapter: BaseAdapter<T>
) : BaseViewHolder<T>(vb.root, adapter) {
    override fun bindUI(data: T, position: Int) {
        bindUI(data, vb, position)
    }

    abstract fun bindUI(data: T, vb: VB, position: Int)
}

interface ItemClickListener {
    fun onItemClicked(adapter: BaseAdapter<*>, view: View, position: Int)
}

interface ItemLongClickListener {
    fun onItemLongClicked(adapter: BaseAdapter<*>, view: View, position: Int)
}
