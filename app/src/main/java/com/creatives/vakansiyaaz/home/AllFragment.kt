package com.creatives.vakansiyaaz.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.viewpager2.widget.ViewPager2
import com.creatives.vakansiyaaz.R
import com.creatives.vakansiyaaz.databinding.FragmentAllBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class AllFragment : Fragment() {
    private lateinit var binding:FragmentAllBinding
    private val tabTextList = listOf("Profile", "Search")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pagerAdapter = ViewPagerAdapter(this)
        binding.viewPager01.adapter = pagerAdapter

        binding.viewPager01.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            var currentState = 0
            var currentPos = 0

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                if (currentState == ViewPager2.SCROLL_STATE_DRAGGING && currentPos == position) {
                    if (currentPos == 0) binding.viewPager01.currentItem = 2
                    else if (currentPos == 2) binding.viewPager01.currentItem = 0
                }
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                currentPos = position
                super.onPageSelected(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                currentState = state
                super.onPageScrollStateChanged(state)
            }
        })


        TabLayoutMediator(binding.Tab, binding.viewPager01) { tab, position ->
            when (position) {
                0 -> tab.text = "Вакансии"
                1 -> tab.text = "VIP Вакансии"

            }
        }.attach()
    }
    private fun setTabItemMargin(tabLayout: TabLayout, marginEnd: Int = 20) {
        for(i in tabTextList.indices) {
            val tabs = tabLayout.getChildAt(0) as ViewGroup
            for(i in 0 until tabs.childCount) {
                val tab = tabs.getChildAt(i)
                val lp = tab.layoutParams as LinearLayout.LayoutParams
                lp.marginEnd = marginEnd
                tab.layoutParams = lp
                tabLayout.requestLayout()
            }
        }
    }



}