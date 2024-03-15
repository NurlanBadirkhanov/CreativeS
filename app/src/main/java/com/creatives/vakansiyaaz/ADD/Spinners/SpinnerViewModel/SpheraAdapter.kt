package com.creatives.vakansiyaaz.ADD.Spinners.SpinnerViewModel

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.creatives.vakansiyaaz.databinding.ItemSpheraBinding

class SpheraAdapter(val context: Context) : RecyclerView.Adapter<SpheraAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemSpheraBinding) : RecyclerView.ViewHolder(binding.root)

    private var originalList: MutableList<String> = mutableListOf()
    private var filteredList: MutableList<String> = mutableListOf()
    var onItemClick: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSpheraBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        filteredList[position].let {
            holder.binding.tvElements.text = it
        }
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(filteredList[position])
        }
    }

    fun setData(data: List<String>) {
        originalList.clear()
        originalList.addAll(data)
        filterList("") // Фильтруем с пустым запросом для отображения всех элементов
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    fun filterList(query: String) {
        filteredList.clear()
        filteredList.addAll(
            originalList.filter {
                it.contains(query, ignoreCase = true)
            }
        )
        notifyDataSetChanged()
    }
}
