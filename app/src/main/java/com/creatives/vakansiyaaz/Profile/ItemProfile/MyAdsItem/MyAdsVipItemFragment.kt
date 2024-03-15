package com.creatives.vakansiyaaz.Profile.ItemProfile.MyAdsItem

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.creatives.vakansiyaaz.R
import com.creatives.vakansiyaaz.databinding.FragmentMyAdsItemBinding
import com.creatives.vakansiyaaz.databinding.FragmentMyAdsVipItemBinding
import com.creatives.vakansiyaaz.home.ScreenDetailActivity
import com.creatives.vakansiyaaz.home.adapter.HomeAdapter
import com.creatives.vakansiyaaz.home.adapter.VacancyData
import com.creatives.vakansiyaaz.home.fetchProductsFromCategory1
import com.creatives.vakansiyaaz.home.fetchProductsFromCategoryMyAds
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MyAdsVipItemFragment : Fragment() {

    private lateinit var binding:FragmentMyAdsVipItemBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HomeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyAdsVipItemBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniRc()
        getDataFromFirebase()
    }

    private fun getDataFromFirebase() {
        CoroutineScope(Dispatchers.Main).launch {
        val auth: FirebaseAuth = FirebaseAuth.getInstance()
        val database = FirebaseDatabase.getInstance()
        val currentUser = auth.currentUser
        val reference = database.getReference("users")
            .child(currentUser?.uid ?: "")
            .child("myAdsPro")


        val product = mutableListOf<VacancyData>()
        val progressBar = binding.progressBar
        fetchProductsFromCategory1(adapter,requireContext(),reference, product,progressBar)

        }
    }

    private fun iniRc() {
        adapter = HomeAdapter(requireContext())
        recyclerView = binding.rc
        recyclerView.adapter = adapter

        adapter.onItemClick = { data ->
            val intent = Intent(requireContext(), ScreenDetailActivity::class.java)
            intent.putExtra("data", data)
            startActivity(intent)
        }
    }


}