package com.c22ps305team.saferoute.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.c22ps305team.saferoute.ui.auth.login.LoginActivity
import com.c22ps305team.saferoute.ui.main.MainActivity
import com.c22ps305team.saferoute.utils.ViewModelFactory

@SuppressLint("CustomSplashScreen")
class SplashActivity: AppCompatActivity() {

    private val viewModel: SplashViewModel by viewModels{
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        validateUser()
    }



    private fun validateUser() {
        viewModel.fetchUser().observe(this){ token ->
            if (token.isNullOrEmpty()){
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                finish()
            } else {
                startActivity(
                    Intent(this@SplashActivity, MainActivity::class.java).putExtra(
                        MainActivity.EXTRA_USER, token))
                finish()

            }
            
        }

    }


}