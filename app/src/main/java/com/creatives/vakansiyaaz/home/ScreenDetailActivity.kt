package com.creatives.vakansiyaaz.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.creatives.vakansiyaaz.databinding.ActivityScreenDetailBinding
import com.creatives.vakansiyaaz.home.adapter.Home2Adapter
import com.creatives.vakansiyaaz.home.adapter.VacancyData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ScreenDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScreenDetailBinding
    private lateinit var getProduct: VacancyData
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: Home2Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScreenDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getProduct()
        initRc()
        setGetLikeForBase()
        buttons()
    }

    private fun buttons() {
        binding.apply {
            bExit.setOnClickListener {
                finish()
            }
            bDisLike.setOnClickListener {
                bLike.visibility = View.VISIBLE
                bDisLike.visibility = View.INVISIBLE
                setNewElementCortFirebase()

            }
            bLike.setOnClickListener {
                bDisLike.visibility = View.VISIBLE
                bLike.visibility = View.INVISIBLE
                deleteElementInBasket()
            }
        }
    }

    private fun setGetLikeForBase() {
        val add = binding.bDisLike
        val delete = binding.bLike

        val data = getProduct
        val auth = FirebaseAuth.getInstance()
        val database = FirebaseDatabase.getInstance()
        val basketRef = database.reference
            .child("users")
            .child(auth.uid.toString())
            .child("basket")

        basketRef.child(data.id).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    delete.visibility = View.VISIBLE
                    add.visibility = View.INVISIBLE
                } else {
                    // Продукта нет в корзине
                    add.visibility = View.VISIBLE
                    delete.visibility = View.INVISIBLE
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Обработка ошибок при чтении данных
            }
        })

    }

    private fun deleteElementInBasket() {

        val auth = FirebaseAuth.getInstance()
        val database = FirebaseDatabase.getInstance()

        val productIdToRemove = getProduct.id
        val basketRef = database.reference
            .child("users")
            .child(auth.uid.toString())
            .child("basket")

        basketRef.child(productIdToRemove).removeValue()

    }

    private fun setNewElementCortFirebase() {
        val auth = FirebaseAuth.getInstance()
        val database = FirebaseDatabase.getInstance()
        val productId = getProduct.id

        val basketRef = database.reference
            .child("users")
            .child(auth.uid.toString())
            .child("basket")

        basketRef.child(productId).setValue(getProduct)

    }

    private fun convertMillisToDateString(millis: Long): String {
        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return dateFormat.format(Date(millis))
    }

    private fun getProduct() {
        getProduct = (intent.getSerializableExtra("data") as VacancyData)
        binding.apply {
            if (getProduct.link.isEmpty()) {
                binding.bLink.visibility = View.GONE
            } else {
                binding.bLink.setOnClickListener {
                    val url = getProduct.link // замените ссылку на вашу
                    val intent = Intent(Intent.ACTION_VIEW)

                    if (!url.startsWith("http://") && !url.startsWith("https://")) {
                        intent.data = Uri.parse("http://$url")
                    } else {
                        intent.data = Uri.parse(url)
                    }

                    startActivity(intent)
                }

            }

            if (getProduct.city.isEmpty()) {
                tvCity.visibility = View.GONE
            } else {
                val city = "Город вакансии: ${getProduct.city}"
                tvCity.text = city
            }

            if (getProduct.data == null) {
                tvData.visibility = View.GONE
            } else {
                val convert = convertMillisToDateString(getProduct.data)
                val date = "Дата вакансии: $convert"
                tvData.text = date
            }



            if (getProduct.desc.isEmpty()) {
                tvDesc.visibility = View.GONE
            } else {
                tvDesc.text = getProduct.desc
            }

            if (getProduct.name.isEmpty()) {
                tvName.visibility = View.GONE
            } else {
                val name = "Имя работодателя: ${getProduct.name}"
                tvName.text = name
            }

            if (getProduct.experience.isEmpty()) {
                tvExpirience.visibility = View.GONE
            } else {
                val expiriencePerson = "Опыт работы: ${getProduct.experience}"
                tvExpirience.text = expiriencePerson
            }

            if (getProduct.title.isEmpty()) {
                tvTitle.visibility = View.GONE

            } else {
                tvTitle.text = getProduct.title
            }

            if (getProduct.companyName.isEmpty()) {
                tvNameOrg.visibility = View.GONE

            } else {
                if (getProduct.verification) {
                    imageView25.visibility = View.VISIBLE
                } else {
                    imageView25.visibility = View.INVISIBLE
                }
                tvNameOrg.text = getProduct.companyName
            }

            if (getProduct.price.isEmpty()) {
                tvPrice.visibility = View.GONE
            } else {
                val price = "${getProduct.price} AZN"
                tvPrice.text = price

            }
            ///График работы
            if (getProduct.timeWork.isEmpty()) {
                tvWork.visibility = View.GONE
            } else {
                val graph = "График работы: ${getProduct.timeWork}"
                tvWork.text = graph
            }
            if (getProduct.sphera.isEmpty()) {
                tvSphera.visibility = View.GONE
            } else {
                val sphera = "Сфера работы: ${getProduct.sphera}"
                tvSphera.text = sphera
            }

            if (getProduct.number.isEmpty()) {
                tvNumber.visibility = View.GONE
                bCall.visibility = View.GONE
            } else {
                val number = "Номер работодателя: ${getProduct.number}"
                tvNumber.text = number
                bCall.setOnClickListener {
                    val dialIntent = Intent(Intent.ACTION_DIAL)
                    dialIntent.data = Uri.parse("tel:${getProduct.number}")
                    startActivity(dialIntent)
                }
            }

            if (getProduct.gmail.isEmpty()) {
                tvGmail.visibility = View.GONE
                bGmail.visibility = View.GONE

            } else {
                val gmail = "Почта работодателя: ${getProduct.gmail} "
                tvGmail.text = gmail
                bGmail.setOnClickListener {
                    val email = getProduct.gmail
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        // Устанавливаем адрес электронной почты в качестве данных для Intent
                        data = Uri.parse("mailto:$email")

                    }
                    startActivity(intent)
                }

            }


        }
    }


    private fun initRc() {
        adapter = Home2Adapter(this)
        recyclerView = binding.rc
        recyclerView.adapter = adapter
        getDataFromFirebase()

        adapter.onItemClick = { data ->
            val intent = Intent(this, ScreenDetailActivity::class.java)
            intent.putExtra("data", data)
            startActivity(intent)
        }

    }

    private fun getDataFromFirebase() {

        CoroutineScope(Dispatchers.Main).launch {
            val database = FirebaseDatabase.getInstance()
            val reference = database.getReference("Vakancies").child("Vakancies")
            val product = mutableListOf<VacancyData>()
            val progressBar = binding.progressBar3
            fetchProductsFromCategoryHome2Adapter(adapter, reference, product, progressBar)
        }

    }
}