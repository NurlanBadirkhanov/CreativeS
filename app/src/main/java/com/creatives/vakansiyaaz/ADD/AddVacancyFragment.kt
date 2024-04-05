package com.creatives.vakansiyaaz.ADD

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.creatives.vakansiyaaz.ADD.Spinners.DescriptionFragment
import com.creatives.vakansiyaaz.ADD.Spinners.ExpirienceWorkFragment
import com.creatives.vakansiyaaz.ADD.Spinners.HegreeEducationFragment
import com.creatives.vakansiyaaz.ADD.Spinners.SpinnerViewModel.SpinnerViewModel
import com.creatives.vakansiyaaz.ADD.Spinners.TimeWorkFragment
import com.creatives.vakansiyaaz.R
import com.creatives.vakansiyaaz.databinding.FragmentAddVacanyBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.properties.Delegates


class AddVacancyFragment : Fragment() {

    private lateinit var binding: FragmentAddVacanyBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var viewModel: SpinnerViewModel
    var verification by Delegates.notNull<Boolean>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddVacanyBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[SpinnerViewModel::class.java]
        initFirebase()
        buttons()
        setupObserver()
        bOk()


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

    override fun onDestroy() {
        super.onDestroy()
        bClear()
    }


    private fun bOk() {

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

        binding.apply {
            bExit.setOnClickListener {
                findNavController().popBackStack()
                bClear()
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

            binding.bSave.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    val spheraWork = viewModel.spheraWork.value.toString()
                    val hegreeEdu = viewModel.hegreeEdu.value.toString()
                    val time = viewModel.time.value.toString()
                    val expirence = viewModel.expirence.toString()
                    val city = viewModel.city.value.toString()
                    val description = viewModel.description.value.toString()
                    val name = binding.editName.text.toString()
                    val price = binding.edPrice.text.toString()
                    val gmail = binding.editGmail.text.toString()
                    val number = binding.editNumber.text.toString()
                    val title = binding.edTitle.text.toString()

                    if (spheraWork.isEmpty() || time.isEmpty() || expirence.isEmpty() || hegreeEdu.isEmpty() ||
                        city.isEmpty() || description.isEmpty() || name.isEmpty() ||
                        gmail.isEmpty() || number.isEmpty() || title.isEmpty()
                    ) {
                        Toast.makeText(
                            context,
                            "Пожалуйста, заполните все поля",
                            Toast.LENGTH_SHORT
                        )
                            .show()

                    } else {
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
                            verification,
                            title
                        )
                    }
                }
            }


        }
    }


    private fun initFirebase() {
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
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
                findNavController().navigate(R.id.action_addVacancyFragment_to_spheraWorkFragment)
            }
            bCity.setOnClickListener {
                findNavController().navigate(R.id.action_addVacancyFragment_to_cityFragment)
            }

        }


    }

    private suspend fun saveToDatabase(
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
        verification: Boolean,
        title: String
    ) {


        val user = auth.currentUser
        val uid = user?.uid

        if (uid != null) {
            val userRef = database.reference.child("users").child(uid)

            val twoDaysInMillis: Long = 2 * 24 * 60 * 60 * 1000L


            val todayData = ServerValue.TIMESTAMP
            val uniqueId = auth.currentUser!!.uid.toString()
            val notPro = "notPro"

            val product = hashMapOf(
                "proAndLink" to notPro,
                "title" to title,
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
                "twoDaysInMillis" to twoDaysInMillis,

                )


            val sharedId = userRef.child("myAds").push().key
            val newAdRef = product + ("id" to sharedId)

            if (sharedId != null) {
                database.reference
                    .child("Vakancies")
                    .child("Vakancies")
                    .child(sharedId)
                    .setValue(newAdRef)
            }

            if (sharedId != null) {
                userRef.child("myAds").child(sharedId).setValue(newAdRef)
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
