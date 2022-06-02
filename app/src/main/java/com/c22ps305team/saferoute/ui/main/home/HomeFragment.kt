package com.c22ps305team.saferoute.ui.main.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.c22ps305team.saferoute.data.Statistic
import com.c22ps305team.saferoute.databinding.FragmentHomeBinding
import com.google.firebase.firestore.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel


    private lateinit var placeInfoData: ArrayList<Statistic>
    private lateinit var placeInfoAdapter: PlaceInfoAdapter
    private lateinit var db: FirebaseFirestore



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*setupViewModel()
        setupObserver()*/
        setupPlaceInfo()
    }

    private fun setupPlaceInfo() {

        placeInfoData = arrayListOf()
        placeInfoAdapter = PlaceInfoAdapter(placeInfoData)

        //setAdapter
        binding.rvArea.apply {
            adapter = placeInfoAdapter
            layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        }

        //Klik = placeInfoAdapter.setOnItemClickCallback()

        //setData
        //placeInfoAdapter.setData(placeInfoData)

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
                    }
                }
                placeInfoAdapter.notifyDataSetChanged()
            }

        })

    }


    /*private fun setupViewModel() {
        homeViewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)
    }

    private fun setupObserver() {
        homeViewModel.text.observe(viewLifecycleOwner) {
            //binding.textHome.text = it
        }
    }*/

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = HomeFragment().apply {
            arguments = Bundle().apply { }
        }
    }
}