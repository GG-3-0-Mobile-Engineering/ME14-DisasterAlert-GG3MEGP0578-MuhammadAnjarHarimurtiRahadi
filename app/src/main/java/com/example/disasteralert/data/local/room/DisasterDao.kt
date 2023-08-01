package com.example.disasteralert.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.disasteralert.data.local.entity.DisasterEntity

@Dao
interface DisasterDao {

    @Query("SELECT * FROM disaster ")
    fun getAllDisaster(): LiveData<List<DisasterEntity>>

    @Query("SELECT * FROM disaster WHERE disasterLoc = :disasterLoc")
    fun getDataByLocation(disasterLoc: String): LiveData<List<DisasterEntity>>

    @Query("SELECT * FROM disaster WHERE disasterType = :disasterType")
    fun getDataByDisaster(disasterType: String): LiveData<List<DisasterEntity>>

    @Query("SELECT * FROM disaster WHERE disasterLoc = :disasterLoc AND disasterType = :disasterType")
    fun getDataByLocationAndDisaster(disasterLoc: String, disasterType: String): LiveData<List<DisasterEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDisaster(news: List<DisasterEntity>)

    @Update
    suspend fun updateNews(news: DisasterEntity)

    @Query("DELETE FROM disaster")
    suspend fun deleteAllDisaster()
}