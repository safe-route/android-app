package com.c22ps305team.saferoute.ui.main.reportCrime

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.c22ps305team.saferoute.databinding.FragmentCrimeReportBinding
import com.c22ps305team.saferoute.utils.Result
import com.c22ps305team.saferoute.utils.ViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.JsonObject
import java.text.SimpleDateFormat
import java.util.*

class CrimeReportFragment : Fragment() {
    private var _binding: FragmentCrimeReportBinding? = null
    private val binding get() = _binding!!


    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var location: Location? = null

    private val crimeReportViewModel: CrimeReportViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentCrimeReportBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())


        setupObserver()
        setupListener()
    }

    private fun setupObserver() {
        crimeReportViewModel.reportResponse.observe(requireActivity()){response ->
            when(response) {
                is Result.Loading -> {

                }
                is Result.Success -> {
                    Toast.makeText(requireContext(), "Berhasil", Toast.LENGTH_SHORT).show()
                }
                is Result.Error -> {

                }
            }
        }
    }


    private fun setupListener() {

        binding.switchLoc.setOnCheckedChangeListener{ _, isChecked ->
            if (isChecked){
                getCurrentLocation()
            } else {
                this.location = null
            }
        }

        binding.btnReport.setOnClickListener {
            //Snackbar.make(binding.root,"Laporan Terkirim! Selalu waspada & berhati-hati!", Snackbar.LENGTH_SHORT)
            //Toast.makeText(requireContext(), "Laporan terkirim", Toast.LENGTH_SHORT).show()
            uploadReport()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    private fun uploadReport(){
        val inputCrime = binding.inputCrime
        val inputTime = binding.inputTime
        val shareLoc = binding.switchLoc.isChecked
        var isValid = true

        if (inputCrime.text.isBlank()){
            inputCrime.error = "Kejadian tidak boleh kosong"
            isValid = false
        }

        if(inputTime.text.isBlank()){
            inputTime.error = "Waktu tidak boleh kosong"
            isValid = false
        }

        if (!shareLoc){
            isValid = false
            Toast.makeText(requireContext(), "Aktifkan Lokasi", Toast.LENGTH_SHORT).show()
        }


        if (isValid){

            //Date
            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd")
            val date = dateFormat.format(calendar.time)

            //InputData
            val crime = inputCrime.text.toString()
            val time: Int = inputTime.text.toString().toInt()

            //Location
            val lat = location!!.latitude
            val lon = location!!.longitude

            //Districts
            val address = Geocoder(requireContext().applicationContext, Locale.getDefault()).getFromLocation(lat, lon, 1)
            val  districts: String = address[0].subAdminArea
            val subdistrict: String = address[0].locality

            //JsonObject
            val crimeJsonObject = JsonObject()
            crimeJsonObject.addProperty("date", date)
            crimeJsonObject.addProperty("time", time)
            crimeJsonObject.addProperty("latitude", lat)
            crimeJsonObject.addProperty("longitude", lon)
            crimeJsonObject.addProperty("districts", districts.replace(' ','_'))
            crimeJsonObject.addProperty("subdistrict", subdistrict.replace(' ','_'))
            crimeJsonObject.addProperty("type", crime)

            //SendData
            crimeReportViewModel.uploadCrimeReport(crimeJsonObject)

            Log.e( "uploadReport: ", crimeJsonObject.toString())
            Toast.makeText(requireContext(), "Laporan terkirim! Selalu waspada & berhati-hati!", Toast.LENGTH_SHORT).show()
            //val snackbar = Snackbar.make(activity.findViewById(R.id.),"Laporan terkirim! Selalu waspada & berhati-hati!", Snackbar.LENGTH_SHORT)
        }
    }


    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ){ isGranted ->
            if (isGranted){
                getCurrentLocation()
            }
        }


    private fun getCurrentLocation(){
        if (ContextCompat.checkSelfPermission(requireContext().applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener { currentLocation ->
                if (currentLocation != null){
                    this.location = currentLocation
                } else {
                    Toast.makeText(requireContext(), "Nyalakan Lokasi", Toast.LENGTH_SHORT).show()
                    binding.switchLoc.isChecked = false
                }
            }
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }



    companion object {
        fun newInstance() = CrimeReportFragment().apply {
            arguments = Bundle().apply { }
        }
    }

}