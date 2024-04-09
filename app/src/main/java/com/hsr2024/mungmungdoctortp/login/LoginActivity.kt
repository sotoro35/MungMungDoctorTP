package com.hsr2024.mungmungdoctortp.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.hsr2024.mungmungdoctortp.R
import com.hsr2024.mungmungdoctortp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener { login() }
        binding.btnLoginKakao.setOnClickListener { loginKakao() }
        binding.btnLoginNaver.setOnClickListener { loginNaver() }
        binding.btnSignup.setOnClickListener { startActivity(Intent(this,SignupActivity::class.java)) }

    }

    private fun login(){

    }

    private fun loginKakao(){

    }

    private fun loginNaver(){

    }
}