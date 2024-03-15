package com.creatives.vakansiyaaz.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.creatives.vakansiyaaz.databinding.FragmentSearchBinding
import com.creatives.vakansiyaaz.home.ScreenDetailActivity
import com.creatives.vakansiyaaz.home.adapter.HomeAdapter
import com.creatives.vakansiyaaz.home.adapter.VacancyData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SearchFragment  : Fragment() {
    private lateinit var binding:FragmentSearchBinding
    private lateinit var auth:FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var rc:RecyclerView
    private lateinit var adapter: HomeAdapter
    private lateinit var context: Context
    private var searchData: MutableList<VacancyData> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRc()
        initFirebase()
//        getDataFromFirebase()
        buttons()
    }

    private fun showProgressBar() {
        binding.progressBar4.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar4.visibility = View.GONE
        binding.textView22.visibility = View.GONE


    }


    private fun buttons() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrEmpty()) {
                    showProgressBar() // Показываем прогресс бар перед выполнением поиска
                    searchInFirebase(newText)
                } else {
                    // Если строка пустая, очищаем результаты и скрываем прогресс-бар
                    adapter.clearData()
                    searchData.clear()
                    hideProgressBar()
                    adapter.notifyDataSetChanged()
                    binding.textView22.visibility = View.VISIBLE


                }
                return true
            }
        })
    }


    private fun searchInFirebase(query: String) {
        val databaseRef = database.reference.child("Vakancies").child("Vakancies")

        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                searchData.clear() // Очищаем предыдущие результаты
                for (postSnapshot in snapshot.children) {
                    val vacancy = postSnapshot.getValue(VacancyData::class.java)
                    vacancy?.let {
                        val idString = it.idElement.toString()
                        val price = it.price.toString()
                        if (it.title.contains(query, true) ||
                            it.work.contains(query, true) ||
                            it.price.contains(query, true) ||
                            it.companyName.contains(query, true) ||
                            idString == query
                        ) {
                            searchData.add(it)
                        }
                    }

                }
                // Обновляем RecyclerView с найденными результатами
                adapter.setData(searchData)
                hideProgressBar() // Скрываем прогресс бар после завершения загрузки
            }

            override fun onCancelled(error: DatabaseError) {

                hideProgressBar() // Скрываем прогресс бар в случае ошибки
            }
        })
    }




    private fun initFirebase() {
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
    }

    private fun initRc() {
        context = requireContext()
        adapter = HomeAdapter(context)
        rc = binding.recyclerView
        rc.adapter = adapter

        adapter.onItemClick = { data ->
            val intent = Intent(requireContext(), ScreenDetailActivity::class.java)
            intent.putExtra("data", data)
            startActivity(intent)
        }

    }

}