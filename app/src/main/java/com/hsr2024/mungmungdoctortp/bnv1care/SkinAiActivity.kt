package com.hsr2024.mungmungdoctortp.bnv1care

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
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
import androidx.loader.content.CursorLoader
import com.hsr2024.mungmungdoctortp.R
import com.hsr2024.mungmungdoctortp.databinding.ActivitySkinAiBinding
import com.takusemba.cropme.CropLayout
import com.takusemba.cropme.OnCropListener
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
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

    var imgPath: String? = null
    lateinit var cropbitmap: Bitmap
    lateinit var file:File

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
                    val image = dialogV.findViewById<CropLayout>(R.id.test_select_image)
                    image.setUri(uri!!)


                    val builder = AlertDialog.Builder(this@SkinAiActivity)
                    builder.setView(dialogV)
                    imgPath = getRealPathFromUri(uri!!)
                    alertDialog = builder.create()

                    dialogV.findViewById<TextView>(R.id.test_close).setOnClickListener { alertDialog.dismiss() }
                    dialogV.findViewById<TextView>(R.id.test_start).setOnClickListener {
                        image.crop()
                        image.addOnCropListener(object : OnCropListener{
                            override fun onFailure(e: Exception) {
                                Toast.makeText(this@SkinAiActivity, "관리자에게문의하세요", Toast.LENGTH_SHORT).show()
                                Log.d("크롭에러","${e.message}")
                            }

                            override fun onSuccess(bitmap: Bitmap) {
                                cropbitmap = bitmap
                                savefile(cropbitmap)
                                val intent = Intent(this@SkinAiActivity,AiResultActivity::class.java)
                                //intent.putExtra("aiSkinImg",uri.toString())
                                //intent.putExtra("aiSkinImg2",imgPath)
                                intent.putExtra("aiSkinImg",file.absolutePath.toString())
                                startActivity(intent)
                                finish()
                            }

                        })

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
                val image = dialogV.findViewById<CropLayout>(R.id.test_select_image)
                image.setUri(uri!!)
                val builder = AlertDialog.Builder(this)
                builder.setView(dialogV)
                alertDialog = builder.create()
                imgPath = getRealPathFromUri(uri!!)

                dialogV.findViewById<TextView>(R.id.test_close).setOnClickListener { alertDialog.dismiss() }
                dialogV.findViewById<TextView>(R.id.test_start).setOnClickListener {
                    image.crop()
                    image.addOnCropListener(object :OnCropListener{
                        override fun onFailure(e: Exception) {
                            Toast.makeText(this@SkinAiActivity, "관리자에게문의하세요", Toast.LENGTH_SHORT).show()
                            Log.d("크롭에러","${e.message}")
                        }

                        override fun onSuccess(bitmap: Bitmap) {
                            val intent = Intent(this@SkinAiActivity,AiResultActivity::class.java)
                            //intent.putExtra("aiSkinImg",uri.toString())
                            //intent.putExtra("aiSkinImg2",imgPath)
                            intent.putExtra("aiSkinImg",file.absolutePath.toString())
                            startActivity(intent)
                            finish()
                        }

                    })

                }

                alertDialog.show()

            }
        }
    }

    private fun savefile(bitmap: Bitmap){
        // 내장 저장공간의 외부 저장소 중에서 공용영역에 저장 - 앱을 삭제해도 파일은 남아 있는 영역

        // 공용영역의 경로부터
        val path: File = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        // 경로는 정해졌으니 저장될 파일의 이름과 확장자 정하기 - 중복되지 않도록 날짜를 이용하여 명명
        val sdf: android.icu.text.SimpleDateFormat =
            android.icu.text.SimpleDateFormat("yyyyMMddHHmmss")
        val fileName:String = "IMG_"+sdf.format(Date()) + ".jpg" //"IMG_20240219143924.jpg

        // 경로와 파일명을 결합
        file = File(path, fileName)

        // stream에서는 경로의 폴더가 있으면 냅두고 없으면 만들는 기능이 있기에.. 미리 만들어둘 필요가 없음
        //file.createNewFile()

        // 파일을 스트림에 넣어줌
        val outputStream = FileOutputStream(file)

        // 비트맵 압축
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream)

        outputStream.flush()
        outputStream.close()

        // 여기까지 경로가 잘 되었는지 확인
        //AlertDialog.Builder(this).setMessage(file.toString()).create().show()

    }


    private fun getRealPathFromUri(uri: Uri): String? {

        // android 10 버전 부터는 Uri를 통해 파일의 실제 경로를 얻을 수 있는 방법이 없어졌음
        // 그래서 uri에 해당하는 파일을 복사하여 임시로 파일을 만들고 그 파일의 경로를 이용하여 서버에 전송

        // Uri[미디어저장소의 DB 주소]파일의 이름을 얻어오기 - DB SELECT 쿼리작업을 해주는 기능을 가진 객체를 이용
        val cursorLoader: CursorLoader = CursorLoader(this, uri, null, null, null, null)
        val cursor: Cursor? = cursorLoader.loadInBackground()
        val fileName: String? = cursor?.run {
            moveToFirst()
            getString(getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))
        } // -------------------------------------------------------------------

        // 복사본이 저장될 파일의 경로와 파일명.확장자
        val file: File = File(externalCacheDir, fileName)

        // 이제 진짜 복사 작업 수행
        val inputStream: InputStream = contentResolver.openInputStream(uri) ?: return null
        val outputStream: OutputStream = FileOutputStream(file)

        // 파일복사
        while (true) {
            val buf: ByteArray = ByteArray(1024) // 빈 바이트 배열[길이:1KB]
            val len: Int =
                inputStream.read(buf) // 스트림을 통해 읽어들인 바이트들을 buf 배열에 넣어줌 -- 읽어드린 바이트 수를 리턴해 줌
            if (len <= 0) break
            outputStream.write(buf, 0, len) // 덮어쓰기가 아님..
            // offset(오프셋-편차) 0을주면 0번부터 1024가 아님.. 0~1023 번 다음은 편차를 주지말고 1024 ~ 로 주라는 의미임
            // 1024길이만큼 가져오는데.. 편차없이 1024 길이만큼 받다가 읽어드린 바이트(len)의 값만큼 쓰라는 의미임..

        }// while

        // 반복문이 끝났으면 복사가 완료된 것임

        inputStream.close()
        outputStream.close()

        return file.absolutePath
    }////////////////////////////////////////////////////////////////////////////

}