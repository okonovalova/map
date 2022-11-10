package com.example.myapplication.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.repository.MarkersRepository
import com.example.myapplication.domain.entity.Marker
import com.example.myapplication.utils.SingleLiveEvent
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(
    private val repository: MarkersRepository,
) : ViewModel() {

    private val _markers: MutableLiveData<List<Marker>> = MutableLiveData()
    val markers: LiveData<List<Marker>> = _markers
    private val _navigateMarker = MutableLiveData<Marker?>()
    val navigateMarker: LiveData<Marker?>
        get() = _navigateMarker
    private val _goToMap = SingleLiveEvent<Unit>()
    val goToMap: LiveData<Unit>
        get() = _goToMap

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                getMarkers()
            }
        }
    }

    fun onMarkerNavigatedSuccessfully() {
        _navigateMarker.postValue(null)
    }

    fun onClickListener(marker: Marker) {
        _goToMap.postValue(Unit)
        _navigateMarker.postValue(marker)
    }

    fun saveMarker(title: String, lat: Double, long:Double) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.saveMarker(Marker(title, lat, long))
                getMarkers()
            }
        }
    }

    fun updateMarker(title: String, lat: Double, long:Double) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.updateMarker(Marker(title, lat, long))
                getMarkers()
            }
        }
    }

    fun removeMarker(lat: Double, long:Double) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.removeMarker(lat, long)
                getMarkers()
            }
        }
    }

    private suspend fun getMarkers() {
        val markers = repository.getAll()
        _markers.postValue(markers)
    }
}