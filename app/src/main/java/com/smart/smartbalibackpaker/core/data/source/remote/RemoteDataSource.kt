package com.smart.smartbalibackpaker.core.data.source.remote

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.smart.smartbalibackpaker.core.data.source.remote.response.DataItem
import com.smart.smartbalibackpaker.core.data.source.remote.response.Place
import com.smart.smartbalibackpaker.core.data.source.remote.response.TourismResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RemoteDataSource {

    fun getAllTourism(placeType: String): LiveData<ApiResponse<List<DataItem>>> {
        val listPlaces = MutableLiveData<ApiResponse<List<DataItem>>>()
//        EspressoIdlingResource.increment()
        ConfigNetwork.getRetrofit().getAllTourism(placeType).enqueue(object : Callback<TourismResponse> {
            override fun onResponse(call: Call<TourismResponse>, response: Response<TourismResponse>) {
                if (response.isSuccessful) {
                    val body = response.body()?.data
                    listPlaces.postValue(ApiResponse.success(body as List<DataItem>))
//                    EspressoIdlingResource.decrement()
                }
            }

            override fun onFailure(call: Call<TourismResponse>, t: Throwable) {
                Log.d("Response", "On Failure")
            }
        })
        return listPlaces
    }

    fun getDetailTourism(placeId: Int): LiveData<ApiResponse<Place>> {
        val listDetailTourism = MutableLiveData<ApiResponse<Place>>()
//        EspressoIdlingResource.increment()
        ConfigNetwork.getRetrofit().getDetailTourism(placeId)
            .enqueue(object : Callback<Place?> {
                override fun onResponse(
                    call: Call<Place?>, response: Response<Place?>
                ) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        listDetailTourism.postValue(ApiResponse.success(body as Place))
//                        EspressoIdlingResource.decrement()
                    }
                }

                override fun onFailure(call: Call<Place?>, t: Throwable) {
                    Log.d("Response", "On Failure")
                }
            })
        return listDetailTourism
    }

    companion object {
        @Volatile
        private var instance: RemoteDataSource? = null

        fun getInstance(): RemoteDataSource =
            instance ?: synchronized(this) {
                instance ?: RemoteDataSource().apply { instance = this }
            }
    }
}