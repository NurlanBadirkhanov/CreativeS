package com.creatives.vakansiyaaz.home.adapter

import android.content.Context
import com.creatives.vakansiyaaz.R

data class VacancyData(
    val id: String = "",
    val uniqueId: String = "",
    val work: String = "",
    val price: String = "",
    val city: String = "",
    val desc: String = "",
    val name: String = "",
    val number: String = "",
    val gmail: String = "",
    val experience: String = "",
    val title: String = "",
    val link: String = "",
    val data: Long = 0,
    val companyName: String = "",
    val verification: Boolean = false,
    val sphera: String = "",
    val education: String = "",
    val timeWork: String = "",
    val twoDaysInMillis: Long = 0,
    val weekInMillis: Long = 0,
    val proAndLink: String = "",
    val idElement: Int = 1,
) : java.io.Serializable {
    constructor(context: Context) : this(
        price = context.getString(R.string.price_text_isEmptv)
    )
}

