package com.c22ps305team.saferoute.ui.main.makeRoute

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import com.c22ps305team.saferoute.R
import com.c22ps305team.saferoute.databinding.ActivityMakeRouteBinding
import com.c22ps305team.saferoute.utils.hideKeyboard
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetBehavior


class MakeRouteActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMakeRouteBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMakeRouteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        setupBottomSheetBehavior()
    }

    // Map Callback
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isMyLocationButtonEnabled = false

        val jakarta = LatLng(-6.229728, 106.6894283)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(jakarta, 15f))

        getMyLocation()
    }

    // Bottom Sheet Behavior
    private fun setupBottomSheetBehavior() {
        val bottomSheetMakeRoute = findViewById<ConstraintLayout>(R.id.bottomSheetMakeRoute)
        val edtOriginPlace = findViewById<EditText>(R.id.edtOriginPlace)
        val btnSelectViaMap = findViewById<AppCompatButton>(R.id.btnSelectViaMap)

        edtOriginPlace.setOnFocusChangeListener { view, b ->
            if (view.isFocused) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }

        btnSelectViaMap.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
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

            }

        })
    }

    // Get user location
    private fun getMyLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        mMap.isMyLocationEnabled = true
    }

}