package com.creatives.vakansiyaaz.ADD

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.creatives.vakansiyaaz.ADD.Spinners.DescriptionFragment
import com.creatives.vakansiyaaz.ADD.Spinners.ExpirienceWorkFragment
import com.creatives.vakansiyaaz.ADD.Spinners.HegreeEducationFragment
import com.creatives.vakansiyaaz.ADD.Spinners.SpinnerViewModel.SpinnerViewModel
import com.creatives.vakansiyaaz.ADD.Spinners.TimeWorkFragment
import com.creatives.vakansiyaaz.R
import com.creatives.vakansiyaaz.databinding.FragmentAddVacancyProBinding
import com.creatives.vakansiyaaz.home.popBack
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.properties.Delegates


class AddVacancyProFragment : Fragment() {

    private lateinit var binding: FragmentAddVacancyProBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var viewModel: SpinnerViewModel
    var verification by Delegates.notNull<Boolean>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddVacancyProBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[SpinnerViewModel::class.java]
        initFirebase()
        buttons()
        setupObserver()
        bMaterialDialog()
        binding.bExit.setOnClickListener {
            bClear()
            popBack()

        }


    }


    private fun bMaterialDialog() {
        binding.bIm.setOnClickListener {
            val dialog = MaterialAlertDialogBuilder(requireContext())
            dialog.setTitle("О Преимуществах")
            dialog.setMessage("1) Вакансия длится неделя 2) Рядом будет Корона подтверждающей Проффесиональный аккаунт. 3) Про Вакансия стоит 1 AZN")
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
            binding.editName.text.clear()
            binding.edPrice.text.clear()
            binding.editGmail.text.clear()
            binding.editNumber.text.clear()
            binding.edTitle.text.clear()
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
        viewModel.expirence.observe(viewLifecycleOwner) { value ->
            binding.tvWorkExpir.text = value
        }
        viewModel.hegreeEdu.observe(viewLifecycleOwner) { value ->
            binding.tvEdu.text = value
        }
        viewModel.time.observe(viewLifecycleOwner) {
            binding.tvTime.text = it
        }
        viewModel.spheraWork.observe(viewLifecycleOwner) { data ->
            binding.tvSfera.text = data
        }
        viewModel.city.observe(viewLifecycleOwner) {
            binding.tvCity.text = it
        }
        viewModel.description.observe(viewLifecycleOwner) {
            binding.edDeck.text = it
        }
    }


    private fun buttons() {
        binding.apply {
            bWorkExpir.setOnClickListener {
                val bottomSheetDialogFragment = ExpirienceWorkFragment()
                bottomSheetDialogFragment.show(parentFragmentManager, bottomSheetDialogFragment.tag)
            }
            bTimeWork.setOnClickListener {
                val bottomSheetDialogFragment = TimeWorkFragment()
                bottomSheetDialogFragment.show(parentFragmentManager, bottomSheetDialogFragment.tag)
            }
            bEdu.setOnClickListener {
                val bottomSheetDialogFragment = HegreeEducationFragment()
                bottomSheetDialogFragment.show(parentFragmentManager, bottomSheetDialogFragment.tag)
            }
            desc.setOnClickListener {
                val bottomSheetDialogFragment = DescriptionFragment()
                bottomSheetDialogFragment.show(parentFragmentManager, bottomSheetDialogFragment.tag)
            }
            bSfera.setOnClickListener {
                findNavController().navigate(R.id.spheraWorkFragment)
            }
            bCity.setOnClickListener {
                findNavController().navigate(R.id.cityFragment)
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
            bSave.setOnClickListener {
                val spheraWork = viewModel.spheraWork.value.toString()
                val hegreeEdu = viewModel.hegreeEdu.value.toString()
                val time = viewModel.time.value.toString()
                val expirence = viewModel.expirence.value.toString()
                val city = viewModel.city.value.toString()
                val description = viewModel.description.value.toString()
                val name = binding.editName.text.toString()
                val price = binding.edPrice.text.toString()
                val gmail = binding.editGmail.text.toString()
                val number = binding.editNumber.text.toString()
                val title = binding.edTitle.text.toString()
                val nameOrg = binding.editNameOrg.text.toString()

                if (spheraWork.isEmpty()
                    || time.isEmpty()
                    || expirence.isEmpty()
                    || hegreeEdu.isEmpty()
                    || city.isEmpty()
                    || description.isEmpty()
                    || gmail.isEmpty()
                    || title.isEmpty()
                    || nameOrg.isEmpty()
                ) {
                    Toast.makeText(context, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }
                CoroutineScope(Dispatchers.Main).launch {

                    val database = FirebaseDatabase.getInstance()
                    val reference = database.getReference("users")

                    val uid = auth.uid
                    val balanceReference = reference.child(uid.toString()).child("balance")
                    balanceReference.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val currentBalanceString = snapshot.getValue(String::class.java) ?: "0"
                            val currentBalance = currentBalanceString.toInt()

                            if (currentBalance > 0) {
                                val newBalance = (currentBalance - 1).toString()
                                balanceReference.setValue(newBalance).addOnSuccessListener {
                                    saveToDatabase(
                                        spheraWork,
                                        time,
                                        expirence,
                                        hegreeEdu,
                                        city,
                                        price,
                                        description,
                                        name,
                                        gmail,
                                        number,
                                        nameOrg,
                                        verification,
                                        title = title

                                    )
                                }.addOnFailureListener {
                                    // Ошибка при выполнении операции вычитания из баланса
                                }
                            } else {
                                Toast.makeText(
                                    context,
                                    "Пожалуйста Пополните Баланс",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            // Обработка ошибки при чтении данных из базы данных
                        }
                    })
                }
            }
        }
    }


    private fun saveToDatabase(
        Sphera: String,
        TimeWork: String,
        WorkExpirience: String,
        Education: String,
        City: String,
        Price: String,
        Description: String,
        Name: String,
        Gmail: String,
        Number: String,
        nameOrg: String,
        verification: Boolean,
        title: String,

        ) {

        val user = auth.currentUser
        val uid = user?.uid

        if (uid != null) {
            val userRef = database.reference.child("users").child(uid)

            val todayData = ServerValue.TIMESTAMP
            val weekInMillis: Long = 7 * 24 * 60 * 60 * 1000L
            val uniqueId = auth.currentUser!!.uid
            val idItem = (10000..99999).random()

            val product = hashMapOf(
                "idElement" to idItem,
                "proAndLink" to "pro",
                "title" to title,
                "nameOrg" to nameOrg,
                "uniqueId" to uniqueId,
                "sphera" to Sphera,
                "timeWork" to TimeWork,
                "experience" to WorkExpirience,
                "education" to Education,
                "city" to City,
                "price" to Price,
                "name" to Name,
                "desc" to Description,
                "Gmail" to Gmail,
                "number" to Number,
                "data" to todayData,
                "verification" to verification,
                "weekInMillis" to weekInMillis,
            )


            val sharedId = userRef.child("myAds").push().key
            val newAdRef = product + ("id" to sharedId)

            if (sharedId != null) {
                database.reference
                    .child("Vakancies")
                    .child("Vakancies")
                    .child(sharedId)
                    .setValue(newAdRef)
                database.reference
                    .child("Vakancies")
                    .child("VakanciesPro")
                    .child(sharedId)
                    .setValue(newAdRef)

            }

            if (sharedId != null) {
                userRef.child("myAdsPro").child(sharedId).setValue(newAdRef)
                    .addOnSuccessListener {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.saveOk),
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

    override fun onDestroy() {
        super.onDestroy()
        bClear()
    }
}