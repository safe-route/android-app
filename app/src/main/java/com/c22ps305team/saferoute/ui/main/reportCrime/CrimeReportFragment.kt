package com.c22ps305team.saferoute.ui.main.reportCrime

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.c22ps305team.saferoute.databinding.FragmentCrimeReportBinding


class CrimeReportFragment : Fragment() {

    private var _binding: FragmentCrimeReportBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentCrimeReportBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }







    companion object {
        fun newInstance() = CrimeReportFragment().apply {
            arguments = Bundle().apply { }
        }
    }

}