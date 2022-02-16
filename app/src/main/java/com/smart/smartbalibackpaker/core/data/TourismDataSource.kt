package com.smart.smartbalibackpaker.core.data

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.smart.smartbalibackpaker.core.data.source.local.entity.TourismDataEntity
import com.smart.smartbalibackpaker.core.vo.Resource

interface TourismDataSource {

    fun getAllTourism(placeType: String): LiveData<Resource<PagedList<TourismDataEntity>>>

    fun getDetailTourism(placeId: Int): LiveData<Resource<TourismDataEntity>>

    fun setPlaceFavorite(place: TourismDataEntity, state: Boolean)

    fun getPlacesFavorite() : LiveData<List<TourismDataEntity>>
}