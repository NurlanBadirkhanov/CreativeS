package com.creatives.vakansiyaaz.Profile.ItemProfile.MyAdsItem

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.creatives.vakansiyaaz.databinding.HomeItemBinding
import com.creatives.vakansiyaaz.home.adapter.VacancyData
import java.text.SimpleDateFormat
import java.util.*

class MyAdsAdapter(private val context: Context): RecyclerView.Adapter<MyAdsAdapter.ViewHolder>() {
    class ViewHolder(val binding: HomeItemBinding): RecyclerView.ViewHolder(binding.root)
    private var myList: MutableList<VacancyData> = mutableListOf()
    var onItemClick: ((VacancyData)->Unit)? = null



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = HomeItemBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = myList[position]

        data.let {
            val formattedDate = convertMillisToDateString(it.data)
            holder.binding.tvData.text = formattedDate
            holder.binding.tvCompanyName.text = it.companyName
            val price = it.price
            holder.binding.tvPrice.text = "$price AZN"
        }

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(data)
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