package com.hsr2024.mungmungdoctortp.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.hsr2024.mungmungdoctortp.G
import com.hsr2024.mungmungdoctortp.R
import com.hsr2024.mungmungdoctortp.data.EasySignUpData
import com.hsr2024.mungmungdoctortp.databinding.ActivityEasyloginBinding
import com.hsr2024.mungmungdoctortp.main.MainActivity
import com.hsr2024.mungmungdoctortp.network.RetrofitCallback
import com.hsr2024.mungmungdoctortp.network.RetrofitProcess

class EasyloginActivity : AppCompatActivity() {

    private val binding by lazy { ActivityEasyloginBinding.inflate(layoutInflater) }

    var nickname = ""
    var checkNickname = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.btnCheckNickname.setOnClickListener { clickNickName() }
        binding.btnSignupSave.setOnClickListener { signup() }

        binding.inputNickname.editText?.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (checkNickname && nickname != s.toString()) checkNickname = false
                // 중복확인 누르고 닉네임 변경하고 중복확인을 무조건 다시 누르고 회원가입 버튼이 눌리게끔

            }
            override fun afterTextChanged(s: Editable?) {
            }

        })
    }

    private fun clickNickName(){
        nickname= binding.inputNickname.editText!!.text.toString()
        RetrofitProcess(this,params="$nickname", callback = object : RetrofitCallback {
            override fun onResponseListSuccess(response: List<Any>?) {}

            override fun onResponseSuccess(response: Any?) {
                val code=(response as String)
                Log.d("nickname DupliCheck code","$code") // 4320 닉네임 중복, 4300 중복x
                when(code){
                    "4320" -> {
                        Toast.makeText(this@EasyloginActivity, "이미 사용중입니다", Toast.LENGTH_SHORT).show()
                        checkNickname = false
                    }
                    "4300" -> {
                        Toast.makeText(this@EasyloginActivity, "사용가능합니다", Toast.LENGTH_SHORT).show()
                        checkNickname = true
                    }
                }
            }

            override fun onResponseFailure(errorMsg: String?) {
                Log.d("nickname DupliCheck fail",errorMsg!!) // 에러 메시지
            }

        }).dupliCheckRequest()

    }

    private fun signup(){
        nickname= binding.inputNickname.editText!!.text.toString()

       if (saveCheck(nickname)){
            val params= EasySignUpData("${G.user_providerId}", "$nickname", "${G.loginType}")
            RetrofitProcess(this, params=params, callback = object : RetrofitCallback {
                override fun onResponseListSuccess(response: List<Any>?) {}

                override fun onResponseSuccess(response: Any?) {
                    val code=(response as String) //  -  - 1200 회원 추가 성공, 1201 회원 추가 실패, 4330 닉네임 또는 이메일 중복
                    Log.d("qa add code","$code")

                    when(code){
                        "1201" -> Toast.makeText(this@EasyloginActivity, "관리자에게 문의하세요", Toast.LENGTH_SHORT).show()
                        "4330" -> Toast.makeText(this@EasyloginActivity, "관리자에게 문의하세요", Toast.LENGTH_SHORT).show()
                        "1200" -> {
                            Toast.makeText(this@EasyloginActivity, "회원가입이 완료되었습니다", Toast.LENGTH_SHORT).show()
                            G.user_nickname = nickname
                            startActivity(Intent(this@EasyloginActivity,MainActivity::class.java))
                            finish()
                        }
                    }
                }

                override fun onResponseFailure(errorMsg: String?) {
                    Log.d("qa add fail",errorMsg!!) // 에러 메시지
                }
            }).easySignUpRequest()
        }
    }

    private fun saveCheck(nickname:String) : Boolean {

        var boolean = false

        when{
            checkNickname != true -> AlertDialog.Builder(this).setMessage("중복확인을 해주세요").create().show()

            nickname.length < 2 -> {
                AlertDialog.Builder(this).setMessage("닉네임이 너무 짧습니다").create().show()
                boolean = false
            }

            nickname.contains(" ") -> {
                AlertDialog.Builder(this).setMessage("띄어쓰기는 사용할 수 없습니다").create().show()
                boolean = false
            }


            else -> boolean = true
        } // when...

        return boolean
    }
}