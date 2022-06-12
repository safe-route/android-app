package com.c22ps305team.saferoute.ui.main.home

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.c22ps305team.saferoute.data.InfoTips
import com.c22ps305team.saferoute.data.InfoTipsData
import com.c22ps305team.saferoute.data.Statistic
import com.c22ps305team.saferoute.databinding.FragmentHomeBinding
import com.c22ps305team.saferoute.ui.main.detail.DetailPlaceActivity
import com.c22ps305team.saferoute.ui.main.detail.DetailTipsActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import com.google.type.LatLng
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeViewModel: HomeViewModel

    private lateinit var placeInfoData: ArrayList<Statistic>
    private lateinit var placeInfoAdapter: PlaceInfoAdapter
    private lateinit var db: FirebaseFirestore

    private lateinit var fusedLocationClient: FusedLocationProviderClient


    private lateinit var tipsInfoAdapter: TipsInfoAdapter
    private lateinit var listInfoTips: ArrayList<InfoTips>

    //private lateinit var cityName: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())


        /*setupViewModel()
        setupObserver()*/


        getCurrentLocation()



        setupPlaceInfo()
        setupTipsInfo()

        setupBanner()


    }



    override fun onResume() {
        super.onResume()

        getCurrentLocation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
        if (ContextCompat.checkSelfPermission(requireContext(),  android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED){
            fusedLocationClient.lastLocation.addOnSuccessListener { currentLocation ->
                if (currentLocation != null){
                    val lat = currentLocation.latitude
                    val long = currentLocation.longitude
                    //Log.e("getCurrentLocation: ", " lat = $lat long = $long" )
                    //PERBAIKI KETIKA LATLONG == NULL
                    getCityName(lat, long)
                } else {
                    Toast.makeText(requireContext(), "Turn on your location!!", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }


    private fun getCityName(lat: Double, long: Double) {
        val geoCoder = Geocoder(requireContext().applicationContext, Locale.getDefault())
        val address = geoCoder.getFromLocation(lat, long, 1)
        val cityName = address[0].locality

        //cityName.replace("Kecamatan",".")
        binding.tvAreaName.text = cityName
        //Log.e("getCityName: ", cityName)
    }


    private fun setupTipsInfo(){
        listInfoTips = arrayListOf()
        listInfoTips.addAll(InfoTipsData.listData)
        tipsInfoAdapter = TipsInfoAdapter(listInfoTips)

        binding.rvTips.apply {
            adapter = tipsInfoAdapter
            layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        }

        tipsInfoAdapter.setOnItemClickCallback(object : TipsInfoAdapter.OnItemClickCallback{
            override fun onItemClicked(infoTips: InfoTips) {
                val intent = Intent(requireContext(), DetailTipsActivity::class.java)
                intent.putExtra(DetailTipsActivity.EXTRA_TIPS, infoTips)
                startActivity(intent)
            }
        })
    }


    private fun setupPlaceInfo() {
        placeInfoData = arrayListOf()
        placeInfoAdapter = PlaceInfoAdapter(placeInfoData)

        //setAdapter
        binding.rvArea.apply {
            adapter = placeInfoAdapter
            layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        }

        //Klik
        placeInfoAdapter.setOnItemClickCallback(object: PlaceInfoAdapter.OnItemClickCallback{
            override fun onItemClicked(statistic: Statistic) {
                val intent = Intent(requireContext(), DetailPlaceActivity::class.java)
                intent.putExtra(DetailPlaceActivity.EXTRA_STATS, statistic)
                //Log.e("onItemClicked: ", statistic.crime_info.toString())
                startActivity(intent)
            }
        })

        eventChangeListener()
    }


    private fun eventChangeListener() {
        db = FirebaseFirestore.getInstance()
        db.collection("statistic").addSnapshotListener(object : EventListener<QuerySnapshot>{
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null){
                    Log.e("Firestore error", error.message.toString())
                    return
                }
                for (dc: DocumentChange in value?.documentChanges!!){
                    if (dc.type == DocumentChange.Type.ADDED){
                        placeInfoData.add(dc.document.toObject(Statistic::class.java))
                        //Log.e("get data", value.toString())
                    }
                }
                placeInfoAdapter.notifyDataSetChanged()
            }
        })
    }


    private fun setupBanner() {
        val banner = binding.bannerArea
        banner.setCardBackgroundColor(Color.parseColor("#9AEFD3"))

        //binding.tvAreaName.text = cityName
        //val description = binding.tvDescArea
        //val image = binding.ivSafe
    }


    /*private fun setupViewModel() {
        homeViewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)
    }

    private fun setupObserver() {
        homeViewModel.text.observe(viewLifecycleOwner) {
            //binding.textHome.text = it
        }
    }*/



    companion object {
        fun newInstance() = HomeFragment().apply {
            arguments = Bundle().apply { }
        }
    }

}