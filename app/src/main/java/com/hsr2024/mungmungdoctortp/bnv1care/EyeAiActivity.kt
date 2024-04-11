package com.hsr2024.mungmungdoctortp.bnv1care

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
import com.hsr2024.mungmungdoctortp.R
import com.hsr2024.mungmungdoctortp.databinding.ActivityEyeAiBinding

class EyeAiActivity : AppCompatActivity() {

    private val binding by lazy { ActivityEyeAiBinding.inflate(layoutInflater) }

    var uri:Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.aiEyeCamera.setOnClickListener {
            startActivity(Intent(this,AiResultActivity::class.java))
            clickCamera()
        }
        binding.aiEyeGallery.setOnClickListener { clickGallery() }

    }// onCreate

    private fun clickCamera(){

    }

    private fun clickGallery(){
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.TIRAMISU) resultLauncher.launch(Intent(MediaStore.ACTION_PICK_IMAGES))
        else resultLauncher.launch(Intent(Intent.ACTION_OPEN_DOCUMENT).setType("image/*"))

    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == RESULT_OK){
            val uri = it.data?.data
            if (uri != null){


            }
        }
    }

    //if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.TIRAMISU) resultLauncher.launch(Intent(MediaStore.ACTION_PICK_IMAGES))
    //        else resultLauncher.launch(Intent(Intent.ACTION_OPEN_DOCUMENT).setType("image/*"))
    //    }
    //
    //    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
    //        if (it.resultCode == RESULT_OK){
    //            val uri = it.data?.data
    //            if (uri != null){
    //                binding.iv.setImageURI(uri)
    //                // 선택한 이미지에 대한 Object Detection 수행
    //                detectObjectFromUri(uri)
    //            }
    //        }
    //    }
}//main