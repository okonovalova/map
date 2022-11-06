package com.example.myapplication.data.repository

import com.example.myapplication.data.db.MarkerDao
import com.example.myapplication.data.db.MarkerEntity
import com.example.myapplication.data.db.toDomain
import com.example.myapplication.domain.entity.Marker
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MarkersRepository @Inject constructor(
    private val markerDao: MarkerDao,
) {
    suspend fun saveMarker(marker: Marker) {
        markerDao.insert(MarkerEntity.toEntity(marker))
    }

    suspend fun updateMarker(marker: Marker) {
        markerDao.insert(MarkerEntity.toEntity(marker))
    }

    suspend fun removeMarker(lat: Double, lng: Double) {
        markerDao.removeById(lat, lng)
    }

    suspend fun getAll(): List<Marker> {
        return markerDao.getAll().toDomain()
    }
}