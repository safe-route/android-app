package com.c22ps305team.saferoute.ui.main.location

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.c22ps305team.saferoute.R
import com.c22ps305team.saferoute.data.ClusteringDataModel
import com.c22ps305team.saferoute.data.CoordinateResponse
import com.c22ps305team.saferoute.data.CoordinatesItem
import com.c22ps305team.saferoute.databinding.FragmentMapsBinding
import com.c22ps305team.saferoute.utils.readJsonFile
import com.c22ps305team.saferoute.utils.vectorToBitmap
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.maps.android.PolyUtil


@SuppressLint("UnspecifiedImmutableFlag")
class MapsFragment : Fragment(), GoogleMap.OnPolygonClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var geofencingClient: GeofencingClient

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!
    private val mapsViewModel by viewModels<MapsViewModel>()

    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var range: Double = 0.0

    private val latLngList = ArrayList<LatLng>()
    private val rangeList = ArrayList<Double>()


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


        setUpFilterBottomSheet()
        setupInformationBottomSheet()
        updateInformationBottomSheet()
        mapsViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
    }

    private fun updateInformationBottomSheet() {
        val tvAreaName = view?.findViewById<TextView>(R.id.tvAreaName)
        val tvStateValue = view?.findViewById<TextView>(R.id.tvStateValue)
        val tvStatePrecentage = view?.findViewById<TextView>(R.id.tvStatePercentage)
        val tvCurrentInfo = view?.findViewById<TextView>(R.id.tvTotalCrime)
        val rvArea = view?.findViewById<RecyclerView>(R.id.rvAreaBottom)
        mapsViewModel.dataAreaStatistic.observe(viewLifecycleOwner) {
            tvAreaName?.text = it.subdistrict
            tvStatePrecentage?.apply {
                visibility = View.VISIBLE
                text = it.totalCrime.toString()
            }

            val dataCrime = it.crimeInfo!!
            val rvAdapter = InfoAdapter(dataCrime)
            rvArea?.adapter = rvAdapter
            rvArea?.layoutManager = LinearLayoutManager(requireContext(),RecyclerView.VERTICAL, false)
        }

    }

    private fun setupInformationBottomSheet() {
        val tvAreaName = view?.findViewById<TextView>(R.id.tvAreaName)
        val tvStateValue = view?.findViewById<TextView>(R.id.tvStateValue)
        val tvStatePrecentage = view?.findViewById<TextView>(R.id.tvStatePercentage)
        val tvCurrentInfo = view?.findViewById<TextView>(R.id.tvTotalCrime)

        tvAreaName?.text = resources.getString(R.string.statistic_crime_info)
        tvStateValue?.text = resources.getString(R.string.latest_info)

        tvStatePrecentage?.visibility = View.GONE
        tvCurrentInfo?.visibility = View.GONE
    }

    private val callback = OnMapReadyCallback { googleMap ->
        mMap = googleMap
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMyLocationButtonEnabled = false

        val jakarta = LatLng(-6.2147648, 106.8085002)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(jakarta, 11f))

//        markerCentroids()
        mMap.setOnPolygonClickListener(this)
        drawSubdistrict()
    }

    // Filter Bottom Sheet
    private fun setUpFilterBottomSheet() {
        binding.btnFilterMap.setOnClickListener {
            val dialog = BottomSheetDialog(requireContext())
            val bottomSheetView = LayoutInflater.from(requireContext()).inflate(
                R.layout.filter_map_bottom_sheet,
                view?.findViewById(R.id.filterBsContainer)
            )
            val btnClose = bottomSheetView.findViewById<ImageButton>(R.id.btnCloseFilterBottomSheet)
            val selectCentroid = bottomSheetView.findViewById<FrameLayout>(R.id.selectCentroid)
            val selectArea = bottomSheetView.findViewById<FrameLayout>(R.id.selectArea)

            btnClose
                .setOnClickListener {
                    dialog.dismiss()
                }
            selectCentroid.setOnClickListener {
                dialog.dismiss()
                mMap.clear()
                markerCentroids()
            }
            selectArea.setOnClickListener {
                dialog.dismiss()
                mMap.clear()
                drawSubdistrict()
            }

            dialog.setContentView(bottomSheetView)
            dialog.show()
        }
    }

    //Centroids
    @SuppressLint("PotentialBehaviorOverride")
    private fun markerCentroids() {
        val jsonFileString = readJsonFile(requireContext(), "clustering.json")
        val centroid: ClusteringDataModel =
            Gson().fromJson(jsonFileString, ClusteringDataModel::class.java)


        //get data
        for (i in centroid.centroids!!.indices) {
            latitude = centroid.centroids[i]?.latitude!!
            longitude = centroid.centroids[i]?.longitude!!
            range = centroid.centroids[i]?.range!!

            latLngList.add(LatLng(latitude, longitude))
            rangeList.add(range)

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
        addGeofence()
        getMyLocation()
    }

    // Tiling Disctrict
    private fun drawSubdistrict() {
        val jsonFileString = readJsonFile(requireContext(), "area_statistic.json")
        val data: CoordinateResponse =
            Gson().fromJson(jsonFileString, CoordinateResponse::class.java)


        val coordinates = ArrayList<List<CoordinatesItem>>()
//
        for (i in data.statistic?.indices!!) {
            coordinates.add(data.statistic[i]?.coordinates as List<CoordinatesItem>)
        }

        var allArea: MutableList<LatLng?>
        for (i in coordinates.indices) {
            allArea = ArrayList()
            for (j in coordinates[i].indices) {
                allArea.add(LatLng(coordinates[i][j].latitude!!, coordinates[i][j].longitude!!))
            }
            if (data.statistic[i]?.totalCrime!! >= 90) {
                mMap.addPolygon(
                    PolygonOptions()
                        .addAll(PolyUtil.simplify(allArea, 3.0))
                        .fillColor(0x22FF0000)
                        .strokeWidth(3f)
                        .clickable(true)
                ).tag = data.statistic[i]?.subdistrict
            } else if (data.statistic[i]?.totalCrime!! in 50..89) {
                mMap.addPolygon(
                    PolygonOptions()
                        .addAll(PolyUtil.simplify(allArea, 3.0))
                        .fillColor(0x22FCFC0F)
                        .strokeWidth(3f)
                        .clickable(true)
                ).tag = data.statistic[i]?.subdistrict
            } else {
                mMap.addPolygon(
                    PolygonOptions()
                        .addAll(PolyUtil.simplify(allArea, 3.0))
                        .fillColor(0x220DE096)
                        .strokeWidth(3f)
                        .clickable(true)
                ).tag = data.statistic[i]?.subdistrict
            }
        }
    }

    override fun onPolygonClick(polygon: Polygon) {
        var color = polygon.strokeColor xor 0x00ffffff
        polygon.strokeColor = color
        color = polygon.fillColor xor 0x00ffffff
        polygon.fillColor = color
        val areaName = JsonObject()
        areaName.addProperty("subdistrict", polygon.tag.toString())
        mapsViewModel.getDataAreaStatistic(areaName)
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

    // Geofence Request
    private fun getGeofencingRequest(): GeofencingRequest {
        val geoFenceList: ArrayList<Geofence> = ArrayList()

        rangeList.forEachIndexed { index, it ->
            geoFenceList.add(
                Geofence.Builder()
                    .setRequestId("Danger")
                    .setCircularRegion(
                        latLngList[index].latitude,
                        latLngList[index].longitude,
                        rangeList[index].toFloat()
                    )
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_DWELL or Geofence.GEOFENCE_TRANSITION_ENTER)
                    .setLoiteringDelay(5000)
                    .build()
            )
        }

        val builder = GeofencingRequest.Builder()
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
        builder.addGeofences(geoFenceList)
        return builder.build()
    }

    //GEOFENCE
    @SuppressLint("MissingPermission")
    private fun addGeofence() {
        //geofencing
        geofencingClient = LocationServices.getGeofencingClient(requireContext())


//        val geofence = Geofence.Builder()
//            .setRequestId("Danger")
//            .setCircularRegion(
//                latitude,
//                longitude,
//                range.toFloat()
//            )
//            .setExpirationDuration(Geofence.NEVER_EXPIRE)
//            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_DWELL or Geofence.GEOFENCE_TRANSITION_ENTER)
//            .setLoiteringDelay(5000)
//            .build()
//
//        Log.d("LatLng: ", "$latitude, $longitude")
//
//        val geofencingRequest = GeofencingRequest.Builder()
//            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
//            .addGeofences(geoFenceList)
//            .build()

//        geofencingClient.removeGeofences(geofencePendingIntent).run {
//            addOnCompleteListener {
//                geofencingClient.addGeofences(getGeofencingRequest(), geofencePendingIntent).run {
//                    addOnSuccessListener {
//                        showToast("Geofencing added")
//                    }
//                    addOnFailureListener {
//                        showToast("Geofencing not added : ${it.message}")
//                    }
//                }
//            }
//        }

        geofencingClient.addGeofences(getGeofencingRequest(), geofencePendingIntent).run {
            addOnSuccessListener {
                showToast("Geofencing added")
            }
            addOnFailureListener {
                showToast("Geofencing not added : ${it.message}")
            }
        }
        Log.d("Apa: ", getGeofencingRequest().toString())
    }

    private fun showToast(text: String) {
        Toast.makeText(requireActivity(), text, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        val shimerMapBottomSheet = view?.findViewById<ShimmerFrameLayout>(R.id.shimerMapBottomSheet)
        val headerBottomSheet = view?.findViewById<ConstraintLayout>(R.id.headerMapBottomSheet)

        if (isLoading) {
            shimerMapBottomSheet?.visibility = View.VISIBLE
            shimerMapBottomSheet?.startShimmer()
            headerBottomSheet?.visibility = View.GONE
        } else {
            shimerMapBottomSheet?.stopShimmer()
            shimerMapBottomSheet?.visibility = View.GONE
            headerBottomSheet?.visibility = View.VISIBLE
        }
    }

//    override fun onResume() {
//        val shimerMapBottomSheet = view?.findViewById<ShimmerFrameLayout>(R.id.shimerMapBottomSheet)
//        super.onResume()
//
//        shimerMapBottomSheet?.stopShimmer()
//    }

//    override fun onPause() {
//        val shimerMapBottomSheet = view?.findViewById<ShimmerFrameLayout>(R.id.shimerMapBottomSheet)
//        super.onPause()
//
//        shimerMapBottomSheet?.stopShimmer()
//    }

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
