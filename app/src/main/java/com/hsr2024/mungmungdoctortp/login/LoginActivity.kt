package com.hsr2024.mungmungdoctortp.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.hsr2024.mungmungdoctortp.G
import com.hsr2024.mungmungdoctortp.R
import com.hsr2024.mungmungdoctortp.data.LoginData
import com.hsr2024.mungmungdoctortp.data.LoginResponse
import com.hsr2024.mungmungdoctortp.databinding.ActivityLoginBinding
import com.hsr2024.mungmungdoctortp.main.MainActivity
import com.hsr2024.mungmungdoctortp.network.RetrofitCallback
import com.hsr2024.mungmungdoctortp.network.RetrofitProcess
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback
import java.net.URLEncoder

class LoginActivity : AppCompatActivity() {

    var checkbox = false

    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //binding.btnTest.setOnClickListener { startActivity(Intent(this,MainActivity::class.java)) }
        binding.btnTest.visibility = View.GONE

        binding.btnLogin.setOnClickListener { login() }
        binding.btnLoginKakao.setOnClickListener { Kakao() }
        binding.btnLoginNaver.setOnClickListener { Naver() }
        binding.btnSignup.setOnClickListener { startActivity(Intent(this,SignupActivity::class.java)) }

        binding.checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (binding.checkbox.isChecked) {
                Toast.makeText(this@LoginActivity, "체크된상태", Toast.LENGTH_SHORT).show()
                checkbox = true

            } else {
                Toast.makeText(this@LoginActivity, "체크해제", Toast.LENGTH_SHORT).show()
                checkbox = false
            }

        } // checkbox

    }// onCreate

    private fun login(){
        var email = binding.loginInputEmail.editText!!.text.toString()
        var passwrod = binding.loginInputPassword.editText!!.text.toString()
        val params= LoginData(email,passwrod,"${URLEncoder.encode("", "UTF-8")}","email") // 액세스토큰의 경우 naver, kakao 로그인일 경우만 작성. 없을 경우 ""
        RetrofitProcess(this,params=params, callback = object : RetrofitCallback {
        override fun onResponseListSuccess(response: List<Any>?) {}
        override fun onResponseSuccess(response: Any?) {
            val data=(response as LoginResponse)
            when(data.code){
                "4200" -> {
                    G.apply {
                        user_email = data.email ?: ""
                        user_providerId = data.provider_id ?: ""
                        user_nickname = data.nickname ?: ""
                        user_imageUrl = data.userImgUrl ?: ""
                        pet_id = data.pet_id ?: ""
                        pet_name = data.pet_name ?: ""
                        pet_imageUrl = data.petImgUrl ?: ""
                        pet_birthDate = data.pet_birth_date ?: ""
                        pet_gender = data.pet_gender ?: ""
                        pet_neutering = data.pet_neutered ?: ""
                        loginType = "email"
                    } // G..

                    if(checkbox) saveSharedPreferences()
                    //Toast.makeText(this@LoginActivity, "${data.nickname}:${G.user_nickname}", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@LoginActivity,MainActivity::class.java))
                    finish()
                }
                "4203" -> AlertDialog.Builder(this@LoginActivity).setMessage("비밀번호가 틀립니다").create().show()
                "4204" -> AlertDialog.Builder(this@LoginActivity).setMessage("미가입 회원입니다").create().show()

            }
            Log.d("login data","$data") // LoginResponse 데이터 출력(email, provider_id, nickname, userImgUrl, pet_id, pet_name, petImgUrl, pet_birth_date, pet_gender, pet_neutered, code)
        }                               // code 값 4200 로그인 성공, 4203 비밀번호 틀림, 4204 없는 회원 3200 간편 로그인 사용자 조회 완료, 3204 간편 로그인 사용자 조회 불가

        override fun onResponseFailure(errorMsg: String?) {
            Log.d("login fail",errorMsg!!) // 에러 메시지
        }
      }).loginRequest()
    } // login

    private fun Kakao(){
        // 카카오톡으로 로그인 안될시 카카오 계정으로 로그인
        val callback:(OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.d("kakao", "카카오계정으로 로그인 실패", error)
            } else if (token != null) {
                Log.d("kakao", "카카오계정으로 로그인 성공: ${token.accessToken}")
                kakaoLogin("${token.accessToken}")
            }
        }

        // 카카오톡이 설치되어 있으면 카카오톡 or 카카오 계정 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)){
            UserApiClient.instance.loginWithKakaoTalk(this){ token, error ->
                if (error != null) {
                    Log.d("kakao", "카카오톡 로그인 실패", error)

                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled){
                        return@loginWithKakaoTalk
                    }
                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                } else if (token != null) {
                    Log.d("kakao", "카카오톡 로그인 성공: ${token.accessToken}")
                    kakaoLogin("${token.accessToken}")
                }
            }//loginWithKakaoTalk
        }else{
            UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
        }
    } // kakao

    private fun kakaoLogin(token:String){
        val params= LoginData("","","${URLEncoder.encode("${token}", "UTF-8")}","kakao") // 액세스토큰의 경우 naver, kakao 로그인일 경우만 작성. 없을 경우 ""
        RetrofitProcess(this,params=params, callback = object : RetrofitCallback {
            override fun onResponseListSuccess(response: List<Any>?) {}

            override fun onResponseSuccess(response: Any?) {
                val data=(response as LoginResponse)
                // code 값 4200 로그인 성공, 4203 로그인 실패 3200 간편 로그인 사용자 조회 완료, 3204 간편 로그인 사용자 조회 불가
                // LoginResponse 데이터 출력(email, provider_id, nickname, userImgUrl, pet_id, pet_name, petImgUrl, pet_birth_date, pet_gender, pet_neutered, code)
                when(data.code) {

                    "3204" -> AlertDialog.Builder(this@LoginActivity).setMessage("로그인 실패").create()
                        .show()

                    "4200" -> {
                        if (data.nickname == null || data.nickname == "") {
                            G.user_providerId = data.provider_id
                            G.loginType = "kakao"
                            startActivity(Intent(this@LoginActivity, EasyloginActivity::class.java))
                        } else {
                            G.apply {
                                user_providerId = data.provider_id ?: ""
                                user_email = data.email ?: ""
                                user_nickname = data.nickname ?: ""
                                user_imageUrl = data.userImgUrl ?: ""
                                pet_id = data.pet_id ?: ""
                                pet_name = data.pet_name ?: ""
                                pet_imageUrl = data.petImgUrl ?: ""
                                pet_birthDate = data.pet_birth_date ?: ""
                                pet_gender = data.pet_gender ?: ""
                                pet_neutering = data.pet_neutered ?: ""
                                loginType = "kakao"
                            }//G.apply

                            startActivity(Intent(this@LoginActivity,MainActivity::class.java))
                            finish()
                        } //else
                    }// 4200
                }

                Log.d("login data","$data")
            } //onResponseSuccess

            override fun onResponseFailure(errorMsg: String?) {
                Log.d("login fail",errorMsg!!) // 에러 메시지
            }

        }).loginRequest()
    }

    private fun Naver(){
        //네아로SDK 초기화
        NaverIdLoginSDK.initialize(this,"FbQRlqwZC2ty8cFgIdC0","pDse1077V4","멍멍닥터")

        NaverIdLoginSDK.authenticate(this,object : OAuthLoginCallback{
            override fun onError(errorCode: Int, message: String) {
                Log.d("naver","로그인에러 : $message")
            }

            override fun onFailure(httpStatus: Int, message: String) {
                Log.d("naver","로그인실패 : $message")
            }
            override fun onSuccess() {
                //로그인에 성공하면 REST API로 요청할 수 있는 토큰(token)을 발급받음.
                Log.d("naver","로그인성공")
                val token:String? = NaverIdLoginSDK.getAccessToken()
                if (token != null) naverLogin(token)
            }

        })
    } //naver

    private fun naverLogin(token:String){
        val params= LoginData("","","${URLEncoder.encode("${token}", "UTF-8")}","naver") // 액세스토큰의 경우 naver, kakao 로그인일 경우만 작성. 없을 경우 ""
        RetrofitProcess(this,params=params, callback = object : RetrofitCallback {
            override fun onResponseListSuccess(response: List<Any>?) {}

            override fun onResponseSuccess(response: Any?) {
                val data=(response as LoginResponse)
                // code 값 4200 로그인 성공, 4203 로그인 실패 3200 간편 로그인 사용자 조회 완료, 3204 간편 로그인 사용자 조회 불가
                // LoginResponse 데이터 출력(email, provider_id, nickname, userImgUrl, pet_id, pet_name, petImgUrl, pet_birth_date, pet_gender, pet_neutered, code)
                when(data.code) {

                    "3204" -> AlertDialog.Builder(this@LoginActivity).setMessage("로그인 실패").create()
                        .show()

                    "4200" -> {
                        if (data.nickname == null || data.nickname == "") {
                            G.user_providerId = data.provider_id
                            G.loginType = "naver"
                            startActivity(Intent(this@LoginActivity, EasyloginActivity::class.java))
                        } else {
                            G.apply {
                                user_email = data.email ?: ""
                                user_providerId = data.provider_id ?: ""
                                user_nickname = data.nickname ?: ""
                                user_imageUrl = data.userImgUrl ?: ""
                                pet_id = data.pet_id ?: ""
                                pet_name = data.pet_name ?: ""
                                pet_imageUrl = data.petImgUrl ?: ""
                                pet_birthDate = data.pet_birth_date ?: ""
                                pet_gender = data.pet_gender ?: ""
                                pet_neutering = data.pet_neutered ?: ""
                                loginType = "naver"
                            }//G.apply

                            startActivity(Intent(this@LoginActivity,MainActivity::class.java))
                            finish()
                        } //else
                    }// 4200
                }

                Log.d("login data","$data")
            } //onResponseSuccess

            override fun onResponseFailure(errorMsg: String?) {
                Log.d("login fail",errorMsg!!) // 에러 메시지
            }

        }).loginRequest()


    }

    //자동로그인 체크시 SharedPreference 저장
    private fun saveSharedPreferences(){
        val preferences = getSharedPreferences("UserData", MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("user_email",G.user_email)
        editor.putString("user_nickname",G.user_nickname)
        editor.putString("user_imageUrl",G.user_imageUrl)
        editor.putString("pet_id",G.pet_id)
        editor.putString("pet_name",G.pet_name)
        editor.putString("pet_imageUrl",G.pet_imageUrl)
        editor.putString("pet_birthDate",G.pet_birthDate)
        editor.putString("pet_gender",G.pet_gender)
        editor.putString("pet_neutering",G.pet_neutering)
        editor.putString("pet_breed",G.pet_breed)
        editor.putString("loginType",G.loginType)
        editor.apply()
    }

    private fun loginKakao(){

    }

}//main
