package com.smart.smartbalibackpaker.di

import android.content.Context
import com.smart.smartbalibackpaker.data.TourismRepository
import com.smart.smartbalibackpaker.data.source.local.LocalDataSource
import com.smart.smartbalibackpaker.data.source.local.room.TourismDatabase
import com.smart.smartbalibackpaker.data.source.remote.RemoteDataSource
import com.smart.smartbalibackpaker.utils.AppExecutors

object Injection {
    fun provideRepository(context: Context): TourismRepository {
        val database = TourismDatabase.getInstance(context)
        val remoteDataSource = RemoteDataSource.getInstance()
        val localDataSource = LocalDataSource.getInstance(database.tourismDao())
        val appExecutors = AppExecutors()
        return TourismRepository.getInstance(remoteDataSource, localDataSource, appExecutors)
    }
}