package com.hsr2024.mungmungdoctortp.bnv3community

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.hsr2024.mungmungdoctortp.R
import com.hsr2024.mungmungdoctortp.databinding.ActivityFeedBinding

class FeedActivity : AppCompatActivity() {

    private val binding by lazy { ActivityFeedBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.iv.setOnClickListener { openGallery() }
        binding.tvRegister.setOnClickListener { clickregister() }

    }

    private fun openGallery(){
        val intent: Intent = if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.TIRAMISU) Intent(
            MediaStore.ACTION_PICK_IMAGES) else Intent(Intent.ACTION_OPEN_DOCUMENT).setType("*image/*")
        resultLauncher.launch(intent)
    }
    private  val resultLauncher= registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        imgUri= it.data?.data
        Glide.with(this).load(imgUri).into(binding.iv)
    }
    var imgUri: Uri?=null

    private fun clickregister(){


    }
}