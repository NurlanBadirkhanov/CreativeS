package com.creatives.vakansiyaaz.ADD.Spinners

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.creatives.vakansiyaaz.ADD.Spinners.SpinnerViewModel.SpinnerViewModel
import com.creatives.vakansiyaaz.R
import com.creatives.vakansiyaaz.databinding.FragmentTimeWorkBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class TimeWorkFragment : BottomSheetDialogFragment() {
    private lateinit var viewModel: SpinnerViewModel
    private lateinit var binding:FragmentTimeWorkBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTimeWorkBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[SpinnerViewModel::class.java]
        buttons()
    }

    private fun buttons() {
        ///h1 = Time 1
        val h1 = "Сменный график"
        val h2 =  "Полный день"
        val h3 = "Неполный день"
        val h4 = "Свободный график"


        binding.apply {
            bTime1.setOnClickListener {
                viewModel.time.value = h1
                dismiss()

            }
            bTime2.setOnClickListener {
                viewModel.time.value = h2
                dismissNow()

            }
            bTime3.setOnClickListener {
                viewModel.time.value = h3
                dismissNow()

            }
            bTime4.setOnClickListener {
                viewModel.time.value = h4
                dismissNow()
            }


        }

    }


}