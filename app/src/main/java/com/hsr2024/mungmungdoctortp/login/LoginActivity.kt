package com.hsr2024.mungmungdoctortp.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import java.net.URLEncoder

class LoginActivity : AppCompatActivity() {

    var checkbox = false

    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnTest.setOnClickListener { startActivity(Intent(this,MainActivity::class.java)) }

        binding.btnLogin.setOnClickListener { login() }
        binding.btnLoginKakao.setOnClickListener { loginKakao() }
        binding.btnLoginNaver.setOnClickListener { loginNaver() }
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
                        user_email = data.email
                        user_nickname = data.nickname
                        user_imageUrl = data.userImgUrl
                        pet_id = data.pet_id
                        pet_name = data.pet_name
                        pet_imageUrl = data.petImgUrl
                        pet_birthDate = data.pet_birth_date
                        pet_gender = data.pet_gender
                        pet_neutering = data.pet_neutered
                        loginType = "email"
                    } // G..

                    if(checkbox) saveSharedPreferences()

                    Toast.makeText(this@LoginActivity, "${data.nickname}:${G.user_nickname}", Toast.LENGTH_SHORT).show()
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

    private fun loginKakao(){
        val params= LoginData("","","${URLEncoder.encode("", "UTF-8")}","kakao") // 액세스토큰의 경우 naver, kakao 로그인일 경우만 작성. 없을 경우 ""
        RetrofitProcess(this,params=params, callback = object : RetrofitCallback {
            override fun onResponseListSuccess(response: List<Any>?) {}

            override fun onResponseSuccess(response: Any?) {
                val data=(response as LoginResponse)
                when(data.code){

                    "3204" -> AlertDialog.Builder(this@LoginActivity).setMessage("로그인 실패").create().show()
                }
                //G.apply {
                //                        user_email = data.email
                //user_providerId = data.provider_id
                //                        user_nickname = data.nickname
                //                        user_imageUrl = data.userImgUrl
                //                        pet_id = data.pet_id
                //                        pet_name = data.pet_name
                //                        pet_imageUrl = data.petImgUrl
                //                        pet_birthDate = data.pet_birth_date
                //                        pet_gender = data.pet_gender
                //                        pet_neutering = data.pet_neutered
                //                    }
                Log.d("login data","$data") // LoginResponse 데이터 출력(email, provider_id, nickname, userImgUrl, pet_id, pet_name, petImgUrl, pet_birth_date, pet_gender, pet_neutered, code)
            }                               // code 값 4200 로그인 성공, 4203 로그인 실패 3200 간편 로그인 사용자 조회 완료, 3204 간편 로그인 사용자 조회 불가

            override fun onResponseFailure(errorMsg: String?) {
                Log.d("login fail",errorMsg!!) // 에러 메시지
            }

        }).loginRequest()

    } // kakao

    private fun loginNaver(){

        val params= LoginData("","","${URLEncoder.encode("", "UTF-8")}","naver") // 액세스토큰의 경우 naver, kakao 로그인일 경우만 작성. 없을 경우 ""
        RetrofitProcess(this,params=params, callback = object : RetrofitCallback {
            override fun onResponseListSuccess(response: List<Any>?) {}

            override fun onResponseSuccess(response: Any?) {
                val data=(response as LoginResponse)

                when(data.code){

                    "3204" -> AlertDialog.Builder(this@LoginActivity).setMessage("로그인 실패").create().show()
                }
                Log.d("login data","$data") // LoginResponse 데이터 출력(email, provider_id, nickname, userImgUrl, pet_id, pet_name, petImgUrl, pet_birth_date, pet_gender, pet_neutered, code)
            }                               // code 값 4200 로그인 성공, 4203 로그인 실패 3200 간편 로그인 사용자 조회 완료, 3204 간편 로그인 사용자 조회 불가

            override fun onResponseFailure(errorMsg: String?) {
                Log.d("login fail",errorMsg!!) // 에러 메시지
            }

        }).loginRequest()
    } //naver

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
        editor.apply()
    }

}//main
