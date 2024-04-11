package com.hsr2024.mungmungdoctortp.network

import android.content.Context
import android.util.Log
import com.hsr2024.mungmungdoctortp.data.LoginData
import com.hsr2024.mungmungdoctortp.data.LoginResponse
import com.hsr2024.mungmungdoctortp.data.SignUpData
import com.hsr2024.mungmungdoctortp.data.UserDelete
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URLEncoder

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

} // Class
