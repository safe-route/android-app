package com.c22ps305team.saferoute.ui.main.location

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.c22ps305team.saferoute.R
import com.c22ps305team.saferoute.data.ClusteringDataModel
import com.c22ps305team.saferoute.databinding.FragmentMapsBinding
import com.c22ps305team.saferoute.utils.readJsonFile
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.gson.Gson


class MapsFragment : Fragment() {

    private lateinit var mMap: GoogleMap
    private lateinit var geofencingClient: GeofencingClient
    private lateinit var mapsViewModel: MapsViewModel

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!

    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var range: Double = 0.0


    private val callback = OnMapReadyCallback { googleMap ->
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isCompassEnabled = true

        markerCentroids()
    }


    //Pending Intent ke BroadcastReceiver
    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(requireActivity(), GeofenceBroadcastReceiver::class.java)
        intent.action = GeofenceBroadcastReceiver.ACTION_GEOFENCE_EVENT
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getBroadcast(requireActivity(), 0, intent, PendingIntent.FLAG_MUTABLE)
        } else {
            PendingIntent.getBroadcast(
                requireActivity(),
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        setupViewModel()
    }


    private fun setupViewModel() {
        mapsViewModel = ViewModelProvider(requireActivity()).get(MapsViewModel::class.java)
    }

    //Centroids
    private fun markerCentroids() {
        val jsonFileString = readJsonFile(requireContext(), "clustering.json")
        val centroid: ClusteringDataModel =
            Gson().fromJson(jsonFileString, ClusteringDataModel::class.java)
        val jakarta = LatLng(-6.2147648, 106.8085002)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(jakarta, 15f))

        //get data
        for (i in centroid.centroids!!.indices) {
            val centoidList: ArrayList<Double> = ArrayList()

            latitude = centroid.centroids[i]?.latitude!!
            longitude = centroid.centroids[i]?.longitude!!
            range = centroid.centroids[i]?.range!!


            //marker
            val latLng = LatLng(latitude, longitude)
            mMap.addMarker(
                MarkerOptions().position(latLng).title("Kecamatan $i").icon(
                    vectorToBitmap(R.drawable.ic_location_marker_30)
                )
            )
            mMap.addCircle(
                CircleOptions()
                    .center(latLng)
                    .radius(range)
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


        getMyLocation()
        addGeofence()
    }


    //PERMISSION
    private val requestBackgroundLocationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    private val runningQOrLater = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

    @TargetApi(Build.VERSION_CODES.Q)
    private val requestLocationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                if (runningQOrLater) {
                    requestBackgroundLocationPermissionLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                } else {
                    getMyLocation()
                }
            }
        }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            requireActivity(),
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    @TargetApi(Build.VERSION_CODES.Q)
    private fun checkForegroundAndBackgroundLocationPermission(): Boolean {
        val foregroundLocationApproved = checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        val backgroundPermissionApproved =
            if (runningQOrLater) {
                checkPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
            } else {
                true
            }
        return foregroundLocationApproved && backgroundPermissionApproved
    }


    //CURRENT LOC
    @SuppressLint("MissingPermission")
    private fun getMyLocation() {
        if (checkForegroundAndBackgroundLocationPermission()) {
            mMap.isMyLocationEnabled = true
        } else {
            requestLocationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }


    //GEOFENCE
    @SuppressLint("MissingPermission")
    private fun addGeofence() {
        //geofencing
        geofencingClient = LocationServices.getGeofencingClient(requireActivity())

        val geofence = Geofence.Builder()
            .setRequestId("Danger")
            .setCircularRegion(
                latitude,
                longitude,
                range.toFloat()
            )
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_DWELL or Geofence.GEOFENCE_TRANSITION_ENTER)
            .setLoiteringDelay(5000)
            .build()

        val geofencingRequest = GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .addGeofence(geofence)
            .build()

        geofencingClient.removeGeofences(geofencePendingIntent).run {
            addOnCompleteListener {
                geofencingClient.addGeofences(geofencingRequest, geofencePendingIntent).run {
                    addOnSuccessListener {
                        showToast("Geofencing added")
                    }
                    addOnFailureListener {
                        showToast("Geofencing not added : ${it.message}")
                    }
                }
            }
        }

    }


    private fun vectorToBitmap(@DrawableRes id: Int): BitmapDescriptor {
        val vectorDrawable = ResourcesCompat.getDrawable(resources, id, null)
        if (vectorDrawable == null) {
            Log.e("BitmapHelper", "Resource not found")
            return BitmapDescriptorFactory.defaultMarker()
        }
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }


    private fun showToast(text: String) {
        Toast.makeText(requireActivity(), text, Toast.LENGTH_SHORT).show()
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        fun newInstance() = MapsFragment().apply {
            arguments = Bundle().apply { }
        }
    }

}