package com.creatives.vakansiyaaz.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.creatives.vakansiyaaz.Profile.ItemProfile.MyAdsItem.MyAdsVipItemFragment

class ViewPagerAdapter(fa: AllFragment) : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        // Возвращаем фрагменты в зависимости от позиции
        return when (position) {
            0 -> HomeFragment()
            1 -> VipFragment()

            else -> MyAdsVipItemFragment()
        }
    }
}