package com.smart.smartbalibackpaker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.material.tabs.TabLayoutMediator
import com.smart.smartbalibackpaker.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_allplace,
            R.string.tab_touristplace,
            R.string.tab_hotelvilla,
            R.string.tab_worshipplace
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding?.root
//        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dashboardPlaceAdapter = activity?.let { DashboardPlaceAdapter(it) }
        binding?.apply {
            viewPager.adapter = dashboardPlaceAdapter
            TabLayoutMediator(
                layoutTabLayout,
                viewPager
            ) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()
        }

        val imageList = ArrayList<SlideModel>() // Create image list

        // imageList.add(SlideModel("String Url" or R.drawable)
        // imageList.add(SlideModel("String Url" or R.drawable, "title") You can add title
        imageList.add(
            SlideModel(
                R.drawable.onboardingone,
                "Your plan is your best choice ever",
                ScaleTypes.CENTER_CROP
            )
        )
        imageList.add(
            SlideModel(
                R.drawable.onboardingtwo,
                "Got your experience with the new friends",
                ScaleTypes.CENTER_CROP
            )
        )
        imageList.add(
            SlideModel(
                R.drawable.onboardingthree,
                "Enjoy your time everywhere you go",
                ScaleTypes.CENTER_CROP
            )
        )
        val imageSlider = binding?.imageSlider
        imageSlider?.setImageList(imageList)



    }

}