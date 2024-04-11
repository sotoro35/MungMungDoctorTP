package com.hsr2024.mungmungdoctortp.bnv1care

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
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

    lateinit var alertDialog: AlertDialog
    private fun clickGallery(){
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.TIRAMISU) resultLauncher.launch(Intent(MediaStore.ACTION_PICK_IMAGES))
        else resultLauncher.launch(Intent(Intent.ACTION_OPEN_DOCUMENT).setType("image/*"))

    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == RESULT_OK){
            val uri = it.data?.data
            if (uri != null){

                val dialogV = layoutInflater.inflate(R.layout.dialog_ai_image,null)
                val image = dialogV.findViewById<ImageView>(R.id.test_select_image)
                image.setImageURI(uri)
                val builder = AlertDialog.Builder(this)
                builder.setView(dialogV)
                alertDialog = builder.create()
                alertDialog.show()

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