package com.c22ps305team.saferoute.ui.main.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.c22ps305team.saferoute.databinding.FragmentProfileBinding
import com.c22ps305team.saferoute.ui.auth.login.LoginActivity
import com.c22ps305team.saferoute.utils.ViewModelFactory

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!


    private val viewModel: ProfileViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObserver()
        setupListener()
    }

    private fun setupListener() {
        binding.btnLogout.setOnClickListener {
            viewModel.deleteUser()
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
            activity?.finish()
        }
    }


    private fun setupObserver() {
        viewModel.fetchUser().observe(requireActivity()){
            binding.tvName.text = it
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = ProfileFragment().apply {
            arguments = Bundle().apply { }
        }
    }
}