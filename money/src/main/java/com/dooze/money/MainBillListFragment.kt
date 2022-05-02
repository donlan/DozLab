package com.dooze.money

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.dooze.money.databinding.MainBillListFragmentBinding
import com.example.base.extensions.viewBinding
import com.example.base.ui.BaseAdapter
import com.example.base.ui.BaseFragment
import com.example.base.ui.ItemClickListener

class MainBillListFragment : BaseFragment<MainBillListViewModel>(
    R.layout.main_bill_list_fragment,
    MainBillListViewModel::class.java
), View.OnClickListener, ItemClickListener {

    private val billsAdapter = BillsAdapter()
    private val binding by viewBinding(MainBillListFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvBills.layoutManager = LinearLayoutManager(requireContext())
        binding.rvBills.adapter = billsAdapter

        viewModel.observerBills(requireContext()) { bills ->
            billsAdapter.refresh(bills)
        }
        binding.fabAddBill.setOnClickListener(this)
        billsAdapter.itemClickListener = this
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.fabAddBill -> {
                //findNavController().navigate(R.id.addBillFragment)
                viewModel.importQianJi(requireContext())
            }
        }
    }

    override fun onItemClicked(adapter: BaseAdapter<*>, view: View, position: Int) {
    }

}