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
import com.hsr2024.mungmungdoctortp.data.UserChange
import com.hsr2024.mungmungdoctortp.databinding.ActivityChangeProfileBinding
import com.hsr2024.mungmungdoctortp.network.RetrofitCallback
import com.hsr2024.mungmungdoctortp.network.RetrofitProcess
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

class ChangeProfileActivity : AppCompatActivity() {

    val imgUrl= "https://43.200.163.153/img/${G.user_imageUrl}"

    private val binding by lazy { ActivityChangeProfileBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnChangeUserImage.paintFlags = Paint.UNDERLINE_TEXT_FLAG // 밑줄
        binding.changeUserNickname.text = G.user_nickname
        binding.userEmailProfile.text = G.user_email

        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.changeUserImage.setOnClickListener { clickImage() }
        binding.btnChangeProfile.setOnClickListener { changeProfile() }

        if (G.user_imageUrl.isNotEmpty()) {
            Glide.with(this).load("https://43.200.163.153/img/${G.user_imageUrl}").into(binding.changeUserImage)
        }


    }// onCreate

    var password:String = ""
    var passwordcon:String = ""
    private fun changeProfile(){

        password = binding.changePassword.editText!!.text.toString()
        passwordcon = binding.changePasswordCon.editText!!.text.toString()

        //이미지파일을 MutipartBody.Part 로 포장하여 전송: @Part
        val filePart: MultipartBody.Part? = imgPath?.let { //널이 아니면...
            val file= File(it) // 생선손질..
            val requestBody: RequestBody = RequestBody.create(MediaType.parse("image/*"),file) // 진공팩포장
            MultipartBody.Part.createFormData("img1", file.name,requestBody) // 택배상자 포장.. == 리턴되는 값
        }

        if (saveCheck(password,passwordcon)){

            if (filePart != null){
                saveChage("$filePart")
            }else saveChage("")

        }else AlertDialog.Builder(this).setMessage("관리자에게 문의하세요").create().show()

    }

    private fun saveChage(image:String){
        val params= UserChange("${G.user_email}", "$password", "${G.user_providerId}", "$image", "${G.loginType}")
        RetrofitProcess(this,params=params, callback = object : RetrofitCallback {
            override fun onResponseListSuccess(response: List<Any>?) {}

            override fun onResponseSuccess(response: Any?) {
                val code=(response as String)
                Log.d("User modify code","$code") // 1220 회원 정보 수정 성공, 1230 회원 정보 수정 실패, 4204 서비스 회원 아님
                when(code){
                    "4004" -> Toast.makeText(this@ChangeProfileActivity, "회원이 아닙니다", Toast.LENGTH_SHORT).show()
                    "1230" -> Toast.makeText(this@ChangeProfileActivity, "관리자에게 문의하세요", Toast.LENGTH_SHORT).show()
                    "1220" -> {
                        Toast.makeText(this@ChangeProfileActivity, "정보가 변경되었습니다", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }

            override fun onResponseFailure(errorMsg: String?) {
                Log.d("User modify fail",errorMsg!!) // 에러 메시지
            }

        }).userModifyRequest()
    }


    private fun saveCheck(password:String,passwordConfirm:String) : Boolean {

        var boolean = false

        when{

            password != passwordConfirm -> {
                AlertDialog.Builder(this).setMessage("패스워드가 다릅니다.다시 확인해주세요").create().show()
                boolean = false
            }

            password.length in 1..3 -> {
                AlertDialog.Builder(this).setMessage("비밀번호가 너무 짧습니다").create().show()
                boolean = false
            }

            password.contains(" ") -> {
                AlertDialog.Builder(this).setMessage("띄어쓰기는 사용할 수 없습니다").create().show()
                boolean = false
            }

            !password.isNotEmpty() && !passwordConfirm.isNotEmpty() -> {
                AlertDialog.Builder(this).setMessage("모두 입력해주세요").create().show()
                boolean = false
            }

            else -> boolean = true
        } // when...

        return boolean
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
            Glide.with(this).load(it).into(binding.changeUserImage)

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
}