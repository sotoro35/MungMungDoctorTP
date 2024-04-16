package com.hsr2024.mungmungdoctortp.bnv1care

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.hsr2024.mungmungdoctortp.R
import com.hsr2024.mungmungdoctortp.databinding.ActivitySkinAiBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SkinAiActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySkinAiBinding.inflate(layoutInflater) }

    var uri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.aiSkinCamera.setOnClickListener { clickCamera() }
        binding.aiSkinGallery.setOnClickListener { clickGallery() }
        binding.previewCapture.setOnClickListener { capture() }

        binding.previewLine.visibility = View.GONE
        binding.preview.visibility = View.GONE
        binding.previewCapture.visibility = View.GONE
        binding.skinLinear.visibility = View.VISIBLE
    }// onCreate

    val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){
        if (it) startCameraPreview()
        else Toast.makeText(this, "카메라 권한을 허용해주세요", Toast.LENGTH_SHORT).show()
    }


    private fun clickCamera(){
        // 카메라 사용에 대한 퍼미션 체크 - 퍼미션 요청 or 카메라 프리뷰 시작
        if ( checkSelfPermission(Manifest.permission.CAMERA ) != PackageManager.PERMISSION_GRANTED ) permissionLauncher.launch(
            Manifest.permission.CAMERA)
        else startCameraPreview()

    }// clickCamera....

    var imageCapture: ImageCapture? =null // 프리뷰 작업이 시작될때 객체 생성

    private fun startCameraPreview(){
        binding.previewLine.visibility = View.VISIBLE
        binding.preview.visibility = View.VISIBLE
        binding.previewCapture.visibility = View.VISIBLE
        binding.skinLinear.visibility = View.GONE

        // 카메라 제공자(관리자) 객체 얻어오기 - [비동기 처리 - 준비가 완료되면 객체를 얻어올 수 있음]
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            // 카메라 기능 제공자 객체 얻기
            val cameraProvider = cameraProviderFuture.get()

            //프리뷰 객체 생성
            val preview = Preview.Builder().build()
            preview.setSurfaceProvider(binding.preview.surfaceProvider)


            // 카메라 선택 [ Front , Back ]
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            cameraProvider.unbindAll()

            // 이미지 캡처가 포함된 작업 시작
            imageCapture= ImageCapture.Builder().build()
            cameraProvider.bindToLifecycle(this,cameraSelector, preview,imageCapture)

        }, ContextCompat.getMainExecutor(this))
    }//startCameraPreview...

    private fun capture(){
        imageCapture ?: return

        // 촬영한 사진을 저장
        val name:String = SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA).format(Date())
        val contentValues = ContentValues()
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME,name) //파일명
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg") //파일타입
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH,"Pictures/MungDoctor")
        }

        // 촬영한 사진을 저장학 객체
        val outputFileOptions = ImageCapture.OutputFileOptions.Builder(
            contentResolver, MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues).build()
        imageCapture!!.takePicture(outputFileOptions,ContextCompat.getMainExecutor(this),object :
            ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {

                uri = outputFileResults.savedUri

                if (uri != null){
                    val dialogV = layoutInflater.inflate(R.layout.dialog_ai_image,null)
                    val image = dialogV.findViewById<ImageView>(R.id.test_select_image)
                    image.setImageURI(uri)
                    val builder = AlertDialog.Builder(this@SkinAiActivity)
                    builder.setView(dialogV)
                    alertDialog = builder.create()

                    dialogV.findViewById<TextView>(R.id.test_close).setOnClickListener { alertDialog.dismiss() }
                    dialogV.findViewById<TextView>(R.id.test_start).setOnClickListener {
                        val intent = Intent(this@SkinAiActivity,AiResultActivity::class.java)
                        intent.putExtra("aiSkinImg",uri.toString())
                        startActivity(intent)
                        finish()
                    }
                    alertDialog.show()

                }
            }
            override fun onError(exception: ImageCaptureException) {
                Toast.makeText(this@SkinAiActivity, "fail", Toast.LENGTH_SHORT).show()
            }
        })

    }




    lateinit var alertDialog: AlertDialog
    private fun clickGallery(){
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.TIRAMISU) resultLauncher.launch(
            Intent(
                MediaStore.ACTION_PICK_IMAGES)
        )
        else resultLauncher.launch(Intent(Intent.ACTION_OPEN_DOCUMENT).setType("image/*"))

    }//clickGallery...

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == RESULT_OK){
            uri = it.data?.data
            if (uri != null){

                val dialogV = layoutInflater.inflate(R.layout.dialog_ai_image,null)
                val image = dialogV.findViewById<ImageView>(R.id.test_select_image)
                image.setImageURI(uri)
                val builder = AlertDialog.Builder(this)
                builder.setView(dialogV)
                alertDialog = builder.create()

                dialogV.findViewById<TextView>(R.id.test_close).setOnClickListener { alertDialog.dismiss() }
                dialogV.findViewById<TextView>(R.id.test_start).setOnClickListener {
                    val intent = Intent(this,AiResultActivity::class.java)
                    intent.putExtra("aiSkinImg",uri.toString())
                    startActivity(intent)
                    finish()
                }

                alertDialog.show()

            }
        }
    }

}