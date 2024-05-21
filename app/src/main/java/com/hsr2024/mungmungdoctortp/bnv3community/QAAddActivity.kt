package com.hsr2024.mungmungdoctortp.bnv3community

import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.loader.content.CursorLoader
import com.bumptech.glide.Glide
import com.hsr2024.mungmungdoctortp.G
import com.hsr2024.mungmungdoctortp.data.AddorModifyorDeleteQA
import com.hsr2024.mungmungdoctortp.databinding.ActivityQaaddBinding
import com.hsr2024.mungmungdoctortp.main.CommentListAdapter
import com.hsr2024.mungmungdoctortp.network.RetrofitCallback
import com.hsr2024.mungmungdoctortp.network.RetrofitHelper
import com.hsr2024.mungmungdoctortp.network.RetrofitProcess
import com.hsr2024.mungmungdoctortp.network.RetrofitService
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

class QAAddActivity : AppCompatActivity() {

    private val binding by lazy { ActivityQaaddBinding.inflate(layoutInflater) }
    private val commentAdapter: CommentListAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.iv.setOnClickListener { openGallery() }
        binding.tvRegister.setOnClickListener { clickregister() }


    }

    private fun openGallery() {
        val intent: Intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Intent(
            MediaStore.ACTION_PICK_IMAGES
        ) else Intent(Intent.ACTION_OPEN_DOCUMENT).setType("*image/*")
        resultLauncher.launch(intent)
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            imgUri = it.data?.data
            Glide.with(this).load(imgUri).into(binding.iv)
            imgPath = getRealPathFromUri(imgUri!!)
        }
    var imgUri: Uri? = null
    var imgPath: String?= null

    //uri를 전달받아 실제 파일 경로를 리턴해주는 기능 메소드 구현하기
    private fun getRealPathFromUri(uri: Uri): String? {

        //android 10 버전 부터는 uri를 통해 파일의 실제 경로를 얻을 수 있는 방법이 없어졌음.
        //그래서 uri에 해당하는 파일을 복사하여 임시로 파일을 만들고 그 파일의 경로를 이용하여 서버에 전송

        //uri[미디어 저장소의 DB 주소]로 부터 파일의 이름을 얻어오기 -DB SELECT 쿼리작업을 해주는 기능을 가진 객체를 이용
        val cursorLoader: CursorLoader = CursorLoader(this, uri, null, null, null, null)
        val cursor: Cursor? = cursorLoader.loadInBackground()
        val fileName: String? = cursor?.run {
            moveToFirst()
            getString(getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))
        }


        //복사본이 저장될 파일의 경로와 파일명.확장자
        val file: File = File(externalCacheDir, fileName)


        //이제 진자 복사 작업 수행
        val inputStream: InputStream = contentResolver.openInputStream(uri) ?: return null
        val outputStream: OutputStream = FileOutputStream(file)

        //파일복사
        while (true) {
            val buf: ByteArray = ByteArray(1024) //빈 바이트 배열[길이:1KB]
            val len: Int = inputStream.read(buf)//스트림을 통해 읽어들인 바이트들을 buf 배열에 넣어줌 --읽어들인 바이트 수를 리턴해 줌
            if (len <= 0) break

            outputStream.write(buf, 0, len)
        }//while..

        //반복문이 끝났으면 복사가 완료된 것임.
        inputStream.close()
        outputStream.close()

        //AlertDialog.Builder(this).setMessage(file.absolutePath).create().show()
        return file.absolutePath
    }///////////////////////////////////////////////////////////////////////////////////

    private fun clickregister() {
        val file: MultipartBody.Part? = imgPath?.let {
            val file = File(it)
            val requestBody: RequestBody = RequestBody.create("image/*".toMediaTypeOrNull(),file)
            MultipartBody.Part.createFormData("img1", file.name, requestBody)
        }

        if (file != null) {
            RetrofitProcess(this, params = file, callback = object : RetrofitCallback {
                override fun onResponseListSuccess(response: List<Any>?) {}

                override fun onResponseSuccess(response: Any?) {
                    val code = (response as String)
                    Log.d("one file upload code", "$code") // 실패 시 5404, 성공 시 이미지 경로
                    if (code != "5404") {
                        save(code)
                    }
                }

                override fun onResponseFailure(errorMsg: String?) {
                    Log.d("one file upload fail", errorMsg!!) // 에러 메시지
                }

            }).onefileUploadRequest()
        }
    }
    private fun save(img:String){
        var title = binding.inputLayoutName.editText!!.text.toString()
        var content= binding.inputLayoutContent.editText!!.text.toString()
        val params= AddorModifyorDeleteQA("${G.user_email}", "${G.user_providerId}",
            "${G.loginType}", "", "$img", "$title", "$content")
        RetrofitProcess(this, params=params, callback = object : RetrofitCallback {
            override fun onResponseListSuccess(response: List<Any>?) {}

            override fun onResponseSuccess(response: Any?) {
                val code=(response as String) //  - 4204 서비스 회원 아님, 7400 qa 추가 성공, 7401 qa 추가 실패
                Log.d("qa add code","$code")

                when(code){
                    "4204" -> {
                        Toast.makeText(this@QAAddActivity, "관리자에게 문의하세요.", Toast.LENGTH_SHORT).show()
                        Log.d("qna오류","서비스 회원 아님")
                    }

                    "7401" -> {
                        Toast.makeText(this@QAAddActivity, "관리자에게 문의하세요.", Toast.LENGTH_SHORT).show()
                        Log.d("qna오류","추가 실패")
                    }

                    "7400" ->{
                        Toast.makeText(this@QAAddActivity, "등록되었습니다.", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }

            }

            override fun onResponseFailure(errorMsg: String?) {
                Log.d("qa add fail",errorMsg!!) // 에러 메시지
            }

        }).qaAddRequest()
    }

}
