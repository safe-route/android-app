package com.c22ps305team.saferoute.ui.main.makeRoute

import android.Manifest
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.c22ps305team.saferoute.BuildConfig
import com.c22ps305team.saferoute.R
import com.c22ps305team.saferoute.adapter.ListPlaceAdapter
import com.c22ps305team.saferoute.data.DirectionsResponse
import com.c22ps305team.saferoute.data.ResultsItem
import com.c22ps305team.saferoute.databinding.ActivityMakeRouteBinding
import com.c22ps305team.saferoute.utils.decodePolyLines
import com.c22ps305team.saferoute.utils.hideKeyboard
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior


class MakeRouteActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var geocoder: Geocoder
    private lateinit var binding: ActivityMakeRouteBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var userCurrentLocation: LatLng? = null
    private val makeRouteViewModel by viewModels<MakeRouteViewModel>()

    private var isTracking = false
    private var boundsBuilder = LatLngBounds.Builder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMakeRouteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.btnBackToHome.setOnClickListener {
            onBackPressed()
        }
        setupBottomSheetBehavior()
        observeViewModel()
    }

    // Map Callback
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isMyLocationButtonEnabled = false

        val jakarta = LatLng(-6.229728, 106.6894283)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(jakarta, 10f))

        binding.btnGetMyLocation.setOnClickListener {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userCurrentLocation!!, 17F))
        }

        getMyLocation()
        createLocationRequest()
        createLocationCallBack()

        binding.btnStart.setOnClickListener {
            if (!isTracking) {
                updateTrackingStatus(true)
                startLocationUpdates()
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userCurrentLocation!!, 17F))
            } else {
                updateTrackingStatus(false)
                stopLocationUpdates()
                mMap.animateCamera(CameraUpdateFactory.zoomTo(14F))
            }
        }
    }

    private val resolutionLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) { result ->
            when (result.resultCode) {
                RESULT_OK ->
                    Log.i(
                        MakeRouteActivity::class.java.simpleName,
                        "onActivityResult: All location settings are satisfied."
                    )
                RESULT_CANCELED ->
                    Toast.makeText(
                        this@MakeRouteActivity,
                        "Anda harus mengaktifkan GPS untuk menggunakan aplikasi ini!",
                        Toast.LENGTH_SHORT
                    ).show()
            }
        }

    private fun updateTrackingStatus(newStatus: Boolean) {
        isTracking = newStatus
        if (isTracking) {
            binding.btnStart.text = "Stop"
        } else {
            binding.btnStart.text = getString(R.string.start_journey)
        }
    }

    fun createLocationRequest() {
        locationRequest = LocationRequest.create()
        locationRequest.interval = 500
        locationRequest.fastestInterval = 500
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val client = LocationServices.getSettingsClient(this)
        client.checkLocationSettings(builder.build())
            .addOnSuccessListener { getMyLocation() }.addOnFailureListener { exception ->
                if (exception is ResolvableApiException) {
                    try {
                        resolutionLauncher.launch(
                            IntentSenderRequest.Builder(exception.resolution).build()
                        )
                    } catch (sendEx: IntentSender.SendIntentException) {
                        Toast.makeText(this@MakeRouteActivity, sendEx.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
    }

    private fun createLocationCallBack() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    val lastLatLng = LatLng(location.latitude, location.longitude)
                    Log.d(
                        MakeRouteActivity::class.java.simpleName,
                        "onLocationResult: " + location.latitude + ", " + location.longitude
                    )

                    boundsBuilder.include(lastLatLng)
                    val bounds: LatLngBounds = boundsBuilder.build()
                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 500))

                }
            }
        }
    }

    private fun startLocationUpdates() {
        try {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        } catch (exception: SecurityException) {
            Log.e(MakeRouteActivity::class.java.simpleName, "Error : " + exception.message)
        }
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    // Observe Viewmodel
    private fun observeViewModel() {
        makeRouteViewModel.listPlace.observe(this) { place ->
            setResultPlaceData(place)
        }

        makeRouteViewModel.isLoading.observe(this) { loading ->
            showLoading(loading)
        }

        makeRouteViewModel.gecodeWaypoint.observe(this) { geoCodeWayPoint ->
            setRoute(geoCodeWayPoint)
        }
    }

    // Draw route to map
    private fun setRoute(geoCodeWayPoint: DirectionsResponse?) {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        val polyLine = geoCodeWayPoint?.routes?.get(0)?.overviewPolyline?.points
        val routeDecode = decodePolyLines(polyLine.toString())
        mMap.addPolyline(PolylineOptions().addAll(routeDecode!!).color(Color.GREEN).width(15F))
    }

    // Bottom Sheet Behavior
    private fun setupBottomSheetBehavior() {
        val bottomSheetMakeRoute = findViewById<ConstraintLayout>(R.id.bottomSheetMakeRoute)
        val edtOriginPlace = findViewById<EditText>(R.id.edtOriginPlace)
        val edtDestinationPlace = findViewById<EditText>(R.id.edtDestinationPlace)
        val btnSelectViaMap = findViewById<AppCompatButton>(R.id.btnSelectViaMap)
        val btnPickCurrentLocation = findViewById<AppCompatButton>(R.id.btnPickCurrentLocation)

        edtOriginPlace.setOnFocusChangeListener { view, b ->
            if (view.isFocused) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                btnPickCurrentLocation.visibility = View.VISIBLE
            } else {
                btnPickCurrentLocation.visibility = View.GONE
            }
        }

        edtDestinationPlace.addTextChangedListener { query ->
            makeRouteViewModel.searchPlace(query.toString(), BuildConfig.API_KEY)
        }

        btnSelectViaMap.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        btnPickCurrentLocation.setOnClickListener {
            geocoder = Geocoder(this)
            val address = geocoder.getFromLocation(
                userCurrentLocation?.latitude!!,
                userCurrentLocation?.longitude!!,
                1
            )
            val streetName = address[0].getAddressLine(0).split(",")
            edtOriginPlace.setText(streetName[0])
        }

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetMakeRoute)
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        bottomSheetMakeRoute.setBackgroundColor(Color.WHITE)
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        bottomSheetMakeRoute.setBackgroundResource(R.drawable.bg_top_corner)
                        edtOriginPlace.hideKeyboard()
                        edtOriginPlace.clearFocus()
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // Do somthing when bottom sheet on slide state
            }

        })
    }

    // Get user location
    private fun getMyLocation() {
        mMap.isMyLocationEnabled = true
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    userCurrentLocation = LatLng(location.latitude, location.longitude)
                    mMap.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            userCurrentLocation!!,
                            18F
                        )
                    )
                } else {
                    Toast.makeText(
                        this@MakeRouteActivity,
                        "Location is not found. Try Again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            requestPermission.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }

    }

    // Request Permission
    private val requestPermission = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permission ->
        when {
            permission[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                // Precise location access granted.
                getMyLocation()
            }
            permission[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                // Only approximate location access granted.
                getMyLocation()
            }
            else -> {
                Toast.makeText(this, "Maaf kamu tidak memberikan izin", Toast.LENGTH_LONG).show()
            }
        }
    }

    // Check Permission
    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    // Set result place data
    private fun setResultPlaceData(placeResult: List<ResultsItem?>) {
        val listPlace = ArrayList<ResultsItem>()
        val rvListPlace = findViewById<RecyclerView>(R.id.rvListPlace)

        for (place in placeResult) {
            listPlace.add(place!!)
        }

        rvListPlace.layoutManager = LinearLayoutManager(this)
        val adapter = ListPlaceAdapter(listPlace) {
            val wrapperBtnControll = findViewById<RelativeLayout>(R.id.wrapperBtnControll)
            val wrappertBottomSheet = findViewById<ConstraintLayout>(R.id.bottomSheetWrapper)
            val wrapperBtnStart = findViewById<RelativeLayout>(R.id.wrapperBtnStart)
            val bottomSheet = findViewById<ConstraintLayout>(R.id.bottomSheetMakeRoute)

            wrapperBtnControll.visibility = View.GONE
            wrappertBottomSheet.visibility = View.GONE
            bottomSheet.visibility = View.GONE
            wrapperBtnStart.visibility = View.VISIBLE

            mMap.clear()
            mMap.animateCamera(CameraUpdateFactory.zoomTo(13F))
            makeRoute(it)
        }
        rvListPlace.adapter = adapter
    }

    // Make routing
    private fun makeRoute(listPlace: ResultsItem) {
        val geoMetry = listPlace.geometry
        val destinationPlace = "${geoMetry?.location?.lat},${geoMetry?.location?.lng}"
        val originPlace = "${userCurrentLocation?.latitude}, ${userCurrentLocation?.longitude}"
        makeRouteViewModel.getDirection(originPlace, destinationPlace, BuildConfig.API_KEY)
    }

    // Handle loading state
    private fun showLoading(loading: Boolean) {
        val shimerFrameLayout = findViewById<ShimmerFrameLayout>(R.id.shimerFrameLayout)
        val rvListPlace = findViewById<RecyclerView>(R.id.rvListPlace)
        if (!loading) {
            shimerFrameLayout.stopShimmer()
            shimerFrameLayout.visibility = View.GONE
            rvListPlace.visibility = View.VISIBLE
        } else {
            shimerFrameLayout.visibility = View.VISIBLE
            shimerFrameLayout.startShimmer()
            rvListPlace.visibility = View.GONE
        }
    }

    override fun onResume() {
        val shimerFrameLayout = findViewById<ShimmerFrameLayout>(R.id.shimerFrameLayout)
        super.onResume()

        shimerFrameLayout.stopShimmer()
        if (isTracking) {
            startLocationUpdates()
        }
    }

    override fun onPause() {
        val shimerFrameLayout = findViewById<ShimmerFrameLayout>(R.id.shimerFrameLayout)
        super.onPause()

        shimerFrameLayout.stopShimmer()
        stopLocationUpdates()
    }

}