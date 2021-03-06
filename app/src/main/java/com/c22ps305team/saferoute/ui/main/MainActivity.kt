package com.c22ps305team.saferoute.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.c22ps305team.saferoute.R
import com.c22ps305team.saferoute.databinding.ActivityMainBinding
import com.c22ps305team.saferoute.ui.main.home.HomeFragment
import com.c22ps305team.saferoute.ui.main.location.MapsFragment
import com.c22ps305team.saferoute.ui.main.makeRoute.MakeRouteActivity
import com.c22ps305team.saferoute.ui.main.profile.ProfileFragment
import com.c22ps305team.saferoute.ui.main.reportCrime.CrimeReportFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setupNavBar()
    }

    private fun setupNavBar() {
        val bottomNavView: BottomNavigationView = binding.navView
        bottomNavView.background = null
        bottomNavView.menu.getItem(2).isEnabled = false

        binding.fabMakeRoute.setOnClickListener {
            val intentToActivityMakeRoute = Intent(this@MainActivity, MakeRouteActivity::class.java)
            startActivity(intentToActivityMakeRoute)
        }

        initHomeFragment(HomeFragment.newInstance())

        bottomNavView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> replaceFragment(HomeFragment.newInstance())
                R.id.navigation_maps -> replaceFragment(MapsFragment.newInstance())
                R.id.navigation_report -> replaceFragment(CrimeReportFragment.newInstance())
                R.id.navigation_profile -> replaceFragment(ProfileFragment.newInstance())
            }
            return@setOnItemSelectedListener true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager.beginTransaction()
        fragmentManager.replace(R.id.frameContainer, fragment)
            .addToBackStack(Fragment::class.java.simpleName).commit()
    }

    private fun initHomeFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager.beginTransaction()
        fragmentManager.replace(R.id.frameContainer, fragment)
            .addToBackStack(Fragment::class.java.simpleName).commit()
    }


    companion object {
        const val EXTRA_USER = "extra_user"
    }

}

