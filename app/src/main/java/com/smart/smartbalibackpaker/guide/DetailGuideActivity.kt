package com.smart.smartbalibackpaker.guide

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.maps.android.PolyUtil
import com.smart.smartbalibackpaker.BuildConfig
import com.smart.smartbalibackpaker.R
import com.smart.smartbalibackpaker.core.data.source.local.entity.GuideMapsEntity
import com.smart.smartbalibackpaker.core.data.source.remote.ConfigNetwork
import com.smart.smartbalibackpaker.core.model.DetailPlaceGuide
import com.smart.smartbalibackpaker.core.model.ResponseRoute
import com.smart.smartbalibackpaker.core.model.TrafficJam
import com.smart.smartbalibackpaker.core.preferences.GuidePreferences
import com.smart.smartbalibackpaker.core.viewmodel.ViewModelFactory
import com.smart.smartbalibackpaker.dashboard.DetailPlaceViewModel
import com.smart.smartbalibackpaker.databinding.ActivityDetailGuideBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class DetailGuideActivity : AppCompatActivity(), OnMapReadyCallback {

    private val binding by lazy {ActivityDetailGuideBinding.inflate(layoutInflater)}
    private lateinit var googleMap : GoogleMap

    private val bandaraLatLng= LatLng(-8.746462696144384, 115.1667655388898)
    private val bandara = "${bandaraLatLng.latitude},${bandaraLatLng.longitude}" // bandara

    private val node2LatLng = LatLng(-8.620890588193252, 115.08683681005293)
    private val node2 = "${node2LatLng.latitude},${node2LatLng.longitude}" //tanah lot

    private val node3LatLng = LatLng(-8.715293354593419, 115.16672561799477)
    private val node3 = "${node3LatLng.latitude},${node3LatLng.longitude}" //pantai kuta

    private val node5LatLng = LatLng(-8.702983425102133, 115.1798332582021)
    private val node5 = "${node5LatLng.latitude},${node5LatLng.longitude}" // krisna

    private val node4LatLng = LatLng(-8.666637900810404, 115.13934473145231)
    private val node4 = "${node4LatLng.latitude},${node4LatLng.longitude}" // finns

    private lateinit var preferences : GuidePreferences
    private val listPlace = ArrayList<GuideMapsEntity>()
    private val listPlaceLatLng = ArrayList<LatLng>()
    private val listValueRoutes = ArrayList<Int?>()
    private val listTrafficJam = ArrayList<TrafficJam>()
    private val listShortestPath = ArrayList<GuideMapsEntity?>()
    private var trafficJam = TrafficJam()
    private val testList = ArrayList<DetailPlaceGuide?>()
    private var idPerjalanan = 0
    private var idVacation : List<String>? = null
    private var idPlace = 0

    private val detailGuideViewModel by lazy { ViewModelProvider(this, ViewModelFactory.getInstance(this)
        ).get(DetailGuideViewModel::class.java)
    }
    private val recordGuideViewModel by lazy { ViewModelProvider(this, ViewModelFactory.getInstance(this)
        ).get(RecordGuideViewModel::class.java)
    }
    private val detailPlaceViewModel by lazy { ViewModelProvider(this, ViewModelFactory.getInstance(this)
        ).get(DetailPlaceViewModel::class.java)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        preferences = GuidePreferences(this)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.guide_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        setupBottomSheet()
//        getAllPlaces()
        verifyData()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun verifyData() {
        detailGuideViewModel.getNodes().observe(this) { result ->
//            offlineGetAllPlaces()
            getTrafficJam()
            val isOpen = getOpenHoursPlace()
            if (result.isEmpty()) {
                getAllPlaces()
            } else {
                val listRoute = ArrayList<GuideMapsEntity?>()
                listRoute.addAll(result)

                listRoute.forEachIndexed { index, _ ->
                    val latlng = listRoute[index]?.placeLatLng?.split(",")?.toList()
                    val lat = latlng?.get(0)
                    val lng = latlng?.get(1)

                    listPlaceLatLng.add(
                        index,
                        LatLng(
                            lat?.toDouble() ?: 0.0,
                            lng?.toDouble() ?: 0.0))
                }

                idPlace = listRoute[1]?.idPlace ?: 0
                populateData(listRoute[1]?.idPlace ?: 0)

                // cek buka/tutup, lalu cek kemacetan
                if(isOpen){
                    if(listRoute.size > 2){
                        checkTrafficJamRoute(listRoute)
                    }
                }
                Log.d("dbresult", result.toString())
                runMapsApi(
                    result[0].placeLatLng,
                    result[1].placeLatLng,
                    true,
                    getData = null
                )
                startNavigation(result[1].placeLatLng)
            }
        }
    }

    private fun getOpenHoursPlace() : Boolean {
        var isOpen = true
        val timeInMillis = System.currentTimeMillis()
        val call = Calendar.getInstance()
        call.timeInMillis = timeInMillis

//        val dateFormat = SimpleDateFormat("dd/MM/yyyy hh:mm:ss a")
        val dateFormat = SimpleDateFormat("HH:mm")
        val hours = dateFormat.format(call.time)

        Log.d("jam", hours.toString())

        val open = "07:00"
        val close = "21:00"

        if(hours < open || hours > close){
            isOpen = false
            binding.btnStartNavigationVac.visibility = View.GONE

            val dialog = AlertDialog.Builder(this@DetailGuideActivity)
            dialog.apply {
                setTitle("Tutup")
                setMessage("Tempat wisata sedang tutup")
                setPositiveButton("Tutup"){ dialogInterface, _ ->
                    dialogInterface.dismiss()
                }
            }
            dialog.show()
        }
        return isOpen
    }

    private fun getTrafficJam(){
        listTrafficJam.apply {
            add(TrafficJam("1 hours 5 min", "1 hours 30 min", 35))
            add(TrafficJam("1 hours", "1 hours 60 min", 60))
            add(TrafficJam("50 min", "60 min", 10))
            add(TrafficJam("50 min", "60 min", 10))
        }

        trafficJam = listTrafficJam.random()
    }

    private fun startNavigation(destination: String) {
        binding.btnStartNavigationVac.setOnClickListener {
            val dialog = AlertDialog.Builder(this@DetailGuideActivity)
            dialog.apply{
                setTitle(resources.getString(R.string.attention))
                setMessage(resources.getString(R.string.arrived))
                setNegativeButton(resources.getString(R.string.yes)) { dialogInterface, i ->
                    dialogInterface.dismiss()
                    turnByTurn(destination)
                }
                setPositiveButton(resources.getString(R.string.no)) { dialogInterface, i ->
                    dialogInterface.dismiss()
                    val intent = Intent(this@DetailGuideActivity, TimerGuideActivity::class.java)
                    intent.putExtra(TimerGuideActivity.EXTRA_ID, idPerjalanan)
                    intent.putExtra(TimerGuideActivity.EXTRA_PLACE, idPlace)
                    startActivity(intent)
                }
            }.show()
        }
//        if(preferences.getState("navigation").equals("1")){
//            preferences.setState("navigation", "0")
//            val intent = Intent(this@DetailGuideActivity, TimerGuideActivity::class.java)
//            intent.putExtra(TimerGuideActivity.EXTRA_ID, idPerjalanan)
//            intent.putExtra(TimerGuideActivity.EXTRA_PLACE, idPlace)
//            startActivity(
//                intent
//            )
//        }
//
//        binding.btnStartNavigationVac.setOnClickListener {
//            if(preferences.getState("navigation").equals("1")){
//                preferences.setState("navigation", "0")
//                val intent = Intent(this@DetailGuideActivity, TimerGuideActivity::class.java)
//                intent.putExtra(TimerGuideActivity.EXTRA_ID, idPerjalanan)
//                intent.putExtra(TimerGuideActivity.EXTRA_PLACE, idPlace)
//                startActivity(
//                    intent
//                )
//            } else {
//                preferences.setState("navigation", "1")
//                turnByTurn(destination)
//            }
//            // }
//        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun offlineGetAllPlaces() {
        listPlace.apply {
            add(GuideMapsEntity(1,  placeLatLng = bandara))
            add(GuideMapsEntity(2,  placeLatLng = node2))
            add(GuideMapsEntity(3,  placeLatLng = node3))
            add(GuideMapsEntity(4, placeLatLng = node4))
            add(GuideMapsEntity(5, placeLatLng = node5))
        }
        listPlaceLatLng.apply {
            add(bandaraLatLng)
            add(node2LatLng)
            add(node3LatLng)
            add(node4LatLng)
            add(node5LatLng)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getAllPlaces() {
        idPerjalanan = intent.getIntExtra(EXTRA_ID_TOUR, 0)
        Log.d("idPerjalanku", idPerjalanan.toString())

        recordGuideViewModel.getDetailRecordVacation(idPerjalanan).observe(this){ record ->
            idVacation = convertStringToList(record.idTempatWisata)
            val idVacationCount = idVacation?.size
            Log.d("idvacation", idVacation.toString())

            idVacation?.forEachIndexed { index, id ->
                Log.d("capek", id)
//                detailGuideViewModel.setSelectedPlace(id.toInt())
                detailGuideViewModel.getDetailPlace(id.toInt()).observe(this){ detail ->
                    if (detail != null){
                        Log.d("hasilrecord", detail.toString())
                        val lat = detail.latitude
                        val lng = detail.longtitude

                        addDataToList(index, detail.title, lat, lng, idVacationCount, detail.id ?: 0)

                        Log.d("hasiltestlist", testList.toString())
                    }
                }
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun populateData(id: Int?) {
        Log.d("populateid", id.toString())
        binding.tvExpectedDuration.text = trafficJam.expectedDuration
        binding.tvDurationInTraffic.text = trafficJam.durationInTraffic
        detailGuideViewModel.getDetailPlace(id ?: 0).observe(this){
            binding.apply {
                    tvTitle.text = it.title
                    tvAddress.text = it.address
                }
                Glide.with(this@DetailGuideActivity)
                    .load("https://balibackpacker.co.id/storage/public/pictures/thumbnail/${it.thumbnail}")
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .apply(RequestOptions().centerCrop())
                    .into(binding.imgPlace)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun addDataToList(index: Int, name: String?, lat: String?, lng: String?, count: Int?, idPlace: Int) {
        listPlace.apply {
            add(
                0,
                GuideMapsEntity(
                    1,
                    namePlace = "bandara",
                    placeLatLng = bandara,
                    idPlace = 0,
                    ))
            add(
                GuideMapsEntity(
                    (index+2),
                    namePlace = name ?: "",
                    placeLatLng = "${lat},${lng}",
                    idPlace = idPlace,))
        }

        listPlace.sortedBy { model: GuideMapsEntity -> model.id}

        listPlaceLatLng.apply {
            add(0, LatLng(bandaraLatLng.latitude, bandaraLatLng.longitude))
            add(LatLng(lat?.toDouble() ?: 0.0, lng?.toDouble() ?: 0.0))
        }
        if((listPlace.size - 1 == count) && (listPlaceLatLng.size - 1 == count)){
            Log.d("hasillistplace1", listPlace.toString())
            createNode()
        }
    }

    private fun convertStringToList(idTempatWisata: String?) : List<String>? {
        var listIdTempatWisata = idTempatWisata
        if (listIdTempatWisata != null) {
            listIdTempatWisata = listIdTempatWisata
                .replace("[", "")
                .replace("]", "")
                .replace(" ", "")
                .trim()
        }
        val tmpListIdTempatWisata = listIdTempatWisata?.split(",")?.toList()
        Log.d("pulang", tmpListIdTempatWisata.toString())
        return tmpListIdTempatWisata
    }

    private fun runMapsApi(
        origin: String,
        destination: String,
        type: Boolean = false,
        getData: GetData?
        ) {
        ConfigNetwork
            .getRoutesNetwork()
            .requestRoute(
                origin,
                destination,
                BuildConfig.GOOGLE_MAPS_API_KEY)
            .enqueue(object: Callback<ResponseRoute> {
                override fun onResponse(
                    call: Call<ResponseRoute>,
                    response: Response<ResponseRoute>
                ) {
                    if(response.isSuccessful){
                        val direction = response.body()
                        Log.d("direct", direction.toString())

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
                            detailGuideViewModel.getNodes().observe(this@DetailGuideActivity) { nodes ->
                                if (nodes.isNotEmpty()) {
                                    nodes.forEachIndexed { index, value ->
                                        var markerColor = BitmapDescriptorFactory.defaultMarker(
                                            BitmapDescriptorFactory.HUE_RED
                                        )
                                        markerColor = when (index) {
                                            0 -> BitmapDescriptorFactory.defaultMarker(
                                                BitmapDescriptorFactory.HUE_AZURE
                                            )
                                            1 -> BitmapDescriptorFactory.defaultMarker(
                                                BitmapDescriptorFactory.HUE_GREEN
                                            )
                                            else -> BitmapDescriptorFactory.defaultMarker(
                                                BitmapDescriptorFactory.HUE_RED
                                            )
                                        }
                                        val placeLatLng = value.placeLatLng
                                        val tmpLatLng =
                                            placeLatLng.split(",").toList().map { it.toDouble() }
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
                            }

                            // locate camera to  two routes
                            val latlongBuilder = LatLngBounds.Builder()
//                            }
                            latlongBuilder.apply {
                                include(listPlaceLatLng[0])
                                include(listPlaceLatLng[1])
                            }

                            val bounds = latlongBuilder.build()
                            val width =  resources.displayMetrics.widthPixels
                            val height =  resources.displayMetrics.heightPixels * 0.5
                            val paddingMap = width * 0.2
                            val camera = CameraUpdateFactory.newLatLngBounds(
                                bounds, width, height.toInt(), paddingMap.toInt()
                            )
                            googleMap.moveCamera(camera)
                        } else {
                            // insert distance value to node
                            val dataLegs = direction?.routes?.get(0)?.legs?.get(0)
                            val dist = dataLegs?.distance?.value
                            getData?.onGetData(dist)
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseRoute>, t: Throwable) {
                    Log.d("error", t.message.toString())
                }
            })

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun createNode(){
        val indexNode = ArrayList<Int>()
        val reverseIndexNode = ArrayList<Int>()
        val fixNode = ArrayList<String>()
        var idx = 0

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
        insertDistanceToNode(sortedFixNode)
    }

    private fun splitNode(node: Int): List<Int> {
        var splitNode = "$node"
        splitNode = "${splitNode[0]},${splitNode[1]}"
        val tmplsNode = splitNode.split(",").toList()
        return tmplsNode.map { it.toInt() }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun insertDistanceToNode(listNode : ArrayList<Int>) {
        val nodeWithDistance : HashMap<Any, Int?> = HashMap()
        var nodeArray : Array<List<Int>> = arrayOf()
        Log.d("listnode", listNode.toString())
        // add node to array
        listNode.forEachIndexed { index, _ ->
            val node = splitNode(listNode[index])
            nodeArray = nodeArray.plusElement(node)
        }
        Log.d("nodearray", nodeArray.contentDeepToString())
        Log.d("nodearraysize", nodeArray.size.toString())
        // insert distance to node
        nodeArray.forEachIndexed { index, _ ->
            val nodeOrigin = nodeArray[index][0]
            val nodeDestination = nodeArray[index][1]
            Log.d("nodearrayindex", "${nodeOrigin},${nodeDestination}")

            runMapsApi(
                listPlace[(nodeOrigin - 1)].placeLatLng,
                listPlace[(nodeDestination - 1)].placeLatLng,
                false,
                object : GetData {
                    override fun onGetData(data: Int?) {
                        nodeWithDistance["${nodeOrigin}${nodeDestination}".toInt()] = data
                        if(nodeWithDistance.size == nodeArray.size){
                            Log.d("nodewithdistance", nodeWithDistance.toString())
                            Log.d("nodewithdistancesize", nodeWithDistance.size.toString())
                            drawShortestRoute(nodeWithDistance)
                        }
                    }
                }
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun drawShortestRoute(nodeWithDistance: HashMap<Any, Int?>) {
        ShortestPath.getDistances(listPlace, nodeWithDistance)
        listShortestPath.clear()
        listShortestPath.addAll(ShortestPath.getShortestRoute())

        Log.d("internetresult", listShortestPath.toString())

        listShortestPath.forEachIndexed { index, _ ->
            listShortestPath[index]?.let { detailGuideViewModel.insertNode(it) }
        }

        populateData(listShortestPath[1]?.idPlace ?: 0)
    }

    private fun checkTrafficJamRoute(list: ArrayList<GuideMapsEntity?>) : ArrayList<GuideMapsEntity?>{
        val tmpList = ArrayList<GuideMapsEntity?>()
        val slice = trafficJam.slice
        Log.d("slice", slice.toString())

        if(slice >= 60){
            val dialog = AlertDialog.Builder(this@DetailGuideActivity)
            dialog.apply{
                setTitle(resources.getString(R.string.trafficjam))
                setMessage(resources.getString(R.string.trafficjamdesc))
                setNegativeButton(resources.getString(R.string.no)) { dialogInterface, i ->
                    dialogInterface.dismiss()
                }
                setPositiveButton(resources.getString(R.string.yes)) { dialogInterface, i ->
                    tmpList.addAll(list)
                    val tmp = tmpList[1]?.placeNumber
                    tmpList[1]?.placeNumber = tmpList[2]?.placeNumber ?: 0
                    tmpList[2]?.placeNumber = tmp ?: 0
                    detailGuideViewModel.deleteNodes()
                    tmpList.forEachIndexed { index, _ ->
                        tmpList[index]?.let {
                            detailGuideViewModel.insertNode(it)
                        }
                    }
                    dialogInterface.dismiss()
                    startActivity(Intent(this@DetailGuideActivity, DetailGuideActivity::class.java))
                    finish()
                }
            }.show()
        }
        Log.d("trafficlist", tmpList.toString())
        return tmpList
    }

    private fun turnByTurn(destination: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=$destination&mode=l"))
        intent.setPackage("com.google.android.apps.maps")

        if(intent.resolveActivity(packageManager) != null){
            startActivity(intent)
        }
    }

    private fun setupBottomSheet(){
        BottomSheetBehavior.from(binding.btmSheetCurrentVac).apply {
            peekHeight=1000
            this.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    override fun onMapReady(gMap: GoogleMap) {
            googleMap = gMap
    }

    companion object{
        const val EXTRA_ID_BACKPAKER = "extra_id_backpacker"
        const val EXTRA_ID_TOUR = "extra_id_tour"
        const val EXTRA_DETAIL_PLACE = "extra_detail_place"
    }
}