package com.smart.smartbalibackpaker.dashboard

import androidx.fragment.app.*
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.smart.smartbalibackpaker.HotelVillaFragment

class DashboardPlaceAdapter(fragment: FragmentActivity) : FragmentStateAdapter(fragment) {

    private val fragment = arrayOf(
        AllPlaceFragment(),
        TouristPlaceFragment(),
        HotelVillaFragment(),
        WorshipPlaceFragment()
    )

    override fun getItemCount(): Int = fragment.size

    override fun createFragment(position: Int): Fragment = fragment[position]
//    override fun getCount(): Int = fragment.size
//
//    override fun getItem(position: Int): Fragment = fragment[position]
}