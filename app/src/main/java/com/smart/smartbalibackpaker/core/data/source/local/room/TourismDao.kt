package com.smart.smartbalibackpaker.core.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.smart.smartbalibackpaker.core.data.source.local.entity.TourismDataEntity

@Dao
interface TourismDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = TourismDataEntity::class)
    fun insertPlace(tourism: List<TourismDataEntity>)

    @Update(entity = TourismDataEntity::class)
    fun updatePlace(tourism: TourismDataEntity)

    @Query("SELECT * FROM tourism_table WHERE type = :placeType")
    fun getPlace(placeType: String): DataSource.Factory<Int, TourismDataEntity>

    @Query("SELECT * FROM tourism_table WHERE isFavorite = 1")
    fun getFavoritePlace(): LiveData<List<TourismDataEntity>>

    @Query("SELECT * FROM tourism_table WHERE id = :placeId")
    fun getDetailPlace(placeId: Int): LiveData<TourismDataEntity>
}