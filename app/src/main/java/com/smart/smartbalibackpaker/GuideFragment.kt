package com.smart.smartbalibackpaker

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.smart.smartbalibackpaker.databinding.FragmentGuideBinding

import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import com.smart.smartbalibackpaker.core.model.ResponseRoute
import com.smart.smartbalibackpaker.core.data.source.remote.ConfigNetwork
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GuideFragment : Fragment(), OnMapReadyCallback {

    private var binding: FragmentGuideBinding? = null
    private val apiKey = "AIzaSyBKQuKAz4cstvm-nNmnYwYJEmSSWEzCxmU"
    private val origin = LatLng(-6.175110, 106.865039) // Jakarta
    private val destination = LatLng(-6.197301,106.795951); // Cirebon
    private lateinit var googleMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGuideBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
        actionRoute()
    }

    private fun actionRoute() {
        val startLocation = "${origin.latitude}, ${origin.longitude}"
        val endLocation = "${destination.latitude}, ${destination.longitude}"
        ConfigNetwork
            .getRoutesNetwork()
            .requestRoute(startLocation, endLocation, apiKey)
            .enqueue(object: Callback<ResponseRoute>{
                override fun onResponse(
                    call: Call<ResponseRoute>,
                    response: Response<ResponseRoute>
                ) {
                    if(response.isSuccessful){
                        val dataDirection = response.body()
                        val dataLegs = dataDirection?.routes?.get(0)?.legs?.get(0)
                        val polylinePoint = dataDirection?.routes?.get(0)?.overviewPolyline?.points
                        val decodePath = PolyUtil.decode(polylinePoint)

                        // Gambar garis antar dua tempat di Maps
                        googleMap.apply {
                            addPolyline(PolylineOptions()
                                .addAll(decodePath)
                                .width(8.0F)
                                .color(Color.argb(255, 56, 167, 252))
                            )
                            addMarker(MarkerOptions().position(origin).title("Lokasi Awal"))
                            addMarker(MarkerOptions().position(destination).title("Lokasi Akhir"))
                        }

                        // Mengarahkan layar ke jalur dari tempat awal sampai tempat akhir
                        val latLongBuilder = LatLngBounds.Builder()
                        latLongBuilder.apply {
                            include(origin)
                            include(destination)
                        }
                        val bounds = latLongBuilder.build()
                        val width = resources.displayMetrics.widthPixels
                        val height = resources.displayMetrics.heightPixels
                        val paddingMap = width * 0.2
                        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, width, height, paddingMap.toInt())
                        googleMap.animateCamera(cameraUpdate)
                    }
                }

                override fun onFailure(call: Call<ResponseRoute>, t: Throwable) {
                }
            })
    }

    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}