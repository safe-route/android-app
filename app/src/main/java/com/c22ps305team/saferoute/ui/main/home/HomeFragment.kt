package com.c22ps305team.saferoute.ui.main.home

import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.c22ps305team.saferoute.data.Statistic
import com.c22ps305team.saferoute.database.dataPredict.DataPredict
import com.c22ps305team.saferoute.database.dataTraining.DataTraining
import com.c22ps305team.saferoute.databinding.FragmentHomeBinding
import com.c22ps305team.saferoute.ui.main.detail.DetailPlaceActivity
import com.c22ps305team.saferoute.ui.main.makeRoute.MakeRouteActivity
import com.c22ps305team.saferoute.utils.DateUtils
import com.c22ps305team.saferoute.utils.RoomViewModelFactory
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var placeInfoData: ArrayList<Statistic>
    private lateinit var placeInfoAdapter: PlaceInfoAdapter
    private lateinit var db: FirebaseFirestore


    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        homeViewModel = obtainViewModel(requireActivity())
        homeViewModel.createModel("userTest")


        doStreamDataTraining()
        getCurrentLocation()
        createLocationRequest()
        createLocationCallBack()
        startLocationUpdates()

        setupPlaceInfo()
    }

    private fun doStreamDataTraining() {
        var data = JsonArray()
        var dataTraining = JsonObject()
        homeViewModel.getAllDataTraining().observe(viewLifecycleOwner) { listData ->
            if (listData.size >= 720) {
                val obj = JsonObject()
                for (list in listData) {
                    obj.addProperty("latitude", list.latitude)
                    obj.addProperty("longitude", list.longitude)
                    obj.addProperty("datetime", list.date)
                    data.add(obj)
                }
                dataTraining.add("data", data)
                homeViewModel.trainModel("userTest", dataTraining)
                doStreamDataPredict()
                lifecycleScope.launch {
                    delay(2000)
                    data = JsonArray()
                    dataTraining = JsonObject()
                }
                homeViewModel.deleteDataTraining()
            }
        }
    }


    private fun doStreamDataPredict() {
        var data = JsonArray()
        var dataPredict = JsonObject()
        homeViewModel.getAllData().observe(viewLifecycleOwner) { listData ->
            if (listData.size >= 15) {
                val obj = JsonObject()
                for (list in listData) {
                    obj.addProperty("latitude", list.latitude)
                    obj.addProperty("longitude", list.longitude)
                    obj.addProperty("datetime", list.date)
                    data.add(obj)
                }
                dataPredict.addProperty("email", "fillahfirdausyah22@gmail.com")
                dataPredict.addProperty("latitude", listData[listData.size - 1].latitude)
                dataPredict.addProperty("longitude", listData[listData.size - 1].longitude)
                dataPredict.add("data", data)
                homeViewModel.foreCast("userTest", dataPredict)
                lifecycleScope.launch {
                    delay(2000)
                    data = JsonArray()
                    dataPredict = JsonObject()
                }
                homeViewModel.deleteData()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        getCurrentLocation()
        createLocationRequest()
        createLocationCallBack()
        startLocationUpdates()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun obtainViewModel(activity: FragmentActivity): HomeViewModel {
        val factory = RoomViewModelFactory.getInstance(activity.application)

        return ViewModelProvider(activity, factory).get(HomeViewModel::class.java)
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                getCurrentLocation()
            }
        }


    private fun getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { currentLocation ->
                if (currentLocation != null) {
                    val lat = currentLocation.latitude
                    val long = currentLocation.longitude
                    getCityName(lat, long)
                } else {
                    Toast.makeText(requireContext(), "Nyalakan location!!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }


    private fun getCityName(lat: Double, long: Double) {
        val geoCoder = Geocoder(requireContext().applicationContext, Locale.getDefault())
        val address = geoCoder.getFromLocation(lat, long, 1)
        val cityName: String = address[0].locality
    }


    private fun setupPlaceInfo() {
        placeInfoData = arrayListOf()
        placeInfoAdapter = PlaceInfoAdapter(placeInfoData)

        //setAdapter
        binding.rvArea.apply {
            adapter = placeInfoAdapter
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        }

        //Klik
        placeInfoAdapter.setOnItemClickCallback(object : PlaceInfoAdapter.OnItemClickCallback {
            override fun onItemClicked(statistic: Statistic) {
                val intent = Intent(requireContext(), DetailPlaceActivity::class.java)
                intent.putExtra(DetailPlaceActivity.EXTRA_STATS, statistic)
                startActivity(intent)
            }
        })
        eventChangeListener()
    }


    private fun eventChangeListener() {
        db = FirebaseFirestore.getInstance()
        db.collection("statistic").addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null) {
                    Log.e("Firestore error", error.message.toString())
                    return
                }
                for (dc: DocumentChange in value?.documentChanges!!) {
                    if (dc.type == DocumentChange.Type.ADDED) {
                        placeInfoData.add(dc.document.toObject(Statistic::class.java))
                    }
                }
                placeInfoAdapter.notifyDataSetChanged()
            }
        })
    }

    private val resolutionLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) { result ->
            when (result.resultCode) {
                AppCompatActivity.RESULT_OK ->
                    Log.i(
                        MakeRouteActivity::class.java.simpleName,
                        "onActivityResult: All location settings are satisfied."
                    )
                AppCompatActivity.RESULT_CANCELED ->
                    Toast.makeText(
                        requireContext(),
                        "Anda harus mengaktifkan GPS untuk menggunakan aplikasi ini!",
                        Toast.LENGTH_SHORT
                    ).show()
            }
        }


    private fun createLocationRequest() {
        locationRequest = LocationRequest.create()
        locationRequest.interval = 120000
        locationRequest.fastestInterval = 120000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val client = LocationServices.getSettingsClient(requireContext())
        client.checkLocationSettings(builder.build())
            .addOnSuccessListener { getCurrentLocation() }.addOnFailureListener { exception ->
                if (exception is ResolvableApiException) {
                    try {
                        resolutionLauncher.launch(
                            IntentSenderRequest.Builder(exception.resolution).build()
                        )
                    } catch (sendEx: IntentSender.SendIntentException) {
                        Toast.makeText(requireContext(), sendEx.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
    }

    private fun createLocationCallBack() {
        val dataPredict = DataPredict()
        val dataTraining = DataTraining()
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    val date = DateUtils.getCurrentDate()
                    dataPredict.let {
                        dataPredict.latitude = location.latitude
                        dataPredict.longitude = location.longitude
                        dataPredict.date = date
                    }
                    dataTraining.let {
                        dataTraining.latitude = location.latitude
                        dataTraining.longitude = location.longitude
                        dataTraining.date = date
                    }
                    homeViewModel.insertData(dataPredict)
                    homeViewModel.insertDataTraining(dataTraining)
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


    companion object {
        fun newInstance() = HomeFragment().apply {
            arguments = Bundle().apply { }
        }
    }

}