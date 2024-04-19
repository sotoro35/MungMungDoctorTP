package com.hsr2024.mungmungdoctortp.bnv4mypage

import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.graphics.Color
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
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.gson.Gson
import com.hsr2024.mungmungdoctortp.G
import com.hsr2024.mungmungdoctortp.R
import com.hsr2024.mungmungdoctortp.data.DeleteDog
import com.hsr2024.mungmungdoctortp.data.ModifyDog
import com.hsr2024.mungmungdoctortp.data.Pet
import com.hsr2024.mungmungdoctortp.databinding.ActivityDogChangeProfileBinding
import com.hsr2024.mungmungdoctortp.network.RetrofitCallback
import com.hsr2024.mungmungdoctortp.network.RetrofitProcess
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DogChangeProfileActivity : AppCompatActivity() {

    private val binding by lazy { ActivityDogChangeProfileBinding.inflate(layoutInflater) }

    lateinit var pet: Pet

    lateinit var petName:String
    lateinit var petbreed:String
    lateinit var birthdate:String


    val glide:String = "http://43.200.163.153/img/"
    var uri: Uri? = null
    var gender = "여아"
    var neutering = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnAddPetImage.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.btnChangeSave.setOnClickListener { changeSave() }
        binding.changePetImage.setOnClickListener { clickImage() }
        binding.btnChangeDelete.setOnClickListener { delete() }
        binding.changePetBirthDate.setOnClickListener { showDatePicker() }

        val s: String? = intent.getStringExtra("pet")
        s?.also {
            pet = Gson().fromJson(it, Pet::class.java)
            load()
        }


        binding.changePetBoy.setOnClickListener {
            gender = "남아"
            binding.changePetBoy.setBackgroundResource(R.drawable.bg_button2)
            binding.changePetGirl.setBackgroundResource(R.drawable.bg_button)
        }

        binding.changePetGirl.setOnClickListener {
            gender = "여아"
            binding.changePetBoy.setBackgroundResource(R.drawable.bg_button)
            binding.changePetGirl.setBackgroundResource(R.drawable.bg_button2)
        }

        binding.changePetNeuteringO.setOnClickListener {
            neutering = true
            binding.changePetNeuteringO.setBackgroundResource(R.drawable.bg_button2)
            binding.changePetNeuteringX.setBackgroundResource(R.drawable.bg_button)
        }

        binding.changePetNeuteringX.setOnClickListener {
            neutering = false
            binding.changePetNeuteringX.setBackgroundResource(R.drawable.bg_button2)
            binding.changePetNeuteringO.setBackgroundResource(R.drawable.bg_button)
        }





    }//onCreate

    private fun load(){
        binding.changePetName.setText(pet.pet_name)
        binding.changePetBreed.setText(pet.pet_breed)
        binding.changePetBirthDate.setText(pet.pet_birthDate)

        if (pet.pet_imageUrl == null || pet.pet_imageUrl == "") {
            binding.changePetImage.setImageResource(R.drawable.pet_image)
        }else Glide.with(this).load("$glide${pet.pet_imageUrl}").into(binding.changePetImage)

        when(pet.pet_neutering){
            "0" -> {
                neutering = false
                binding.changePetNeuteringX.setBackgroundResource(R.drawable.bg_button2)
                binding.changePetNeuteringO.setBackgroundResource(R.drawable.bg_button)
            }
            "1" ->{
                neutering = true
                binding.changePetNeuteringO.setBackgroundResource(R.drawable.bg_button2)
                binding.changePetNeuteringX.setBackgroundResource(R.drawable.bg_button)
            }
        }

        when(pet.pet_gender){
            "남아" -> {
                gender = "남아"
                binding.changePetBoy.setBackgroundResource(R.drawable.bg_button2)
                binding.changePetGirl.setBackgroundResource(R.drawable.bg_button)
            }
            "여아" -> {
                gender = "여아"
                binding.changePetBoy.setBackgroundResource(R.drawable.bg_button)
                binding.changePetGirl.setBackgroundResource(R.drawable.bg_button2)
            }
        }
    }

    private fun changeSave(){

        petName = binding.changePetName.text.toString()
        petbreed = binding.changePetBreed.text.toString()
        birthdate = binding.changePetBirthDate.text.toString().replace("-","")

        var dateFormat = SimpleDateFormat("yyyyMMdd", Locale.KOREA)
        var currentDate = dateFormat.format(Date()).toInt()
        var selectedDate = binding.changePetBirthDate.text.toString().replace("-","").toInt()

        if (currentDate >= selectedDate) {

            binding.btnChangeSave.isEnabled = false
            binding.btnChangeDelete.isEnabled = false
            Glide.with(this).load(R.drawable.loading).into(binding.loading)

            //이미지파일을 MutipartBody.Part 로 포장하여 전송: @Part
            val filePart: MultipartBody.Part? = imgPath?.let { //널이 아니면...
                val file = File(it) // 생선손질..
                val requestBody: RequestBody =
                    RequestBody.create("image/*".toMediaTypeOrNull(), file) // 진공팩포장
                MultipartBody.Part.createFormData(
                    "img1",
                    file.name,
                    requestBody
                ) // 택배상자 포장.. == 리턴되는 값
            }

            if (filePart != null) {

                Log.d("이미지", filePart.toString())

                RetrofitProcess(this, params = filePart, callback = object : RetrofitCallback {
                    override fun onResponseListSuccess(response: List<Any>?) {}

                    override fun onResponseSuccess(response: Any?) {
                        val code = (response as String)
                        Log.d("one file upload code", "$code") // 실패 시 5404, 성공 시 이미지 경로
                        if (code != "5404") save(code)
                    }

                    override fun onResponseFailure(errorMsg: String?) {
                        Log.d("one file upload fail", errorMsg!!) // 에러 메시지
                        Toast.makeText(
                            this@DogChangeProfileActivity,
                            "이미지 업로드 에러",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }).onefileUploadRequest()

            } else save(pet.pet_imageUrl)

        }else AlertDialog.Builder(this).setMessage("현재날짜까지 가능합니다").create().show()

    }

    private fun save(img:String){

        petName = binding.changePetName.text.toString()
        petbreed = binding.changePetBreed.text.toString()
        birthdate = binding.changePetBirthDate.text.toString().replace("-","")

        var dateFormat = SimpleDateFormat("yyyyMMdd", Locale.KOREA)
        var currentDate = dateFormat.format(Date()).toInt()
        var selectedDate = binding.changePetBirthDate.text.toString().replace("-","").toInt()

        if (currentDate >= selectedDate) {

            val params= ModifyDog("${G.user_email}", "${G.user_providerId}",
                "${pet.pet_id}", "$petName", "$img", "$birthdate",
                "$gender", "$neutering", "$petbreed", "${G.loginType}")
            RetrofitProcess(this,params=params, callback = object : RetrofitCallback {
                override fun onResponseListSuccess(response: List<Any>?) {}

                override fun onResponseSuccess(response: Any?) {
                    val code=(response as String)
                    Log.d("Modify Pet code","$code") //  - 5300 펫 수정 성공, 5301 펫 수정 실패, 4204 서비스 회원 아님

                    when(code){
                        "5301" -> Toast.makeText(this@DogChangeProfileActivity, "관리자에게 문의하세요", Toast.LENGTH_SHORT).show()
                        "4204" -> Toast.makeText(this@DogChangeProfileActivity, "관리자에게 문의하세요", Toast.LENGTH_SHORT).show()
                        "5300" -> {
                            Toast.makeText(this@DogChangeProfileActivity, "수정이 완료되었습니다", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }
                }

                override fun onResponseFailure(errorMsg: String?) {
                    Log.d("Modify Pet fail",errorMsg!!) // 에러 메시지
                }

            }).petModifyRequest()
        }else AlertDialog.Builder(this).setMessage("현재날짜까지 가능합니다").create().show()
    }

    private fun delete(){
        val dialog = AlertDialog.Builder(this).setMessage("진짜로 삭제하시겠습니까?")
        dialog.setPositiveButton("네",object :DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                val params= DeleteDog("${G.user_email}", "${G.user_providerId}", "${pet.pet_id}", "${G.loginType}")
                RetrofitProcess(this@DogChangeProfileActivity,params=params, callback = object : RetrofitCallback {
                    override fun onResponseListSuccess(response: List<Any>?) {}
                    override fun onResponseSuccess(response: Any?) {
                        val code=(response as String)
                        Log.d("Delete Pet code","$code") //  - 5400 펫 삭제 성공, 5401 펫 삭제 실패, 4204 서비스 회원 아님
                        if (code == "5400") {
                            Toast.makeText(
                                this@DogChangeProfileActivity,
                                "삭제 완료 되었습니다",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        }
                    }

                    override fun onResponseFailure(errorMsg: String?) {
                        Log.d("Delete Pet fail",errorMsg!!) // 에러 메시지
                    }

                }).petDeleteRequest()
                }

        })

        dialog.create().show()
    }


    // 이미지의 절대경로를 저장할 멤버변수
    var imgPath:String? = null

    private fun clickImage(){
        // 앱에서 사진 가져오기
        val intent = if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.TIRAMISU) Intent(MediaStore.ACTION_PICK_IMAGES)
        else Intent(Intent.ACTION_OPEN_DOCUMENT).setType("image/*")
        resultLauncher.launch(intent)
    }

    // 사진 가져올 대행사..
    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        val uri: Uri? = it.data?.data
        uri?.let {
            Glide.with(this).load(it).into(binding.changePetImage)
            // uri --> 절대경로
            imgPath= getRealPathFromUri(uri)
        }
    }//resultLauncher

    // Uri를 전달받아 실제 파일 경로를 리턴해주는 기능 메소드 구현하기
    private fun getRealPathFromUri(uri: Uri) : String? {

        // android 10 버전 부터는 Uri를 통해 파일의 실제 경로를 얻을 수 있는 방법이 없어졌음
        // 그래서 uri에 해당하는 파일을 복사하여 임시로 파일을 만들고 그 파일의 경로를 이용하여 서버에 전송

        // Uri[미디어저장소의 DB 주소]파일의 이름을 얻어오기 - DB SELECT 쿼리작업을 해주는 기능을 가진 객체를 이용
        val cursorLoader: CursorLoader = CursorLoader(this,uri, null,null,null,null)
        val cursor: Cursor? = cursorLoader.loadInBackground()
        val fileName:String? = cursor?.run {
            moveToFirst()
            getString( getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))
        } // -------------------------------------------------------------------

        // 복사본이 저장될 파일의 경로와 파일명.확장자
        val file: File = File(externalCacheDir,fileName)

        // 이제 진짜 복사 작업 수행
        val inputStream: InputStream = contentResolver.openInputStream(uri) ?: return null
        val outputStream: OutputStream = FileOutputStream(file)

        // 파일복사
        while (true){
            val buf: ByteArray = ByteArray(1024) // 빈 바이트 배열[길이:1KB]
            val len:Int= inputStream.read(buf) // 스트림을 통해 읽어들인 바이트들을 buf 배열에 넣어줌 -- 읽어드린 바이트 수를 리턴해 줌
            if (len <= 0 ) break
            outputStream.write(buf, 0, len) // 덮어쓰기가 아님..
            // offset(오프셋-편차) 0을주면 0번부터 1024가 아님.. 0~1023 번 다음은 편차를 주지말고 1024 ~ 로 주라는 의미임
            // 1024길이만큼 가져오는데.. 편차없이 1024 길이만큼 받다가 읽어드린 바이트(len)의 값만큼 쓰라는 의미임..

        }// while

        // 반복문이 끝났으면 복사가 완료된 것임

        inputStream.close()
        outputStream.close()

        return file.absolutePath
    }////////////////////////////////////////////////////////////////////////////

    private fun showDatePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("날짜 선택")
            .build()

        datePicker.addOnPositiveButtonClickListener { selection ->
            val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            binding.changePetBirthDate.text = dateFormatter.format(Date(selection))
            binding.changePetBirthDate.setTextColor(Color.BLACK)
        }

        datePicker.show(supportFragmentManager, "DATE_PICKER")
    }

}