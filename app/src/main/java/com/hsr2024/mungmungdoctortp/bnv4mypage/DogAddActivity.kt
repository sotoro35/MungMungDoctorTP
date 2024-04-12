package com.hsr2024.mungmungdoctortp.bnv4mypage

import android.content.Intent
import android.database.Cursor
import android.graphics.Paint
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.loader.content.CursorLoader
import com.bumptech.glide.Glide
import com.hsr2024.mungmungdoctortp.G
import com.hsr2024.mungmungdoctortp.R
import com.hsr2024.mungmungdoctortp.data.AddDog
import com.hsr2024.mungmungdoctortp.databinding.ActivityDogAddBinding
import com.hsr2024.mungmungdoctortp.network.RetrofitCallback
import com.hsr2024.mungmungdoctortp.network.RetrofitProcess
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DogAddActivity : AppCompatActivity() {

    private val binding by lazy { ActivityDogAddBinding.inflate(layoutInflater) }

    var uri:Uri? = null
    var gender = "girl"
    var neutering = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnAddPetImage.paintFlags = Paint.UNDERLINE_TEXT_FLAG // 밑줄

        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.btnAddSave.setOnClickListener { addPet() }
        binding.addPetImage.setOnClickListener { clickImage() }

        binding.addPetBoy.setOnClickListener {
            gender = "boy"
            binding.addPetBoy.setBackgroundResource(R.drawable.bg_button2)
            binding.addPetGirl.setBackgroundResource(R.drawable.bg_button)
        }

        binding.addPetGirl.setOnClickListener {
            gender = "girl"
            binding.addPetBoy.setBackgroundResource(R.drawable.bg_button)
            binding.addPetGirl.setBackgroundResource(R.drawable.bg_button2)
        }

        binding.addPetNeuteringO.setOnClickListener {
            neutering = true
            binding.addPetNeuteringO.setBackgroundResource(R.drawable.bg_button2)
            binding.addPetNeuteringX.setBackgroundResource(R.drawable.bg_button)
        }

        binding.addPetNeuteringX.setOnClickListener {
            neutering = false
            binding.addPetNeuteringX.setBackgroundResource(R.drawable.bg_button2)
            binding.addPetNeuteringO.setBackgroundResource(R.drawable.bg_button)
        }



    }// onCreate


    private fun addPet() {

        //이미지파일을 MutipartBody.Part 로 포장하여 전송: @Part
        val filePart: MultipartBody.Part? = imgPath?.let { //널이 아니면...
            val file = File(it) // 생선손질..
            val requestBody: RequestBody =
                RequestBody.create(MediaType.parse("image/*"), file) // 진공팩포장
            MultipartBody.Part.createFormData("img1", file.name, requestBody) // 택배상자 포장.. == 리턴되는 값
        }

        if (filePart != null){
            RetrofitProcess(this,params=filePart, callback = object : RetrofitCallback {
                override fun onResponseListSuccess(response: List<Any>?) {}

                override fun onResponseSuccess(response: Any?) {
                    val code=(response as String)
                    Log.d("one file upload code","$code") // 실패 시 5404, 성공 시 이미지 경로
                    if (code != "5404") {
                        savePet(code)
                        Toast . makeText (this@DogAddActivity, "이미지+추가성공", Toast.LENGTH_SHORT).show()
                    }else Toast . makeText (this@DogAddActivity, "이미지 업로드 실패", Toast.LENGTH_SHORT).show()
                }
                override fun onResponseFailure(errorMsg: String?) {
                    Log.d("one file upload fail",errorMsg!!) // 에러 메시지
                    Toast.makeText(this@DogAddActivity, "이미지 업로드 에러", Toast.LENGTH_SHORT).show()
                }
            }).onefileUploadRequest()
        }else savePet("")

    }// addPet..

    private fun savePet(image:String){
        var petName = binding.addPetName.text.toString()
        var petbreed = binding.addPetBreed.text.toString()
        var birthdate = SimpleDateFormat("yyyyMMdd", Locale.KOREA).format(Date()).toString()

        if (saveCheck(petName, petbreed, birthdate) == true ) {

            val params= AddDog("${G.user_email}", "${G.user_providerId}",
                "$petName", "$image",
                "$birthdate", "$gender", "${neutering.toString()}",
                "$petbreed", "${G.loginType}")
            RetrofitProcess(this,params=params, callback = object : RetrofitCallback {
                override fun onResponseListSuccess(response: List<Any>?) {}

                override fun onResponseSuccess(response: Any?) {
                    val code=(response as String)
                    Log.d("Add Pet code","$code") //  - 5200 펫 추가 성공, 5201 펫 추가 실패, 4204 서비스 회원 아님
                    Toast.makeText(this@DogAddActivity, "추가성공", Toast.LENGTH_SHORT).show()
                }

                override fun onResponseFailure(errorMsg: String?) {
                    Log.d("Add Pet fail",errorMsg!!) // 에러 메시지
                }

            }).petAddRequest()

        } else AlertDialog.Builder(this).setMessage("관리자에게 문의하세요").create().show()
    }


    private fun saveCheck(petName: String, petbreed: String, birthdate: String): Boolean {

        var boolean = false

        when {
            !petName.isNotEmpty() && !petbreed.isNotEmpty() && !birthdate.isNotEmpty() -> {
                AlertDialog.Builder(this).setMessage("모두 입력해주세요").create().show()
                boolean = false
            }

            else -> boolean = true
        } // when...

        return boolean
    }


    // 이미지의 절대경로를 저장할 멤버변수
    var imgPath: String? = null

    private fun clickImage() {
        // 앱에서 사진 가져오기
        val intent =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Intent(MediaStore.ACTION_PICK_IMAGES)
            else Intent(Intent.ACTION_OPEN_DOCUMENT).setType("image/*")
        resultLauncher.launch(intent)
    }

    // 사진 가져올 대행사..
    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            uri = it.data?.data
            uri?.let {
                Glide.with(this).load(it).into(binding.addPetImage)
                 //uri --> 절대경로
                imgPath = getRealPathFromUri(uri!!)
            }
        }//resultLauncher

    // Uri를 전달받아 실제 파일 경로를 리턴해주는 기능 메소드 구현하기
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
}//main...