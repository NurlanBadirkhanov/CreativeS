package com.creatives.vakansiyaaz.Profile.ItemProfile.MyAdsItem

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class MyAdsViewPager(fa: AllMyAdsFragment) : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        // Возвращаем фрагменты в зависимости от позиции
        return when (position) {
            0 -> MyAdsItemFragment()
            1 -> MyAdsVipItemFragment()
            else -> MyAdsItemFragment()
        }
    }
}
