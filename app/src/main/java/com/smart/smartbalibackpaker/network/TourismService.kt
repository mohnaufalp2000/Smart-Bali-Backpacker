package com.smart.smartbalibackpaker.network

import com.smart.smartbalibackpaker.data.source.remote.response.DetailTourismResponse
import com.smart.smartbalibackpaker.data.source.remote.response.Place
import com.smart.smartbalibackpaker.data.source.remote.response.TourismResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TourismService {

    @GET("{type_place}")
    fun getAllTourism(
        @Path("type_place") type_place: String
    ): Call<TourismResponse>

    @GET("hotel")
    fun getAllHotel(): Call<TourismResponse>

    @GET("tempat-ibadah")
    fun getAllWorship(): Call<TourismResponse>

    @GET("place/{id_place}")
    fun getDetailTourism(
        @Path("id_place") id_place: Int
    ): Call<Place>

}