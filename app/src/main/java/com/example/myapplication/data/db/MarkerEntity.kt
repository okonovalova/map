package com.example.myapplication.data.db

import androidx.room.Entity
import com.example.myapplication.domain.entity.Marker

@Entity(primaryKeys = ["lat", "lng"])
data class MarkerEntity(
    val title: String,
    val lat: Double,
    val lng: Double
) {

    companion object {
        fun toEntity(marker: Marker) = MarkerEntity(marker.title, marker.lat, marker.lng)
        fun toDomain(marker: MarkerEntity) = Marker(marker.title, marker.lat, marker.lng)
    }
}

fun List<Marker>.toEntity(): List<MarkerEntity> = map(MarkerEntity::toEntity)
fun List<MarkerEntity>.toDomain(): List<Marker> = map(MarkerEntity::toDomain)