package com.smart.smartbalibackpaker.core.data.source.local

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.smart.smartbalibackpaker.core.data.source.local.entity.TourismDataEntity
import com.smart.smartbalibackpaker.core.data.source.local.room.TourismDao

class LocalDataSource private constructor(private val movieDao: TourismDao) {

    fun insertPlace(place: List<TourismDataEntity>) {
        movieDao.insertPlace(place)
    }

    fun updatePlace(place: TourismDataEntity) {
        movieDao.updatePlace(place)
    }

    fun getPlaces(placeType: String): DataSource.Factory<Int, TourismDataEntity> =
        movieDao.getPlace(placeType)

    fun getDetailPlace(placeId: Int): LiveData<TourismDataEntity> =
        movieDao.getDetailPlace(placeId)

    fun setPlaceFavorite(place: TourismDataEntity, newState: Boolean){
        place.isFavorite = newState
        movieDao.updatePlace(place)
    }

    fun getPlacesFavorite() : LiveData<List<TourismDataEntity>> = movieDao.getFavoritePlace()

    companion object {
        private var INSTANCE: LocalDataSource? = null

        fun getInstance(tourismDao: TourismDao): LocalDataSource =
            INSTANCE ?: LocalDataSource(tourismDao)
    }
}