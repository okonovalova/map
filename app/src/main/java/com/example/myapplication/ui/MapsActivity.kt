package com.example.myapplication.ui

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.ui.list.MarkersListFragment
import com.example.myapplication.ui.map.MapFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MapsActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private val viewModel: MapsViewModel by viewModels()

    private val mapFragment by lazy {
        MapFragment()
    }

    private val listFragment by lazy {
        MarkersListFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bottomMenu.setOnItemSelectedListener {
            if (binding.bottomMenu.selectedItemId == it.itemId) return@setOnItemSelectedListener true
            when (it.itemId) {
                R.id.map -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, mapFragment, MapFragment.TAG)
                        .addToBackStack(null)
                        .commit()

                }
                R.id.markers_list -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, listFragment, MarkersListFragment.TAG)
                        .addToBackStack(null)
                        .commit()
                }
                else -> {}
            }
            true
        }

        viewModel.goToMap.observe(this) {
            binding.bottomMenu.selectedItemId = R.id.map
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, mapFragment, MapFragment.TAG)
                .addToBackStack(null)
                .commit()
        }
    }
}
