package com.hsr2024.mungmungdoctortp.network

import android.app.AlertDialog
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.loader.content.CursorLoader
import com.hsr2024.mungmungdoctortp.data.LoginData
import com.hsr2024.mungmungdoctortp.data.LoginResponse
import com.hsr2024.mungmungdoctortp.data.SignUpData
import com.hsr2024.mungmungdoctortp.data.UserDelete
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import android.content.ContextWrapper
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.hsr2024.mungmungdoctortp.data.AddDog
import com.hsr2024.mungmungdoctortp.data.UserChange
import com.hsr2024.mungmungdoctortp.main.MainActivity
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create
import retrofit2.http.Multipart
import java.net.URI

class RetrofitProcess(
    val context: Context,
    val serviceUrl: String = "https://43.200.163.153",
    val params: Any,
    private val callback: RetrofitCallback? = null
)
{
    private fun setRetrofitService() : RetrofitService {    // retrofit 서비스 객체  생성
        val retrofit= RetrofitHelper.getunsafeRetrofitInstance(serviceUrl)
        val retrofitService=retrofit.create(RetrofitService::class.java)
        return retrofitService
    }

    // access toeken의 경우 URL Encode 필수
    // "${URLEncoder.encode(access_token, "UTF-8")}"
    fun loginRequest() {
        val retrofitService = setRetrofitService()
        // 파라미터를 Json 형태로 변환
        val loginData=(params as LoginData)
        val call = retrofitService.login(loginData)
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(p0: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val s= response.body()
                    s ?: return
                    callback?.onResponseSuccess(s)
                }
            }

            override fun onFailure(p0: Call<LoginResponse>, t: Throwable) {
                callback?.onResponseFailure(t.message)
            }

        })
    }
//    loginRequest 사용법
//    val params= LoginData("이메일","패스워드","${URLEncoder.encode(액세스토큰, "UTF-8")}","naver") // 액세스토큰은 naver, kakao일 경우 작성, email의 경우 액세스토큰 빈 값
//    RetrofitProcess(this,params=params, callback = object : RetrofitCallback {
//    override fun onResponseListSuccess(response: List<Any>?) {}
//
//    override fun onResponseSuccess(response: Any?) {
//        val data=(response as LoginResponse)
//        Log.d("login data","$data") // LoginResponse 데이터 출력(email, provider_id, nickname, userImgUrl, pet_id, pet_name, petImgUrl, pet_birth_date, pet_gender, pet_neutered, code)
//    }                               // code 값 4200 로그인 성공, 4203 로그인 실패 3200 간편 로그인 사용자 조회 완료, 3204 간편 로그인 사용자 조회 불가
//
//    override fun onResponseFailure(errorMsg: String?) {
//        Log.d("login fail",errorMsg!!) // 에러 메시지
//    }
//
//  }).loginRequest()

    //회원가입
    fun signupRequest() {

        val retrofitService = setRetrofitService()
        // 파라미터를 Json 형태로 변환
        val signUpData=(params as SignUpData)
        val call = retrofitService.singUp(signUpData)
        call.enqueue(object : Callback<String> {
            override fun onResponse(p0: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    val s= response.body()
                    s ?: return
                    callback?.onResponseSuccess(s)
                }
            }

            override fun onFailure(p0: Call<String>, t: Throwable) {
                callback?.onResponseFailure(t.message)
            }

        })
    }

// signupRequest 사용법
//val params= SignUpData("이메일정보","패스워드 정보","닉네임")
//RetrofitProcess(this,params=params, callback = object : RetrofitCallback {
//    override fun onResponseListSuccess(response: List<Any>?) {}
//
//    override fun onResponseSuccess(response: Any?) {
//        val code=(response as String)
//        Log.d("signup code","$code") //1200 회원추가 성공, 1201 회원 추가 실패
//    }
//
//    override fun onResponseFailure(errorMsg: String?) {
//        Log.d("signup fail",errorMsg!!) // 에러 메시지
//    }
//
//}).signupRequest()

    //중복 체크
    fun dupliCheckRequest() {
        val retrofitService = setRetrofitService()
        // 파라미터를 Json 형태로 변환
        val nickname=(params as String)
        val call = retrofitService.dupliCheck(nickname)
        call.enqueue(object : Callback<String>{
            override fun onResponse(p0: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    val s= response.body()
                    s ?: return
                    callback?.onResponseSuccess(s)
                }
            }

            override fun onFailure(p0: Call<String>, t: Throwable) {
                callback?.onResponseFailure(t.message)
            }

        })
    }
// dupliCheckRequest 사용법
//RetrofitProcess(this,params="중복체크할닉네임", callback = object : RetrofitCallback {
//    override fun onResponseListSuccess(response: List<Any>?) {}
//
//    override fun onResponseSuccess(response: Any?) {
//        val code=(response as String)
//        Log.d("nickname code","$code") // 4320 닉네임 중복, 4300 중복x
//    }
//
//    override fun onResponseFailure(errorMsg: String?) {
//        Log.d("nickname fail",errorMsg!!) // 에러 메시지
//    }
//
//}).dupliCheckRequest()

    fun userWithDrawRequest(){
        val retrofitService = setRetrofitService()
        // 파라미터를 Json 형태로 변환
        val userDelete=(params as UserDelete)
        val call = retrofitService.withdraw(userDelete)
        call.enqueue(object : Callback<String> {
            override fun onResponse(p0: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    val s= response.body()
                    s ?: return
                    callback?.onResponseSuccess(s)
                }
            }
            override fun onFailure(p0: Call<String>, t: Throwable) {
                callback?.onResponseFailure(t.message)
            }

        })
    }
// userWithDrawRequest 사용법
//val params= UserDelete("이메일정보","패스워드 정보","provider_id", "로그인 타입")
//RetrofitProcess(this,params=params, callback = object : RetrofitCallback {
//    override fun onResponseListSuccess(response: List<Any>?) {}
//
//    override fun onResponseSuccess(response: Any?) {
//        val code=(response as String)
//        Log.d("signup code","$code") //1220 회원탈퇴 성공, 1230 회원탈퇴 실패, 4204 서비스 회원 아님, 4203 이메일 로그인 시 입력 정보 잘못되어 로그인 실패
//    }
//
//    override fun onResponseFailure(errorMsg: String?) {
//        Log.d("signup fail",errorMsg!!) // 에러 메시지
//    }
//
//}).userWithDrawRequest()

    fun onefileUploadRequest(){
        val uri=(params as Uri)
        val imgPath=onegetRealPathfromUri(uri)
        val file=File(imgPath)
        val requestBody:RequestBody=RequestBody.create(MediaType.parse("image/*"),file) //일종의 진공팩
        val retrofitService = setRetrofitService()

        val part=MultipartBody.Part.createFormData("img1",file.name,requestBody)
        val call = retrofitService.onefileuploadImage(part)
        call.enqueue(object : Callback<String> {
            override fun onResponse(p0: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    val s= response.body()
                    s ?: return
                    callback?.onResponseSuccess(s)
                }
            }
            override fun onFailure(p0: Call<String>, t: Throwable) {
                callback?.onResponseFailure(t.message)
            }

        })
    }
// onefileUploadRequest 사용법
//    // val intent = if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU) Intent(MediaStore.ACTION_PICK_IMAGES) else Intent(Intent.ACTION_OPEN_DOCUMENT).setType("image/^*")
//    // resultLauncher.launch(intent)
//    // }
//    // private val resultLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ imgUri ->
//    // val params= Uri("위의 imgUri 정보")
//    RetrofitProcess(this,params=params, callback = object : RetrofitCallback {
//    override fun onResponseListSuccess(response: List<Any>?) {}
//
//    override fun onResponseSuccess(response: Any?) {
//        val code=(response as String)
//        Log.d("signup code","$code") // 실패 시 5404, 성공 시 이미지 경로
//    }
//
//    override fun onResponseFailure(errorMsg: String?) {
//        Log.d("signup fail",errorMsg!!) // 에러 메시지
//    }
//
//}).onefileUploadRequest()

    fun userModifyRequest(){
        val retrofitService = setRetrofitService()
        // 파라미터를 Json 형태로 변환
        val userChange=(params as UserChange)
        val call = retrofitService.userModify(userChange)
        call.enqueue(object : Callback<String> {
            override fun onResponse(p0: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    val s= response.body()
                    s ?: return
                    callback?.onResponseSuccess(s)
                }
            }
            override fun onFailure(p0: Call<String>, t: Throwable) {
                callback?.onResponseFailure(t.message)
            }

        })
    }
// userModifyRequest 사용법
//val params= UserChange("이메일정보", "패스워드", "provider_id", "userModifyRequest으로 나온 url값", "로그인 타입")
//RetrofitProcess(this,params=params, callback = object : RetrofitCallback {
//    override fun onResponseListSuccess(response: List<Any>?) {}
//
//    override fun onResponseSuccess(response: Any?) {
//        val code=(response as String)
//        Log.d("signup code","$code") // 1220 회원 정보 수정 성공, 1230 회원 정보 수정 실패, 4204 서비스 회원 아님
//    }
//
//    override fun onResponseFailure(errorMsg: String?) {
//        Log.d("signup fail",errorMsg!!) // 에러 메시지
//    }
//
//}).userModifyRequest()

    fun petAddRequest(){
        val retrofitService = setRetrofitService()
        // 파라미터를 Json 형태로 변환
        val addDog=(params as AddDog)
        val call = retrofitService.addDog(addDog)
        call.enqueue(object : Callback<String> {
            override fun onResponse(p0: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    val s= response.body()
                    s ?: return
                    callback?.onResponseSuccess(s)
                }
            }
            override fun onFailure(p0: Call<String>, t: Throwable) {
                callback?.onResponseFailure(t.message)
            }

        })
    }

// petAddRequest 사용법
//val params= AddDog("이메일정보", "provider_id", "펫 이름", "펫 프로필", "펫 생년월일", "펫 성별", "펫 중성화 여부", "펫 견종", "로그인 타입")
//RetrofitProcess(this,params=params, callback = object : RetrofitCallback {
//    override fun onResponseListSuccess(response: List<Any>?) {}
//
//    override fun onResponseSuccess(response: Any?) {
//        val code=(response as String)
//        Log.d("signup code","$code") //  - 5200 펫 추가 성공, 5201 펫 추가 실패, 4204 서비스 회원 아님
//    }
//
//    override fun onResponseFailure(errorMsg: String?) {
//        Log.d("signup fail",errorMsg!!) // 에러 메시지
//    }
//
//}).petAddRequest()

    private fun onegetRealPathfromUri(uri:Uri) : String? {
        //android 10 버전 부터는 Uri를 통해 파일의 실제 경로를 얻을 수 있는 방법이 없어졌음
        //그래서 uri에 해당하는 파일을 복사하여 임시로 파일을 만들고 그 파일의 경로를 이용하여 서버에 전송

        //Uri[미디어저장소의 DB 주소]로부터 파일의 이름을 얻어오기 - DB SELECT 쿼리 작업을 해주는 기능을 가진 객체를 이용
        // CursorLoader : Content, uri객체, 컬럼 위치, 조건(where), 조건에 해당되는 값, 오름 or 내림차순
        val cursorLoader=CursorLoader(context, uri, null, null, null, null)
        //비동기로 로딩해라
        val cursor:Cursor?=cursorLoader.loadInBackground()
        val fileName:String?=cursor?.run {
            moveToFirst()
            // 컬럼 위치 array MediaStore.Images.Media 중 이미지 이름을 담고 있는 멤버변수 DISPLAY_NAME
            getString(getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))
        }
        //복사본이 저장될 파일의 경로와 파일명.확장자
        //externalCacheDir : /stroage/emulated/0/Android/data/패키지이름/cache/
        //fileName : 1000000076.jpg
        val file=File((context as MainActivity).externalCacheDir,fileName)
        //AlertDialog.Builder(this).setMessage(file.absolutePath).create().show()

        //이제 파일 복사 작업 수행(로드된 파일 경로(uri)에서 파일을 가져와(inputStream) 파일을 실제 경로에 저장(outputStream)
        // contentResolver 컨텐츠 주소 해독
        val inputStream:InputStream=(context as MainActivity).contentResolver.openInputStream(uri) ?: return null
        val outputStream:OutputStream=FileOutputStream(file)

        while(true){
            val buf:ByteArray= ByteArray(1024) // 빈 바이트 배열 [1KB]
            val len:Int=inputStream.read(buf) //스트림을 통해 읽어들인 바이트들을 buf 배열에 넣기 -- 읽어들인 데이터의 크기를 리턴
            if(len <= 0) break //읽어들인 데이터 크기가 0일 경우 break
            outputStream.write(buf, 0, len) //buf 배열에 있는 바이트들을 file경로로 던지기. 첫시작(0) ~ 1023번, 다음 첫시작(1024) ~ 1027번 ..
        } // 반복문이 끝났으면 복사가 완료
        inputStream.close()
        outputStream.close()
        AlertDialog.Builder(context).setMessage(file.absolutePath).create().show()
        return file.absolutePath
    }


} // Class
