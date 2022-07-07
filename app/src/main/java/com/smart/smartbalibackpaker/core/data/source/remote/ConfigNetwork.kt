package com.smart.smartbalibackpaker.core.data.source.remote

import com.smart.smartbalibackpaker.core.data.source.remote.service.PlaceService
import com.smart.smartbalibackpaker.core.data.source.remote.service.RouteService
import com.smart.smartbalibackpaker.core.data.source.remote.service.TourismService
import com.smart.smartbalibackpaker.core.utils.Constant.GOOGLE_MAPS_PLACE_URL
import com.smart.smartbalibackpaker.core.utils.Constant.GOOGLE_MAPS_URL
import com.smart.smartbalibackpaker.core.utils.Constant.TOURISM_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ConfigNetwork {
    companion object {
        fun getRetrofit(): TourismService {
            val retrofit = Retrofit.Builder()
                .baseUrl(TOURISM_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(TourismService::class.java)
        }

        fun getRoutesNetwork(): RouteService {
            val retrofit = Retrofit.Builder()
                .baseUrl(GOOGLE_MAPS_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(RouteService::class.java)
        }

        fun getPlacesNetwork(): PlaceService {
            val retrofit = Retrofit.Builder()
                .baseUrl(GOOGLE_MAPS_PLACE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(PlaceService::class.java)
        }
    }
}