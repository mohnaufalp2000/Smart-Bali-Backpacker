package com.smart.smartbalibackpaker.guide

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.maps.android.PolyUtil
import com.smart.smartbalibackpaker.MainActivity
import com.smart.smartbalibackpaker.R
import com.smart.smartbalibackpaker.core.data.source.local.entity.GuideMapsEntity
import com.smart.smartbalibackpaker.core.data.source.remote.ConfigNetwork
import com.smart.smartbalibackpaker.core.data.source.remote.response.TrafficJamResponse
import com.smart.smartbalibackpaker.core.model.ResponseRoute
import com.smart.smartbalibackpaker.core.model.TrafficJam
import com.smart.smartbalibackpaker.core.viewmodel.ViewModelFactory
import com.smart.smartbalibackpaker.databinding.ActivityDetailGuideBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailGuideActivity : AppCompatActivity(), OnMapReadyCallback {

    private val binding by lazy {ActivityDetailGuideBinding.inflate(layoutInflater)}
    private lateinit var googleMap : GoogleMap

    private val node1LatLng= LatLng(-8.746462696144384, 115.1667655388898)
    private val node1 = "${node1LatLng.latitude},${node1LatLng.longitude}" // bandara

    private val node2LatLng = LatLng(-8.620890588193252, 115.08683681005293)
    private val node2 = "${node2LatLng.latitude},${node2LatLng.longitude}" //tanah lot

    private val node3LatLng = LatLng(-8.715293354593419, 115.16672561799477)
    private val node3 = "${node3LatLng.latitude},${node3LatLng.longitude}" //pantai kuta

    private val node4LatLng = LatLng(-8.666637900810404, 115.13934473145231)
    private val node4 = "${node4LatLng.latitude},${node4LatLng.longitude}" // finns

    private val listPlace = ArrayList<GuideMapsEntity>()
    private val listPlaceLatLng = ArrayList<LatLng>()
    private val listValueRoutes = ArrayList<Int?>()
    private val listTrafficJam = ArrayList<TrafficJam>()
    private val listNode = ArrayList<GuideMapsEntity?>()
    private var distance : Int? = 0

    private val detailGuideViewModel by lazy { ViewModelProvider(this, ViewModelFactory.getInstance(this)
        ).get(DetailGuideViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.guide_map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        setupBottomSheet()
        getAllPlaces()
        calculateDistance()
//        runMapsApi(node1, node4, "$node2|$node3", object : GetData{
//            override fun onGetData(data: Int?) {
//            }
//        })
    }

    private fun getAllPlaces() {
        listPlace.apply {
            add(GuideMapsEntity(1,  placeLatLng = node1))
            add(GuideMapsEntity(2,  placeLatLng = node2))
            add(GuideMapsEntity(3,  placeLatLng = node3))
            add(GuideMapsEntity(4, placeLatLng = node4))
        }

        listPlaceLatLng.apply {
            add(node1LatLng)
            add(node2LatLng)
            add(node3LatLng)
            add(node4LatLng)
        }
    }

    private fun runMapsApi(
        origin: String,
        destination: String,
        type: Boolean = false,
        waypoints: String = "",
        getData: GetData?
        ) {

        ConfigNetwork
            .getRoutesNetwork()
            .requestRoute(
                origin,
                destination,
                waypoints,
                "AIzaSyBfUKBEp3aNBuwVER3OhK3hWBiCV0DKzgM")
            .enqueue(object: Callback<ResponseRoute> {
                override fun onResponse(
                    call: Call<ResponseRoute>,
                    response: Response<ResponseRoute>
                ) {
                    if(response.isSuccessful){
                        val direction = response.body()
                        Log.d("directions", direction?.geocodedWaypoints.toString())
                        val dataLegs = direction?.routes?.get(0)?.legs?.get(0)
                        val dist = dataLegs?.distance?.value
                        getData?.onGetData(dist)

                        if(type){
                            // draw routes
                            val polylinePoint = direction?.routes?.get(0)?.overviewPolyline?.points
                            val decodePath = PolyUtil.decode(polylinePoint)
                            googleMap.addPolyline(
                                PolylineOptions()
                                    .addAll(decodePath)
                                    .width(8f)
                                    .color((Color.argb(255, 56, 167, 252)))
                                    .geodesic(true)
                            )

                            // add marker
                            detailGuideViewModel.getNodes().observe(this@DetailGuideActivity, { nodes ->
                                if(nodes.isNotEmpty()){
                                    nodes.forEachIndexed {index, value ->
                                        var markerColor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                                        markerColor = when(index){
                                            0 -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
                                            1 -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
                                            else -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                                        }
                                        val placeLatLng = value.placeLatLng
                                        val tmpLatLng = placeLatLng.split(",").toList().map { it.toDouble() }
                                        val latLng = LatLng(tmpLatLng[0], tmpLatLng[1])
                                        latLng.let {
                                            MarkerOptions()
                                                .position(it)
                                                .icon(markerColor)
                                        }.let {
                                            googleMap.addMarker(
                                                it
                                            )
                                        }
                                    }
                                }
                            })

                            // locate camera to routes
                            val latlongBuilder = LatLngBounds.Builder()
                            latlongBuilder.apply {
                                listPlaceLatLng.forEach { value ->
                                    include(value)
                                }
                            }
                            val bounds = latlongBuilder.build()
                            val width =  resources.displayMetrics.widthPixels
                            val height =  resources.displayMetrics.heightPixels
                            val paddingMap = width * 0.2
                            val camera = CameraUpdateFactory.newLatLngBounds(
                                bounds, width, height, paddingMap.toInt()
                            )
                            googleMap.animateCamera(camera)
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseRoute>, t: Throwable) {
                    Log.d("error", t.message.toString())
                }
            })

    }

    private fun calculateDistance() {
        val node: HashMap<Any, Int?> = HashMap()
        val indexNode = ArrayList<Int>()
        val reverseIndexNode = ArrayList<Int>()
        val fixNode = ArrayList<String>()
        var idx = 0

        /**
         * Create node
         */
        listPlace.forEachIndexed { index, _ ->
            indexNode.add(index + 1)
        }

        reverseIndexNode.addAll(indexNode.asReversed())

        while (idx < indexNode.size) {

            if (indexNode.size == 0) {
                idx = indexNode.size
            }

            if (idx < indexNode.size - 1) {
                val nodeString = "${indexNode[0]}${indexNode[idx + 1]}"
                fixNode.add(nodeString)
                idx++
            } else {
                indexNode.removeAt(0)
                idx = 0
            }
        }

        idx = 0
        while (idx < reverseIndexNode.size) {

            if (reverseIndexNode.size == 0) {
                idx = reverseIndexNode.size
            }

            if (idx < reverseIndexNode.size - 1) {
                val nodeString = "${reverseIndexNode[0]}${reverseIndexNode[idx + 1]}"
                fixNode.add(nodeString)
                idx++
            } else {
                reverseIndexNode.removeAt(0)
                idx = 0
            }
        }

        val sortedFixNode = ArrayList<Int>()
        val mappedNode = fixNode.map { it.toInt() }
        sortedFixNode.addAll(mappedNode.sorted())

        /**
         * End of Create node
         */

//        FIX
        sortedFixNode.forEachIndexed() { index, _ ->
            val tmpNode = sortedFixNode[index]
            var splitNode = "$tmpNode"
            splitNode = "${splitNode[0]},${splitNode[1]}"
            val tmplsNode = splitNode.split(",").toList()
            val lsNode = tmplsNode.map { it.toInt() }
            runMapsApi(
                listPlace[(lsNode[0] - 1)].placeLatLng,
                listPlace[(lsNode[1] - 1)].placeLatLng,
                getData = object : GetData {
                    override fun onGetData(data: Int?) {
                        distance = data
                        listValueRoutes.add(distance)
                        node["${lsNode[0]}${lsNode[1]}".toInt()] = distance
                        FloydWarshall.getDistance(listValueRoutes, listPlace, node)
                        listNode.addAll(FloydWarshall.defineRoutes())

                        if (listNode.isNotEmpty() && index == sortedFixNode.size - 1) {
                            Log.d("listnodedetail", listNode.toString())
                            var placeNumber = 1

//                             traffic jam
                            listNode.forEachIndexed { index, _ ->
                                if (index < 3){
                                    getTrafficJam(
                                        listNode[index]?.placeLatLng ?: "",
                                        listNode[index+1]?.placeLatLng ?: "",
                                        object : GetTrafficJamData{
                                            override fun onGetTrafficJamData(
                                                expectedDuration: Int,
                                                durationInTraffic: Int
                                            ) {
                                                listTrafficJam.add(TrafficJam(expectedDuration, durationInTraffic))
                                                Log.d("listjam", listTrafficJam.toString())

                                                listTrafficJam.forEachIndexed { index, _ ->
                                                    val expectDuration = listTrafficJam[index].expectedDuration ?: 0
                                                    val durationTraffic = listTrafficJam[index].durationInTraffic ?: 0

                                                    binding.apply {
                                                        tvExpectedDuration.text = "${listTrafficJam[0].expectedDuration} min"
                                                        tvDurationInTraffic.text = "${listTrafficJam[0].durationInTraffic} min"
                                                    }

                                                    if((durationTraffic - expectDuration) > 60){

                                                    }
                                                }
                                            }
                                        })

                                }
                            }

                            detailGuideViewModel.getNodes().observe(this@DetailGuideActivity) { list ->

                                if (list.isEmpty()) {
                                    listNode.forEachIndexed { listIndex, _ ->
                                        if (placeNumber <= listNode.size) {
                                            listNode[listIndex]?.placeNumber = placeNumber
                                            placeNumber += 1
                                        }
                                        Log.d("angka", placeNumber.toString())
                                        listNode[listIndex]?.let {
                                            detailGuideViewModel.insertNode(
                                                it
                                            )
                                        }
                                    }
                                } else {
                                    val dbNodes = ArrayList<GuideMapsEntity>()
                                    dbNodes.addAll(list)

                                    val result =
                                        dbNodes.sortedBy { model: GuideMapsEntity -> model.placeNumber }

                                    Log.d("detaillist", result.toString())

                                    runMapsApi(
                                        result[0].placeLatLng,
                                        result[1].placeLatLng,
                                        true,
                                        getData = null
                                    )
                                }

                                binding.btnStartNavigationVac.setOnClickListener {
                                    if (list.size < 3) {
                                        detailGuideViewModel.deleteNodes()
                                        finishAffinity()
                                        startActivity(
                                            Intent(
                                                this@DetailGuideActivity,
                                                MainActivity::class.java
                                            )
                                        )
                                    } else {
//                                        turnByTurn(dbNodes[1].placeLatLng)
                                        startActivity(
                                            Intent(
                                                this@DetailGuideActivity,
                                                TimerGuideActivity::class.java
                                            )
                                        )
                                    }
                                }

                            }
                            //            var waypointsStr = ""
                            //            listNode.forEachIndexed { index, _ ->
                            //                if (index > 0 && index < listNode.size - 1) {
                            //                    waypointsStr += if (index == listNode.size - 2) {
                            //                        listNode[index]?.placeLatLng
                            //                    } else {
                            //                        "${listNode[index]?.placeLatLng}|"
                            //                    }
                            //                }
                            //            }
                        }
                    }
                })
        }
    }

    private fun turnByTurn(destination: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=$destination&mode=l"))
        intent.setPackage("com.google.android.apps.maps")

        if(intent.resolveActivity(packageManager) != null){
            startActivity(intent)
        }
    }

    private fun getTrafficJam(origin: String, destination: String, getTrafficJamData: GetTrafficJamData){
        ConfigNetwork.getTrafficJamNetwork().getTrafficJam(origin, destination, key = "AIzaSyBfUKBEp3aNBuwVER3OhK3hWBiCV0DKzgM").enqueue(object : Callback<TrafficJamResponse>{
            override fun onResponse(
                call: Call<TrafficJamResponse>,
                response: Response<TrafficJamResponse>
            ) {
                if (response.isSuccessful) {
                    val body = response.body()
                    Log.d("responsejam", body.toString())
                    val data = body?.rows?.get(0)?.elements
                    val expectedMin = data?.get(0)?.duration?.text
                    val durationMin = data?.get(0)?.durationInTraffic?.text
                    val expectedDuration = "${expectedMin?.get(0)}${expectedMin?.get(1)}".toInt()
                    val durationInTraffic = "${durationMin?.get(0)}${durationMin?.get(1)}".toInt()

                    getTrafficJamData.onGetTrafficJamData(expectedDuration, durationInTraffic)
                }
            }

            override fun onFailure(call: Call<TrafficJamResponse>, t: Throwable) {
            }
        })
    }

    private fun setupBottomSheet(){
        BottomSheetBehavior.from(binding.btmSheetCurrentVac).apply {
            peekHeight=800
            this.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    override fun onMapReady(gMap: GoogleMap) {
            googleMap = gMap
    }
}