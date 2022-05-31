package com.c22ps305team.saferoute.ui.main.makeRoute

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.c22ps305team.saferoute.R
import com.c22ps305team.saferoute.databinding.ActivityMakeRouteBinding

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
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

        val bottomSheetMakeRoute = findViewById<ConstraintLayout>(R.id.bottomSheetMakeRoute)
        val edtOriginPlace = findViewById<EditText>(R.id.edtOriginPlace)

        edtOriginPlace.setOnFocusChangeListener { view, b ->
            if (view.isFocused) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
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
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

        })

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}