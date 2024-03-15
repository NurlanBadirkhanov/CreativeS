package com.creatives.vakansiyaaz.Profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.creatives.vakansiyaaz.R
import com.creatives.vakansiyaaz.R.id.myAdsFragment
import com.creatives.vakansiyaaz.databinding.DialogNameNumberBinding
import com.creatives.vakansiyaaz.databinding.FragmentProfileBinding
import com.creatives.vakansiyaaz.home.navigate
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ProfileFragment : Fragment() {
    lateinit var binding: FragmentProfileBinding
    private var originalStatusBarColor: Int = 0
    lateinit var database: FirebaseDatabase
    lateinit var auth: FirebaseAuth


    override fun onResume() {
        super.onResume()
        activity!!.window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.gray)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity!!.window.statusBarColor = originalStatusBarColor

    }


    override fun onStop() {
        super.onStop()
        activity!!.window.statusBarColor = originalStatusBarColor

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        activity!!.window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.gray)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBaseItem()
        initFirebase()
        buttons()


    }

    private fun initFirebase() {
        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()
    }

    private fun getBaseItem() {
        CoroutineScope(Dispatchers.Main).launch {
            val auth = FirebaseAuth.getInstance()
            val database = FirebaseDatabase.getInstance()
            val reference = database.getReference("users")
                .child(auth.uid.toString())
                .child("myAds")

            val proAds = database.getReference("users")
                .child(auth.uid.toString())
                .child("myAdsPro")

            proAds.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val count = snapshot.childrenCount.toInt()
                    binding.tvVipAds.text = count.toString()
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })

            reference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val count = dataSnapshot.childrenCount.toInt()
                    binding.tvAds.text = count.toString()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Обработка ошибки, если не удалось получить данные из базы данных
                }
            })

            val getName = database.reference
                .child("users")
                .child(auth.uid.toString())

            getName.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val name = snapshot.child("name").getValue(String::class.java)
                    binding.tvPersonNick.text = name

                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

            val getVerfication = database.reference
                .child("users")
                .child(auth.uid.toString())

            getVerfication.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val verification = snapshot.child("verification").getValue(Boolean::class.java)
                    if (verification == true) {
                        binding.tvVerfication.visibility = View.VISIBLE
                    }

                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        }

    }

    private fun buttons() {
        binding.apply {
            bMyAds.setOnClickListener {
                navigate(myAdsFragment)
            }
            bPayments.setOnClickListener {
                findNavController().navigate(R.id.paymentFragment)

            }
            bSettings.setOnClickListener {
//                startActivity(Intent(requireContext(), LoginActivity::class.java))
                navigate(R.id.settingsFragment)

            }
            bEdit.setOnClickListener {
                showForgotPasswordDialog()
            }
            bStatistics.setOnClickListener {
                navigate(R.id.statisticsFragment)
            }
        }
    }

    private fun showForgotPasswordDialog() {
        val builder = MaterialAlertDialogBuilder(requireContext())
        val inflater = DialogNameNumberBinding.inflate(layoutInflater)
        val dialogView = inflater.root

        builder.setView(dialogView)
            .setTitle("Записать Имя и Номер")
            .setPositiveButton("Сохранить") { _, _ ->

                val name = inflater.editName.text.toString()
                val number = inflater.editNumber.text.toString()
                setNameOrNumberFirebase(name, number)

            }
            .setNegativeButton("Отмена", null)
            .show()

        val getVerfication = database.reference
            .child("users")
            .child(auth.uid.toString())

        getVerfication.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val name = snapshot.child("name").getValue(String::class.java)
                val number = snapshot.child("number").getValue(String::class.java)
                inflater.editName.setText(name)
                inflater.editNumber.setText(number)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }


    private fun setNameOrNumberFirebase(name: String, number: String) {
        val database = FirebaseDatabase.getInstance()
        val auth = FirebaseAuth.getInstance()

        val reference = database.reference.child("users")
            .child(auth.uid.toString())
            .child("name")

        val referenceNumber = database.reference.child("users")
            .child(auth.uid.toString())
            .child("number")

        reference.setValue(name)
        referenceNumber.setValue(number)

    }

    data class data(
        val name: String,
        val number: String,
    )


}