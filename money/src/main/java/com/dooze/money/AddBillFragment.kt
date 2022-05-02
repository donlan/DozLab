package com.dooze.money

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.dooze.money.databinding.AddBillFragmentBinding
import com.example.base.extensions.viewBinding
import com.example.base.ui.BaseFragment

class AddBillFragment :
    BaseFragment<AddBillViewModel>(R.layout.add_bill_fragment, AddBillViewModel::class.java),
    View.OnClickListener {


    private val binding by viewBinding(AddBillFragmentBinding::bind)
    private val tagsAdapter = TagsAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvTags.layoutManager = GridLayoutManager(requireContext(), 4)
        binding.rvTags.adapter = tagsAdapter


        viewModel.observeTags(requireContext()) {
            tagsAdapter.refresh(it)
        }

        binding.numOne.setOnClickListener(this)
        binding.numTwo.setOnClickListener(this)
        binding.numThree.setOnClickListener(this)
        binding.numFour.setOnClickListener(this)
        binding.numFine.setOnClickListener(this)
        binding.numSix.setOnClickListener(this)
        binding.numSeven.setOnClickListener(this)
        binding.numEight.setOnClickListener(this)
        binding.numNine.setOnClickListener(this)
        binding.numZero.setOnClickListener(this)
        binding.dot.setOnClickListener(this)
        binding.delete.setOnClickListener(this)
        binding.done.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.numOne -> {

            }
            R.id.numTwo -> {

            }
            R.id.numThree -> {

            }
            R.id.numFour -> {

            }
            R.id.numFine -> {

            }
            R.id.numSix -> {

            }
            R.id.numSeven -> {

            }
            R.id.numEight -> {

            }
            R.id.numNine -> {

            }
            R.id.numZero -> {

            }
            R.id.dot -> {

            }
            R.id.delete -> {

            }
            R.id.done -> {

            }
        }
    }

}