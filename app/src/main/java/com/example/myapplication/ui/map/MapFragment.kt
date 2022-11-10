package com.example.myapplication.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentMapsBinding
import com.example.myapplication.domain.entity.Marker
import com.example.myapplication.ui.MapsViewModel
import com.example.myapplication.ui.NewPointDialogFragment
import com.example.myapplication.ui.UpdatePointDialogFragment
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapFragment : Fragment(), OnMapReadyCallback {
    companion object {
        const val TAG = "MapFragmentTAG"
    }

    private val viewModel: MapsViewModel by activityViewModels()
    private lateinit var mMap: GoogleMap
    private lateinit var binding: FragmentMapsBinding

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                enableMyLocation()
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                enableMyLocation()
            }
            else -> {
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isMyLocationButtonEnabled = false
        initListeners()
        checkPermissions()
        viewModel.markers.observe(this) { markers ->
            mMap.clear()
            markers.map {
                addMarker(it.title, LatLng(it.lat, it.lng))
            }
        }
        viewModel.navigateMarker.observe(this) { marker ->
            if (marker == null) return@observe
            val camera = CameraUpdateFactory.newLatLngZoom(LatLng(marker.lat, marker.lng), 16.0f)
            mMap.animateCamera(camera)
            viewModel.onMarkerNavigatedSuccessfully()
        }
    }

    private fun initListeners() {
        mMap.setOnMapClickListener { latLng ->
            if (childFragmentManager.findFragmentByTag(NewPointDialogFragment.TAG) == null) {
                NewPointDialogFragment.newInstance {
                    viewModel.saveMarker(it, latLng.latitude,latLng.longitude)
                    addMarker(it, latLng)
                }
                    .show(childFragmentManager, NewPointDialogFragment.TAG)
            }

        }
        mMap.setOnMarkerClickListener { marker ->
            if (marker.isInfoWindowShown) {
                marker.hideInfoWindow()
            } else {
                marker.showInfoWindow()
            }
            if (childFragmentManager.findFragmentByTag(UpdatePointDialogFragment.TAG) == null) {
                UpdatePointDialogFragment.newInstance(
                    onDeletePointListener = {
                        marker.remove()
                        viewModel.removeMarker(marker.position.latitude,marker.position.longitude)
                    },
                    onRenamePointListener = {
                        marker.title = it
                        marker.hideInfoWindow()
                        marker.showInfoWindow()
                        viewModel.updateMarker(marker.title.orEmpty(), marker.position.latitude, marker.position.longitude)
                    },
                    title = marker.title.orEmpty()
                )
                    .show(childFragmentManager, UpdatePointDialogFragment.TAG)
            }
            true
        }
    }

    private fun checkPermissions() {
        if (hasLocationPermission()) {
            enableMyLocation()
        } else {
            locationPermissionRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    @SuppressLint("MissingPermission")
    private fun enableMyLocation() {
        mMap.uiSettings.isMyLocationButtonEnabled = true
        mMap.isMyLocationEnabled = true
    }

    private fun addMarker(name: String, latLng: LatLng) {
        val marker = MarkerOptions()
            .position(latLng)
            .title(name)
        mMap.addMarker(marker)
    }

    private fun hasLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
}