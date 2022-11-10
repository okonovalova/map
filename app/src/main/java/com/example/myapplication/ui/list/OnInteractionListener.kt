package com.example.myapplication.ui.list

import com.example.myapplication.domain.entity.Marker
import com.google.android.gms.maps.model.LatLng

interface OnInteractionListener {
    fun goToPointOnMap(marker:Marker)
    fun onRemove(marker:Marker)
    fun onUpdate(title: String, marker:Marker)
}