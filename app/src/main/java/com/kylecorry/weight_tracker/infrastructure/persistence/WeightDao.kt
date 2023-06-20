package com.kylecorry.weight_tracker.infrastructure.persistence

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface WeightDao {
    @Query("SELECT * FROM weight")
    fun getAll(): LiveData<List<WeightEntity>>

    @Query("SELECT * FROM weight WHERE _id = :id LIMIT 1")
    suspend fun get(id: Long): WeightEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(dream: WeightEntity): Long

    @Delete
    suspend fun delete(dream: WeightEntity)

    @Update
    suspend fun update(dream: WeightEntity)
}