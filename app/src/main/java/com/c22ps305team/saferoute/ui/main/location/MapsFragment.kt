package com.c22ps305team.saferoute.ui.main.location

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.c22ps305team.saferoute.R
import com.c22ps305team.saferoute.databinding.FragmentMapsBinding
import com.c22ps305team.saferoute.utils.lightStatusBar
import com.c22ps305team.saferoute.utils.setFullScreen
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment() {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!
    private lateinit var mapsViewModel: MapsViewModel
    private lateinit var mMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(callback)
        setupViewModel()
    }

    private fun setupViewModel() {
        mapsViewModel = ViewModelProvider(requireActivity()).get(MapsViewModel::class.java)
    }

    private val callback = OnMapReadyCallback { mMap ->

        // Add a marker in Stanford and move the camera
        val stanford = LatLng(-6.229728, 106.6894283)
        mMap.addMarker(MarkerOptions().position(stanford).title("Marker in Jakarta"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(stanford, 15f))
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}