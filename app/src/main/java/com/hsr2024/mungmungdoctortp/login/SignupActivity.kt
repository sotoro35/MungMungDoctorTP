package com.hsr2024.mungmungdoctortp.login

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
import androidx.core.widget.addTextChangedListener
import com.hsr2024.mungmungdoctortp.R
import com.hsr2024.mungmungdoctortp.data.SignUpData
import com.hsr2024.mungmungdoctortp.databinding.ActivitySignupBinding
import com.hsr2024.mungmungdoctortp.network.RetrofitCallback
import com.hsr2024.mungmungdoctortp.network.RetrofitProcess

class SignupActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySignupBinding.inflate(layoutInflater) }

    var nickname = ""
    var checkNickname = false // 닉네임 변경시 무조건 중복체크 하게끔

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        binding.toolbar.setOnClickListener { finish() }
        binding.btnCheckNickname.setOnClickListener { checkNickname() }
        binding.btnSignupSave.setOnClickListener { signup() }

        binding.inputNickname.editText?.addTextChangedListener(object:TextWatcher{
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

    private fun checkNickname(){

        nickname = binding.inputNickname.editText!!.text.toString()


        RetrofitProcess(this,params=nickname, callback = object : RetrofitCallback {
            override fun onResponseListSuccess(response: List<Any>?) {}

            override fun onResponseSuccess(response: Any?) {
                val code=(response as String)

                when(code){
                    "4300" -> {
                        AlertDialog.Builder(this@SignupActivity).setMessage("사용가능합니다").create().show()
                        checkNickname = true
                    }
                    "4320" -> {
                        AlertDialog.Builder(this@SignupActivity).setMessage("이미 사용중입니다").create().show()
                        checkNickname = false
                    }

                }
                Log.d("nickname code","$code") // 4320 닉네임 중복, 4300 중복x  //
            }

            override fun onResponseFailure(errorMsg: String?) {
                Log.d("nickname fail",errorMsg!!) // 에러 메시지
            }

        }).dupliCheckRequest()

    }


    private fun signup(){
        var email = binding.inputEmail.editText!!.text.toString()
        nickname = binding.inputNickname.editText!!.text.toString()
        var password = binding.inputPassword.editText!!.text.toString()
        var passwordConfirm = binding.inputPasswordCon.editText!!.text.toString()

        if (saveCheck(nickname,email,password,passwordConfirm)){

            val params= SignUpData(email,password,nickname)
            RetrofitProcess(this,params=params, callback = object : RetrofitCallback {
                override fun onResponseListSuccess(response: List<Any>?) {}

                override fun onResponseSuccess(response: Any?) {
                    val code=(response as String)

                    when(code){
                        "4330" -> Toast.makeText(this@SignupActivity, "중복된 사용자가 있습니다", Toast.LENGTH_SHORT).show()
                        "1201" -> Toast.makeText(this@SignupActivity, "관리자에게 문의하세요", Toast.LENGTH_SHORT).show()
                        "1200" -> {
                            Toast.makeText(this@SignupActivity, "회원가입이 완료되었습니다", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }

                    Log.d("signup code","$code") //1200 회원추가 성공, 1201 회원 추가 실패 // 4330 중복일경우

                }

                override fun onResponseFailure(errorMsg: String?) {
                    Log.d("signup fail",errorMsg!!) // 에러 메시지
                }

            }).signupRequest()

        }

    }

    private fun saveCheck(nickname:String,email: String,password:String,passwordConfirm:String) : Boolean {

        var boolean = false

        when{

            checkNickname != true -> AlertDialog.Builder(this).setMessage("중복확인을 해주세요").create().show()

            !email.contains("@") -> {
                AlertDialog.Builder(this).setMessage("@넣어 입력해주세요").create().show()
                boolean = false
            }

            password != passwordConfirm -> {
                AlertDialog.Builder(this).setMessage("패스워드가 다릅니다.다시 확인해주세요").create().show()
                boolean = false
            }

            nickname.length < 2 -> {
                AlertDialog.Builder(this).setMessage("닉네임이 너무 짧습니다").create().show()
                boolean = false
            }

            password.length < 4 -> {
                AlertDialog.Builder(this).setMessage("비밀번호가 너무 짧습니다").create().show()
                boolean = false
            }

            password.contains(" ") || nickname.contains(" ") || email.contains(" ") -> {
                AlertDialog.Builder(this).setMessage("띄어쓰기는 사용할 수 없습니다").create().show()
                boolean = false
            }

            !nickname.isNotEmpty() && !email.isNotEmpty() && !password.isNotEmpty() && !passwordConfirm.isNotEmpty() -> {
                AlertDialog.Builder(this).setMessage("모두 입력해주세요").create().show()
                boolean = false
            }

            else -> boolean = true
        } // when...

        return boolean
    }


}

