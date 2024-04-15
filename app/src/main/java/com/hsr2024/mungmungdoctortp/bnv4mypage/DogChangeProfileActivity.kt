package com.hsr2024.mungmungdoctortp.bnv4mypage

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.hsr2024.mungmungdoctortp.R
import com.hsr2024.mungmungdoctortp.databinding.ActivityDogChangeProfileBinding

class DogChangeProfileActivity : AppCompatActivity() {

    private val binding by lazy { ActivityDogChangeProfileBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)




    }
}