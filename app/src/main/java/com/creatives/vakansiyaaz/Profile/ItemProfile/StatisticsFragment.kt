package com.creatives.vakansiyaaz.Profile.ItemProfile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.creatives.vakansiyaaz.R
import com.creatives.vakansiyaaz.databinding.FragmentPayBinding
import com.creatives.vakansiyaaz.databinding.FragmentStatisticsBinding
import com.creatives.vakansiyaaz.home.popBack
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class StatisticsFragment : Fragment() {
    private lateinit var binding: FragmentStatisticsBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatisticsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniFirebase()
        getDataFromFirebase()
        binding.bExit.setOnClickListener {popBack()}
    }

    private fun getDataFromFirebase() {
        val textName = database.reference.child("users").child(auth.uid.toString())
        textName.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val name = snapshot.child("name").getValue(String::class.java)
                binding.tvName.text = name
                val gmail = snapshot.child("gmail").getValue(String::class.java)
                binding.tvGmail.text = gmail
                val tvDataAkk = snapshot.child("registrationDate").getValue(Long::class.java)
                binding.tvDataAkk.text = tvDataAkk?.let { convertMillisToDateString(it) }

                val balance = snapshot.child("balance").getValue(String::class.java)
                binding.tvBalance.text = balance

                val verify = snapshot.child("verification").getValue(Boolean::class.java)
                if (verify == true){
                    binding.tvVerfication.text = "Верифицированый аккаунт"
                }
                else{
                    binding.tvVerfication.text = "Не верифицированый аккаунт"
                }
                val number = snapshot.child("number").getValue(String::class.java)
                binding.tvNumber.text = number

            }

            override fun onCancelled(error: DatabaseError) {
                // Обработка ошибок
            }
        })
    }

    private fun iniFirebase() {
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
    }

    private fun convertMillisToDateString(millis: Long): String {
        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return dateFormat.format(Date(millis))
    }


}