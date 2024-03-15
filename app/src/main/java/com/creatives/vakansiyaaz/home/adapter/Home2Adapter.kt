package com.creatives.vakansiyaaz.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.creatives.vakansiyaaz.databinding.ItemScreenDetailBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*

class Home2Adapter(private val context:Context):RecyclerView.Adapter<Home2Adapter.ViewHolder>() {
    class ViewHolder(val binding:ItemScreenDetailBinding):RecyclerView.ViewHolder(binding.root)
    private var myList: MutableList<VacancyData> = mutableListOf()
    var onItemClick: ((VacancyData)->Unit)? = null



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val binding = ItemScreenDetailBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = myList[position]

        data.let {
            val formattedDate = convertMillisToDateString(it.data)
            setGetLikeForBase(data,holder)
            holder.binding.tvData.text = formattedDate
            holder.binding.tvExperience.text = it.experience
            holder.binding.tvCompanyName.text = it.companyName
            holder.binding.tvCity.text = it.city
            holder.binding.tvWork.text = it.title

            val price = it.price
            holder.binding.tvPrice.text = "$price AZN"

            holder.binding.bDisLike.setOnClickListener{
                holder.binding.bLike.visibility = View.VISIBLE
                holder.binding.bDisLike.visibility = View.INVISIBLE
                setNewElementCortFirebase(data)

            }
            holder.binding.bLike.setOnClickListener{
                holder.binding.bLike.visibility = View.INVISIBLE
                holder.binding.bDisLike.visibility = View.VISIBLE
                deleteElementInBasket(data)

            }
        }

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(data)
        }
    }
    private fun setGetLikeForBase(data: VacancyData,holder: ViewHolder){
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
    private fun deleteElementInBasket(data:VacancyData) {
        val auth = FirebaseAuth.getInstance()
        val database = FirebaseDatabase.getInstance()

        val productIdToRemove = data.id
        val basketRef = database.reference
            .child("users")
            .child(auth.uid.toString())
            .child("basket")

        basketRef.child(productIdToRemove).removeValue()

    }
    private fun setNewElementCortFirebase(data:VacancyData) {
        val auth = FirebaseAuth.getInstance()
        val database = FirebaseDatabase.getInstance()
        val productId = data.id

        val basketRef = database.reference
            .child("users")
            .child(auth.uid.toString())
            .child("basket")

        basketRef.child(productId).setValue(data)

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