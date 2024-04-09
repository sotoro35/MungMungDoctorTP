package com.hsr2024.mungmungdoctortp.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.hsr2024.mungmungdoctortp.R
import com.hsr2024.mungmungdoctortp.bnv1care.CareFragment
import com.hsr2024.mungmungdoctortp.bnv2map.MapFragment
import com.hsr2024.mungmungdoctortp.bnv3community.CommunityFragment
import com.hsr2024.mungmungdoctortp.databinding.ActivityMainBinding
import com.naver.maps.map.util.FusedLocationSource

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }









    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.bnvView.itemIconTintList = null // 아이콘 색 넣으려고 설정..


        // [바텀네비 별로 프래그먼트 보이도록 설정]
        supportFragmentManager.beginTransaction().add((R.id.container_fragment),CareFragment()).commit()

        binding.bnvView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_care -> supportFragmentManager.beginTransaction().replace((R.id.container_fragment),CareFragment()).commit()
                R.id.menu_map -> supportFragmentManager.beginTransaction().replace((R.id.container_fragment),MapFragment()).commit()
                R.id.menu_community -> supportFragmentManager.beginTransaction().replace((R.id.container_fragment),CommunityFragment()).commit()
            }
           true
            }


    } // onCreate...




















} //main...
