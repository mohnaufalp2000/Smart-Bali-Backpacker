package com.smart.smartbalibackpaker.core.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ConfigNetwork {
    companion object {
        fun getRetrofit(): TourismService {
            val retrofit = Retrofit.Builder()
                .baseUrl("http://smart-balibackpacker.com/data/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(TourismService::class.java)
        }
    }
}