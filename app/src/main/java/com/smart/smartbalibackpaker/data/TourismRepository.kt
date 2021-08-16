package com.smart.smartbalibackpaker.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.smart.smartbalibackpaker.data.source.local.LocalDataSource
import com.smart.smartbalibackpaker.data.source.local.entity.TourismDataEntity
import com.smart.smartbalibackpaker.data.source.remote.ApiResponse
import com.smart.smartbalibackpaker.data.source.remote.RemoteDataSource
import com.smart.smartbalibackpaker.data.source.remote.response.DataItem
import com.smart.smartbalibackpaker.data.source.remote.response.DetailTourismResponse
import com.smart.smartbalibackpaker.data.source.remote.response.Place
import com.smart.smartbalibackpaker.utils.AppExecutors
import com.smart.smartbalibackpaker.vo.Resource

class TourismRepository private constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
) :
    TourismDataSource {

    override fun getAllTourism(placeType: String): LiveData<Resource<PagedList<TourismDataEntity>>> {
        return object :
            NetworkBoundResource<PagedList<TourismDataEntity>, List<DataItem>>(appExecutors) {
            override fun loadFromDB(): LiveData<PagedList<TourismDataEntity>> {
                val config = PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setInitialLoadSizeHint(10)
                    .setPageSize(10)
                    .build()
                return LivePagedListBuilder(localDataSource.getPlaces(placeType), config).build()
            }

            override fun shouldFetch(data: PagedList<TourismDataEntity>?): Boolean =
//                data == null || data.isEmpty()
                true
            override fun createCall(): LiveData<ApiResponse<List<DataItem>>> {
                return when (placeType) {
                    "tour" -> {
                        remoteDataSource.getAllTourism("wisata")
                    }
                    "hotel" -> {
                        remoteDataSource.getAllTourism("hotel")
                    }
                    "worship" -> {
                        remoteDataSource.getAllTourism("tempat-ibadah")
                    }
                    //prepare for getALlWisata
                    else -> remoteDataSource.getAllTourism("wisata")
                }
            }

            override fun saveCallResult(data: List<DataItem>) {
                val placeList = ArrayList<TourismDataEntity>()
                for (response in data) {
                    val movie = TourismDataEntity(
                        id = response.id,
                        thumbnail = response.thumbnail,
                        address = response.address,
                        latitude = response.latitude,
                        longtitude = response.longtitude,
                        title = response.title,
                        type = response.type,
                        slug = response.slug,
                        desc = response.desc
                    )
                    placeList.add(movie)
                }
                localDataSource.insertPlace(placeList)
            }
        }.asLiveData()
    }

    override fun getDetailTourism(placeId: Int): LiveData<Resource<TourismDataEntity>> {
        return object : NetworkBoundResource<TourismDataEntity, Place>(appExecutors) {
            override fun loadFromDB(): LiveData<TourismDataEntity> =
                localDataSource.getDetailPlace(placeId)

            override fun shouldFetch(data: TourismDataEntity?): Boolean =
                data?.title.isNullOrEmpty()

            override fun createCall(): LiveData<ApiResponse<Place>> =
                remoteDataSource.getDetailTourism(placeId)

            override fun saveCallResult(data: Place) {
                val tourismDetail = TourismDataEntity(
                    id = data.id,
                    thumbnail = data.thumbnail,
                    address = data.address,
                    latitude = data.latitude,
                    longtitude = data.longtitude,
                    title = data.title,
                    type = data.type,
                    slug = data.slug,
                    desc = data.desc
                )
                localDataSource.updatePlace(tourismDetail)
            }
        }.asLiveData()
    }

    companion object {
        @Volatile
        private var instance: TourismRepository? = null

        fun getInstance(
            remoteData: RemoteDataSource,
            localData: LocalDataSource,
            appExecutors: AppExecutors
        ): TourismRepository =
            instance ?: synchronized(this) {
                instance ?: TourismRepository(remoteData, localData, appExecutors).apply {
                    instance = this
                }
            }
    }

}