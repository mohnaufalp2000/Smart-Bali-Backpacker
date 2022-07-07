package com.smart.smartbalibackpaker.core.data.source.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.smart.smartbalibackpaker.core.data.source.local.entity.AccomDataEntity
import com.smart.smartbalibackpaker.core.data.source.local.entity.TourismDataEntity
import com.smart.smartbalibackpaker.core.data.source.local.entity.UploadResultEntity

@Database(
    entities = [TourismDataEntity::class, UploadResultEntity::class, AccomDataEntity::class],
    version = 2,
    exportSchema = false
)
abstract class TourismDatabase : RoomDatabase() {
    abstract fun tourismDao(): TourismDao
    abstract fun uploadResultDao(): UploadResultDao
    abstract fun accomDataDao(): AccomDataDao

    companion object {
        @Volatile
        private var INSTANCE: TourismDatabase? = null

        fun getInstance(context: Context): TourismDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    TourismDatabase::class.java,
                    "tourism_database"
                ).fallbackToDestructiveMigration().build().apply {
                    INSTANCE = this
                }
            }
    }
}