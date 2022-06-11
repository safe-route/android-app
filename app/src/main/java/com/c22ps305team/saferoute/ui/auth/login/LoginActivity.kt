package com.c22ps305team.saferoute.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.c22ps305team.saferoute.data.auth.AuthRequest
import com.c22ps305team.saferoute.data.auth.LoginResponse
import com.c22ps305team.saferoute.databinding.ActivityLoginBinding
import com.c22ps305team.saferoute.ui.auth.register.RegisterActivity
import com.c22ps305team.saferoute.ui.main.MainActivity
import com.c22ps305team.saferoute.utils.Result
import com.c22ps305team.saferoute.utils.ViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val loginViewModel: LoginViewModel by viewModels(){
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListener()
        setupObserver()
    }

    private fun setupListener() {
        binding.buttonSubmit.setOnClickListener {
            val username = binding.edtUsername.text.toString().trim()
            val password = binding.edtPassword.text.toString().trim()
            when {
                username.isEmpty() -> {
                    //error("Must Be Filled!")
                }
                password.length < 3 -> {
                    //error("Password is not valid")
                }
                else -> {
                    //val login = AuthRequest(username, password)
                    loginViewModel.login(username,password)
                    loginViewModel.saveUser(username)
                    //Log.e( "Login: ", username )
                }
            }
        }
        binding.registerButton.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun setupObserver() {
        loginViewModel.loginResponse.observe(this) { loginResponse ->
            when (loginResponse) {
                is Result.Loading -> {
                    onLoading(true)
                }
                is Result.Success -> loginResponse.let {
                    onLoading(false)
                    onSuccess(it.data!!)
                }
                is Result.Error -> loginResponse.data.let {
                    onLoading(false)
                    onFailed()
                }
            }
        }
    }

    private fun onLoading(isLoading: Boolean) {
        /*if (isLoading){
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }*/
    }

    private fun onSuccess(loginResponse: LoginResponse) {
        val userData = loginResponse.token
        loginViewModel.saveToken(userData)
        //Toast.makeText(this, getString(R.string.login_success, loginResult.name), Toast.LENGTH_LONG).show()
        Log.e( "onSuccess: ", userData )
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        intent.putExtra(MainActivity.EXTRA_USER, userData)
        startActivity(intent)
        finish()
    }

    private fun onFailed() {
        //Snackbar.make(binding.root, getString(R.string.login_failed), Snackbar.LENGTH_LONG).show()
    }


}