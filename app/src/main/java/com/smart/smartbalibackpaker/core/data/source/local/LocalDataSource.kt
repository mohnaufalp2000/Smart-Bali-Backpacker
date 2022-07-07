package com.smart.smartbalibackpaker.core.data.source.local

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.smart.smartbalibackpaker.core.data.source.local.entity.AccomDataEntity
import com.smart.smartbalibackpaker.core.data.source.local.entity.TourismDataEntity
import com.smart.smartbalibackpaker.core.data.source.local.entity.UploadResultEntity
import com.smart.smartbalibackpaker.core.data.source.local.room.AccomDataDao
import com.smart.smartbalibackpaker.core.data.source.local.room.TourismDao
import com.smart.smartbalibackpaker.core.data.source.local.room.UploadResultDao

class LocalDataSource private constructor(
    private val movieDao: TourismDao,
    private val uploadResultDao: UploadResultDao,
    private val accomDataDao: AccomDataDao
    ) {

    fun insertPlace(place: List<TourismDataEntity>) {
        movieDao.insertPlace(place)
    }

    fun updatePlace(place: TourismDataEntity) {
        movieDao.updatePlace(place)
    }

    fun deletePlace(id: Int){
        movieDao.deleteTourism(id)
    }

    fun getPlaces(placeType: String): DataSource.Factory<Int, TourismDataEntity> =
        movieDao.getPlace(placeType)

    fun getChoosePlaces(placeType: String, sortItem: String, priceTarget: Int): DataSource.Factory<Int, TourismDataEntity> {
        var arrayRandomPlace : DataSource.Factory<Int, TourismDataEntity>
        when (sortItem) {
            "price" -> {
                return movieDao.getChoosePlaceByPrice(placeType)
            }
            "priceTarget" -> {
                return movieDao.getRandomByPrice(priceTarget)
            }
            else -> (
                    return movieDao.getChoosePlace(placeType)
                    )
        }
    }

    fun getDetailPlace(placeId: Int): LiveData<TourismDataEntity> =
        movieDao.getDetailPlace(placeId)

    fun setPlaceFavorite(place: TourismDataEntity, newState: Boolean){
        place.isFavorite = newState
        movieDao.updatePlace(place)
    }

    fun getPlacesFavorite() : LiveData<List<TourismDataEntity>> = movieDao.getFavoritePlace()

    fun insertResult(uploadResultEntity: UploadResultEntity) =
        uploadResultDao.insertRecordResult(uploadResultEntity)

    fun getUploadResult(tglDatang: String) =
        uploadResultDao.getUploadResult(tglDatang)

    fun insertAccom(accomDataEntity: List<AccomDataEntity>) =
        accomDataDao.insertAccom(accomDataEntity)

    fun updateAccom(accomDataEntity: AccomDataEntity) =
        accomDataDao.updateAccom(accomDataEntity)

    fun getAccom() =
        accomDataDao.getAccom()

    fun getDetailAccom(accomId: Int) =
        accomDataDao.getDetailAccom(accomId)

    companion object {
        private var INSTANCE: LocalDataSource? = null

        fun getInstance(tourismDao: TourismDao, uploadResultDao: UploadResultDao, accomDataDao: AccomDataDao): LocalDataSource =
            INSTANCE ?: LocalDataSource(tourismDao, uploadResultDao, accomDataDao)
    }
}