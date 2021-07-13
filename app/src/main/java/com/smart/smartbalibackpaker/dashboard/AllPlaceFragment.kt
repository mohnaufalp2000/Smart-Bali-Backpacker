package com.smart.smartbalibackpaker.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smart.smartbalibackpaker.databinding.FragmentAllPlaceBinding

class AllPlaceFragment : Fragment() {

    private var _binding: FragmentAllPlaceBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAllPlaceBinding.inflate(inflater, container, false)
        return binding?.root
//        return inflater.inflate(R.layout.fragment_hotel_villa, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.sampleTv?.text = "This is AllPlaces Fragment"
    }

}