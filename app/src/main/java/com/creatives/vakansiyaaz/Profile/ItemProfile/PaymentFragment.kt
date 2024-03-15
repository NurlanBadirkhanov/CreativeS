package com.creatives.vakansiyaaz.Profile.ItemProfile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.android.billingclient.api.BillingClient
import com.creatives.vakansiyaaz.R
import com.creatives.vakansiyaaz.databinding.FragmentPaymentBinding
import com.creatives.vakansiyaaz.home.adapter.VacancyData
import com.creatives.vakansiyaaz.home.popBack
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PaymentFragment : Fragment() {
    private lateinit var binding: FragmentPaymentBinding
    private var originalStatusBarColor: Int = 0
    lateinit var billingClient: BillingClient
    private val skuList = ArrayList<String>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PaymentAdapter


    override fun onResume() {
        super.onResume()
        if (originalStatusBarColor == 0) {
            originalStatusBarColor = activity!!.window.statusBarColor
        }
        activity!!.window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.black)
    }

    override fun onStop() {
        super.onStop()
        activity!!.window.statusBarColor = originalStatusBarColor

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPaymentBinding.inflate(layoutInflater, container, false)
        activity!!.window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.black)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getDataFromFirebase()
        buttons()

    }

    private fun initRc() {
        adapter = PaymentAdapter(requireContext())
        recyclerView = binding.rc
        recyclerView.adapter = adapter

    }

    private fun getDataFromFirebase() {
        val list: MutableList<VacancyData> = mutableListOf()
        val auth = FirebaseAuth.getInstance()
        val database = FirebaseDatabase.getInstance()
        val reference = database.reference
            .child("users")
            .child(auth.uid.toString())

        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val balance = snapshot.child("balance").getValue(String::class.java)
                val azn = "${balance}AZN"
                binding.tvBalance.text = azn
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
        val historyItem = database.reference
            .child("users")
            .child(auth.uid.toString())
            .child("history_balance")

        historyItem.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (i in snapshot.children) {
                    initRc()
                    val price = i.child("price").getValue(String::class.java)
                    val date = i.child("timestamp").getValue(Long::class.java)
                    val product = VacancyData(
                        price = price ?: "1000",
                        data = date ?: 1,
                    )

                    list.add(product)
                    adapter.setData(list)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

    }

    private fun buttons() {
        binding.apply {
            bExit.setOnClickListener {
                popBack()
            }
            button.setOnClickListener {
                val bottom = PayFragment()
                bottom.show(activity!!.supportFragmentManager, bottom.tag)

            }
        }
    }


}