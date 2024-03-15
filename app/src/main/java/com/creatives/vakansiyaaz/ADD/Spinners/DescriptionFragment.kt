package com.creatives.vakansiyaaz.ADD.Spinners

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.creatives.vakansiyaaz.ADD.Spinners.SpinnerViewModel.SpheraAdapter
import com.creatives.vakansiyaaz.ADD.Spinners.SpinnerViewModel.SpinnerViewModel
import com.creatives.vakansiyaaz.R
import com.creatives.vakansiyaaz.databinding.FragmentDescriptionBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class DescriptionFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentDescriptionBinding
    private lateinit var viewModel: SpinnerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDescriptionBinding.inflate(layoutInflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[SpinnerViewModel::class.java]

        binding.bOk.setOnClickListener {
            val ed = binding.edPrice.text.toString()
            viewModel.description.value = ed
            dismiss()
        }
        viewModel.description.observe(viewLifecycleOwner) { newText ->
            binding.edPrice.setText(newText)
        }

    }




}