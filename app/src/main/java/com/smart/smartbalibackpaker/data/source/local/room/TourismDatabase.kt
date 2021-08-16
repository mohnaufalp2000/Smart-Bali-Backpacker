package com.smart.smartbalibackpaker.data.source.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.smart.smartbalibackpaker.data.source.local.entity.TourismDataEntity

@Database(
    entities = [TourismDataEntity::class],
    version = 1,
    exportSchema = false
)
abstract class TourismDatabase : RoomDatabase() {
    abstract fun tourismDao(): TourismDao

    companion object {
        @Volatile
        private var INSTANCE: TourismDatabase? = null

        fun getInstance(context: Context): TourismDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    TourismDatabase::class.java,
                    "tourism_database"
                ).build().apply {
                    INSTANCE = this
                }
            }
    }
}