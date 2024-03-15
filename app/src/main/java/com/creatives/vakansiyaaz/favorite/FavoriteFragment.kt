package com.creatives.vakansiyaaz.favorite

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.creatives.vakansiyaaz.R
import com.creatives.vakansiyaaz.databinding.FragmentFavoriteBinding
import com.creatives.vakansiyaaz.home.ScreenDetailActivity
import com.creatives.vakansiyaaz.home.adapter.HomeAdapter
import com.creatives.vakansiyaaz.home.adapter.VacancyData
import com.creatives.vakansiyaaz.home.fetchProductsFromCategory1
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FavoriteFragment : Fragment() {
    lateinit var binding: FragmentFavoriteBinding
    lateinit var adapter: HomeAdapter
    lateinit var recyclerView: RecyclerView
    private var backPressedOnce = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        back()
        initRc()
        getFirebase()

    }

    private fun back() {
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            if (backPressedOnce) {
                requireActivity().finish()
            } else {
                backPressedOnce = true
                Toast.makeText(
                    requireContext(),
                   "Точно хотите выйти?",
                    Toast.LENGTH_SHORT
                )
                    .show()
                Handler().postDelayed(
                    { backPressedOnce = false },
                    2000
                ) // Сброс флага через 2 секунды
            }
        }
        callback.isEnabled = true
    }

    private fun getFirebase() {
        CoroutineScope(Dispatchers.Main).launch {
            val auth = FirebaseAuth.getInstance()
            val database = FirebaseDatabase.getInstance()

            val reference = database.getReference("users")
                .child(auth.uid.toString())
                .child("basket")
            val product = mutableListOf<VacancyData>()
            val progressBar = binding.progressBar2
            fetchProductsFromCategory1(adapter, requireContext(), reference, product, progressBar)
            binding.swipr.setOnRefreshListener {
                product.clear()
                CoroutineScope(Dispatchers.Main).launch {
                    fetchProductsFromCategory1(
                        adapter,
                        requireContext(),
                        reference,
                        product,
                        progressBar

                    )
                    binding.swipr.isRefreshing = false


                }
            }
        }
    }


    private fun initRc() {
        adapter = HomeAdapter(requireContext())
        recyclerView = binding.rc
        recyclerView.adapter = adapter
        adapter.onItemClick = { adminData ->
            val intent = Intent(requireContext(), ScreenDetailActivity::class.java)
            intent.putExtra("data", adminData)
            startActivity(intent)
        }

    }


}