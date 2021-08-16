package com.smart.smartbalibackpaker.data

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.smart.smartbalibackpaker.data.source.local.entity.TourismDataEntity
import com.smart.smartbalibackpaker.vo.Resource

interface TourismDataSource {

    fun getAllTourism(placeType: String): LiveData<Resource<PagedList<TourismDataEntity>>>

    fun getDetailTourism(placeId: Int): LiveData<Resource<TourismDataEntity>>
}