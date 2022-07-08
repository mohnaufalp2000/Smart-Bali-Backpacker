package com.smart.smartbalibackpaker.core.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.smart.smartbalibackpaker.core.data.source.local.entity.RecordVacationListEntity
import com.smart.smartbalibackpaker.core.data.source.local.entity.VacationCountEntity

@Dao
interface VacationCountDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = VacationCountEntity::class)
    fun insertVacatonCount(vacationcount: List<VacationCountEntity>)

    @Query("DELETE FROM vacation_count_table")
    fun deleteVacationCount()

    @Query("SELECT * FROM vacation_count_table WHERE id_perjalanan = :idPerjalanan")
    fun getVacationCount(idPerjalanan: Int): LiveData<List<VacationCountEntity>>
}