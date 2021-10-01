package com.smart.smartbalibackpaker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.smart.smartbalibackpaker.databinding.FragmentGuideBinding

class GuideFragment : Fragment(), OnMapReadyCallback {

    private var binding : FragmentGuideBinding? = null
    var mapFragment : SupportMapFragment? = null
    lateinit var googleMap : GoogleMap


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentGuideBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapFragment = fragmentManager?.findFragmentById(R.id.guide_maps) as SupportMapFragment?

    }

    override fun onMapReady(gMap: GoogleMap) {
        googleMap = gMap
        googleMap.setOnMapClickListener(object : GoogleMap.OnMapClickListener{
            override fun onMapClick(latLng: LatLng) {
                val marker = MarkerOptions()
                marker.position(latLng)
                googleMap.clear()
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10F))
                googleMap.addMarker(marker)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}