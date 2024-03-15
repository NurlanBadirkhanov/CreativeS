package com.creatives.vakansiyaaz.home.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.creatives.vakansiyaaz.databinding.HomeItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*

class HomeAdapter(private val context: Context) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {
    class ViewHolder(val binding: HomeItemBinding) : RecyclerView.ViewHolder(binding.root)

    private var myList: MutableList<VacancyData> = mutableListOf()
    var onItemClick: ((VacancyData) -> Unit)? = null

    fun clearData() {
        myList.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = HomeItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = myList[position]

        data.let {
            holder.binding.apply {

                val formattedDate = convertMillisToDateString(it.data)
                setGetLikeForBase(data, holder)
                if (it.idElement == 0) {
                    idElement.visibility = View.GONE
                } else {
                    val hashtag = "#${it.idElement}"
                    idElement.text = hashtag
                }
                tvData.text = formattedDate

                if (it.experience.isEmpty()){
                    tvExperience.visibility = View.GONE
                }
                else{
                    tvExperience.text = it.experience
                }

                if (it.companyName.isEmpty()){
                    tvCompanyName.visibility = View.GONE
                }
                else{
                    tvCompanyName.text = it.companyName
                }
                if (it.city.isEmpty()){
                    tvCity.visibility = View.GONE
                }
                else{
                    tvCity.text = it.city
                }


                imLink.setOnClickListener {
                    showToast("Быстрая Вакансия")
                }
                imPro.setOnClickListener {
                    showToast("Аккаунт Проффессиональный и могут принадлежать компании!")
                }
                imVerfication.setOnClickListener {
                    showToast("Аккаунт Подтвержден!")
                }
                if (it.title.isEmpty()){
                    tvWork.visibility = View.GONE
                }
                else{
                    tvWork.text = it.title
                }
                getVerficationFromFirebase(data, holder)
                getImFromFirebase(data, holder)

                if (data.number.isEmpty()) {
                    button.visibility = View.INVISIBLE
                    buttonConnect.visibility = View.VISIBLE
                    buttonConnect.setOnClickListener {
                        onItemClick?.invoke(data)
                    }
                } else {
                    button.setOnClickListener {
                        val dialIntent = Intent(Intent.ACTION_DIAL)
                        dialIntent.data = Uri.parse("tel:${data.number}")
                        context.startActivity(dialIntent)
                    }

                }
                if (it.price.isEmpty()){
                    tvPrice.visibility = View.GONE
                }
                else{
                    val price = it.price
                    tvPrice.text = "$price AZN"
                }


                bDisLike.setOnClickListener {
                    bLike.visibility = View.VISIBLE
                    bDisLike.visibility = View.INVISIBLE
                    setNewElementCortFirebase(data)

                }
                bLike.setOnClickListener {
                    bLike.visibility = View.INVISIBLE
                    bDisLike.visibility = View.VISIBLE
                    deleteElementInBasket(data)

                }
            }

            holder.itemView.setOnClickListener {
                onItemClick?.invoke(data)
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun setGetLikeForBase(data: VacancyData, holder: ViewHolder) {
        val auth = FirebaseAuth.getInstance()
        val database = FirebaseDatabase.getInstance()
        val basketRef = database.reference
            .child("users")
            .child(auth.uid.toString())
            .child("basket")

        basketRef.child(data.id).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    holder.binding.bLike.visibility = View.VISIBLE
                    holder.binding.bDisLike.visibility = View.INVISIBLE
                } else {
                    // Продукта нет в корзине
                    holder.binding.bDisLike.visibility = View.VISIBLE
                    holder.binding.bLike.visibility = View.INVISIBLE
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Обработка ошибок при чтении данных
            }
        })

    }

    private fun deleteElementInBasket(data: VacancyData) {
        val auth = FirebaseAuth.getInstance()
        val database = FirebaseDatabase.getInstance()

        val productIdToRemove = data.id
        val basketRef = database.reference
            .child("users")
            .child(auth.uid.toString())
            .child("basket")

        basketRef.child(productIdToRemove).removeValue()

    }

    private fun setNewElementCortFirebase(data: VacancyData) {
        val auth = FirebaseAuth.getInstance()
        val database = FirebaseDatabase.getInstance()
        val productId = data.id

        val basketRef = database.reference
            .child("users")
            .child(auth.uid.toString())
            .child("basket")

        basketRef.child(productId).setValue(data)

    }


    private fun getVerficationFromFirebase(setData: VacancyData, holder: ViewHolder) {
        if (setData.verification) {
            holder.binding.imVerfication.visibility = View.VISIBLE
        } else {
            holder.binding.imVerfication.visibility = View.GONE

        }
    }

    private fun getImFromFirebase(setData: VacancyData, holder: ViewHolder) {
        when (setData.proAndLink) {
            "link" -> {
                holder.binding.imLink.visibility = View.VISIBLE
            }
            "pro" -> {
                holder.binding.imPro.visibility = View.VISIBLE
            }
            "notPro" -> {
                holder.binding.imPro.visibility = View.GONE
                holder.binding.imLink.visibility = View.GONE

            }
        }


    }


    override fun getItemCount(): Int {
        return myList.size
    }

    private fun convertMillisToDateString(millis: Long): String {
        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return dateFormat.format(Date(millis))
    }

    fun setData(data: List<VacancyData>) {
        myList.clear()
        myList.addAll(data)
        notifyDataSetChanged()
    }
}