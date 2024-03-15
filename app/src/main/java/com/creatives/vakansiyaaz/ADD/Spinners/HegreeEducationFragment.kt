package com.creatives.vakansiyaaz.ADD.Spinners

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.creatives.vakansiyaaz.ADD.Spinners.SpinnerViewModel.SpinnerViewModel
import com.creatives.vakansiyaaz.R
import com.creatives.vakansiyaaz.databinding.FragmentHegreeEducationBinding
import com.creatives.vakansiyaaz.databinding.FragmentSferaWorkBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class HegreeEducationFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentHegreeEducationBinding
    private lateinit var viewModel: SpinnerViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHegreeEducationBinding.inflate(layoutInflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[SpinnerViewModel::class.java]

        ///h1 = hegree 1
        val h1 = "Высшее"
        val h2 =  "Неполное высшее"
        val h3 = "Среднее техническое"
        val h4 = "Среднее специальное"
        val h5 = "Среднее"


        binding.apply {
            bHeg1.setOnClickListener {
                viewModel.hegreeEdu.value = h1
                dismiss()

            }
            bHeg2.setOnClickListener {
                viewModel.hegreeEdu.value = h2
                dismissNow()

            }
            bHeg3.setOnClickListener {
                viewModel.hegreeEdu.value = h3
                dismissNow()

            }
            bHeg4.setOnClickListener {
                viewModel.hegreeEdu.value = h4
                dismissNow()

            }
            bHeg5.setOnClickListener {
                viewModel.hegreeEdu.value = h5
                dismissNow()

            }

        }

    }


}