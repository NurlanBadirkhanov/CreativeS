package com.creatives.vakansiyaaz.ADD.Spinners

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.creatives.vakansiyaaz.ADD.Spinners.SpinnerViewModel.SpinnerViewModel
import com.creatives.vakansiyaaz.databinding.FragmentExpirienceWorkBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ExpirienceWorkFragment : BottomSheetDialogFragment() {
    private lateinit var viewModel: SpinnerViewModel
    private lateinit var binding: FragmentExpirienceWorkBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExpirienceWorkBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(SpinnerViewModel::class.java)

        val work0 = "Неопытный"
        val work1 =  "Менее 1 года"
        val work13 = "От 1 года до 3 лет"
        val work35 = "От 3 до 5 лет"
        val work5 = "Более 5 лет опыта"


        binding.apply {
            bWork0.setOnClickListener {
                viewModel.expirence.value = work0
                dismiss()

            }
            bWork1.setOnClickListener {
                viewModel.expirence.value = work1
                dismissNow()

            }
            bWork13.setOnClickListener {
                viewModel.expirence.value = work13
                dismissNow()

            }
            bWork35.setOnClickListener {
                viewModel.expirence.value = work35
                dismissNow()

            }
            bWork5.setOnClickListener {
                viewModel.expirence.value = work5
                dismissNow()

            }
        }

    }


}