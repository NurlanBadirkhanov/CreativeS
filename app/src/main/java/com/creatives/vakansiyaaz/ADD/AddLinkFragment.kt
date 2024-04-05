package com.creatives.vakansiyaaz.ADD

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.creatives.vakansiyaaz.ADD.Spinners.SpinnerViewModel.SpinnerViewModel
import com.creatives.vakansiyaaz.R
import com.creatives.vakansiyaaz.databinding.FragmentAddLinkBinding
import com.creatives.vakansiyaaz.home.navigate
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
import kotlin.properties.Delegates


class AddLinkFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var viewModel: SpinnerViewModel
    private lateinit var binding: FragmentAddLinkBinding
    var verification by Delegates.notNull<Boolean>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddLinkBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[SpinnerViewModel::class.java]
        initFirebase()
        buttons()
        setupObserver()
        bOk()
        bMaterialDialog()

    }
    private fun bMaterialDialog() {
        binding.bIm.setOnClickListener {
            val dialog = MaterialAlertDialogBuilder(requireContext())
            dialog.setTitle("О Преимуществах")
            dialog.setMessage("1) Вакансия длится 2 дня. 2)Вам нужно указать ссылку на вакансию из Браузера.")
                .setPositiveButton("Ок") { _, _ ->

                }.show()
        }
    }
    private fun bClear() {
        binding.apply {
            val vm = viewModel
            vm.spheraWork.value = "Сфера"
            vm.expirence.value = "Опыт работы"
            vm.city.value = "Город"
            vm.hegreeEdu.value = "Образование"
            vm.time.value = "График работы"
            vm.description.value = "Напишите действия"
            binding.edPrice.text.clear()
            binding.edTitle.text.clear()
            binding.edLink.text.clear()

        }
    }
    override fun onDestroy() {
        super.onDestroy()
        bClear()
    }


    private fun bOk() {
        binding.bExit.setOnClickListener {
            findNavController().popBackStack()
            bClear()
        }
        binding.bSave.setOnClickListener {
            val spheraWork = viewModel.spheraWork.value.toString()
            val city = viewModel.city.value.toString()
            val price = binding.edPrice.text.toString()
            val title = binding.edTitle.text.toString()
            val link = binding.edLink.text.toString()


            if (city.isNotEmpty()  && title.isNotEmpty() && link.isNotEmpty()) {
                saveToDatabase(
                    spheraWork,
                    city,
                    price,
                    title,
                    verification,
                    link,
                )
            } else {
                Toast.makeText(
                    requireContext(),
                    "Заполните все поля!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    private fun initFirebase() {
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        val databaseReference = FirebaseDatabase.getInstance().reference
        val userReference = databaseReference.child("users")
            .child(auth.currentUser?.uid.toString())
            .child("verification")

        userReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                verification = dataSnapshot.getValue(Boolean::class.java) == true
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }


    private fun setupObserver() {
        viewModel.spheraWork.observe(viewLifecycleOwner) { data ->
            binding.tvSfera.text = data
        }
        viewModel.city.observe(viewLifecycleOwner) {
            binding.tvCity.text = it
        }

    }


    private fun buttons() {
        binding.apply {

            bSfera.setOnClickListener {
                navigate(R.id.spheraWorkFragment)
            }
            bCity.setOnClickListener {
                navigate(R.id.cityFragment)
            }
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    edPrice.isEnabled = false
                    edPrice.setText("Договорная")
                } else {
                    edPrice.isEnabled = true
                    edPrice.setText("")
                }
            }


        }


    }

    private fun saveToDatabase(
        Sphera: String,
        City: String,
        Price: String,
        title: String,
        verification: Boolean,
        link: String,
    ) {

        val user = auth.currentUser
        val uid = user?.uid

        if (uid != null) {
            val userRef = database.reference.child("users").child(uid)
            val todayData = ServerValue.TIMESTAMP
            val uniqueId = auth.uid.toString()
            val day: Long = 1 * 24 * 60 * 60 * 1000L

            val IdItem = (10000..99999).random()

            val product = hashMapOf(
                "idElement" to IdItem,
                "verification" to verification,
                "proAndLink" to "link",
                "uniqueId" to uniqueId,
                "sphera" to Sphera,
                "city" to City,
                "price" to Price,
                "data" to todayData,
                "title" to title,
                "day" to day,
                "link" to link,
            )

            val sharedId = userRef.child("myAds").push().key
            val newAdRef = product + ("id" to sharedId)


            database = FirebaseDatabase.getInstance()

            if (sharedId != null) {
                userRef.child("myAds").child(sharedId).setValue(newAdRef)

                val productWithId = product + ("id" to sharedId)
                database.reference.child("Vakancies")
                    .child("Vakancies")
                    .child(sharedId)
                    .setValue(productWithId)
                    .addOnSuccessListener {
                        Toast.makeText(
                            requireContext(),
                            "Данные успешно сохранены!",
                            Toast.LENGTH_SHORT
                        ).show()
                        bClear()


                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            requireContext(),
                            "Ошибка сохранения данных",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
            } else {
                Toast.makeText(
                    requireContext(),
                    "Пользователь не аутентифицирован!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


    }
}