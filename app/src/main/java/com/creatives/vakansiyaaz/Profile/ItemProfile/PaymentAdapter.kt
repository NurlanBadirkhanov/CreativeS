package com.creatives.vakansiyaaz.Profile.ItemProfile

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.creatives.vakansiyaaz.databinding.PaymentItemListBinding
import com.creatives.vakansiyaaz.home.adapter.VacancyData
import java.text.SimpleDateFormat
import java.util.*

class PaymentAdapter(val context: Context) : RecyclerView.Adapter<PaymentAdapter.PaymentHolder>() {

    class PaymentHolder(val binding: PaymentItemListBinding) : RecyclerView.ViewHolder(binding.root)

    private var paymentList: MutableList<VacancyData> = mutableListOf()

    fun setData(data: List<VacancyData>) {
        paymentList.clear()
        paymentList.addAll(data)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PaymentHolder {
        val binding = PaymentItemListBinding.inflate(LayoutInflater.from(context), parent, false)
        return PaymentHolder(binding)
    }

    override fun onBindViewHolder(holder: PaymentHolder, position: Int) {
        paymentList[position].let {
            holder.binding.textView29.text = it.price.toString()

            val formattedDate = convertMillisToDateString(it.data)
            holder.binding.tvDatas.text = formattedDate
        }
    }


    override fun getItemCount(): Int {
        return paymentList.size
    }

    private fun convertMillisToDateString(millis: Long): String {
        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return dateFormat.format(Date(millis))
    }
}