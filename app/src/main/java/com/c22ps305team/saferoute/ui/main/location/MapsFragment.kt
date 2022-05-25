package com.c22ps305team.saferoute.ui.main.location

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.c22ps305team.saferoute.R
import com.c22ps305team.saferoute.data.ClusteringDataModel
import com.c22ps305team.saferoute.databinding.FragmentMapsBinding
import com.c22ps305team.saferoute.utils.readJsonFile
import com.c22ps305team.saferoute.utils.vectorToBitmap
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson

class MapsFragment : Fragment() {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!
    private lateinit var mapsViewModel: MapsViewModel


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
        val jsonFileString = readJsonFile(requireContext(), "clustering.json")
        val centroid: ClusteringDataModel =
            Gson().fromJson(jsonFileString, ClusteringDataModel::class.java)
        val jakarta = LatLng(-6.2147648, 106.8085002)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(jakarta, 11f))

        for (i in centroid.centroids!!.indices) {
            val latLng =
                LatLng(centroid.centroids[i]?.latitude!!, centroid.centroids[i]?.longitude!!)
            mMap.addMarker(
                MarkerOptions().position(latLng).title("Kecamatan $i").icon(
                    vectorToBitmap(R.drawable.ic_location_marker_30)
                )
            )
            mMap.addCircle(
                CircleOptions()
                    .center(latLng)
                    .radius(centroid.centroids[i]?.range!!)
                    .fillColor(0x22FF0000)
                    .strokeColor(Color.RED)
                    .strokeWidth(3f)
            )
        }

        mMap.setOnMarkerClickListener { marker ->
            val tvAreaName = activity?.findViewById<TextView>(R.id.tvAreaName)
            tvAreaName?.text = marker.title
            true
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}