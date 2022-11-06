package com.example.myapplication.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MarkerDao {
    @Query("SELECT * FROM MarkerEntity")
    suspend fun getAll(): List<MarkerEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: MarkerEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(posts: List<MarkerEntity>)

    @Query("DELETE FROM MarkerEntity WHERE lat = :lat AND lng = :lng")
    suspend fun removeById(lat: Double, lng: Double)
}