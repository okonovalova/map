package com.example.myapplication.ui.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.myapplication.databinding.FragmentMarkersListBinding
import com.example.myapplication.domain.entity.Marker
import com.example.myapplication.ui.MapsViewModel
import com.example.myapplication.ui.UpdatePointDialogFragment
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MarkersListFragment : Fragment() {

    companion object {
        const val TAG = "MarkersListFragmentTAG"
    }

    private val viewModel: MapsViewModel by activityViewModels()
    private val adapter by lazy {
        MarkersViewAdapter(object : OnInteractionListener {
            override fun goToPointOnMap(marker: Marker) {
                viewModel.onClickListener(marker)
            }

            override fun onRemove(marker: Marker) {

                    viewModel.removeMarker(marker.lat, marker.lng)

            }

            override fun onUpdate(title: String, marker: Marker) {
                if (childFragmentManager.findFragmentByTag(UpdatePointDialogFragment.TAG) == null) {
                    UpdatePointDialogFragment.newInstance(
                        onDeletePointListener = {
                            viewModel.removeMarker(marker.lat, marker.lng)
                        },
                        onRenamePointListener = {
                            viewModel.updateMarker(it, marker.lat, marker.lng)
                        },
                        title = marker.title.orEmpty()
                    )
                        .show(childFragmentManager, UpdatePointDialogFragment.TAG)
                }
                viewModel.updateMarker(title, marker.lat,marker.lng)
            }

        })
    }
    private lateinit var binding: FragmentMarkersListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMarkersListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.list.adapter = adapter
        viewModel.markers.observe(viewLifecycleOwner) { markers ->
            adapter.submitList(markers)
        }
    }
}