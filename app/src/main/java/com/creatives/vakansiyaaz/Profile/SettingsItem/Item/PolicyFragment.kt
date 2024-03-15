package com.creatives.vakansiyaaz.Profile.SettingsItem.Item

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.creatives.vakansiyaaz.R
import com.creatives.vakansiyaaz.databinding.FragmentPolicyBinding
import com.creatives.vakansiyaaz.home.popBack


class PolicyFragment : Fragment() {
    private lateinit var binding: FragmentPolicyBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPolicyBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bExit.setOnClickListener {
            popBack()
        }
    }


}