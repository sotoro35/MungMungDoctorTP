package com.hsr2024.mungmungdoctortp.bnv4mypage

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputLayout
import com.hsr2024.mungmungdoctortp.G
import com.hsr2024.mungmungdoctortp.R
import com.hsr2024.mungmungdoctortp.adapter.MypageDogAdapter
import com.hsr2024.mungmungdoctortp.data.Individual
import com.hsr2024.mungmungdoctortp.data.Pet
import com.hsr2024.mungmungdoctortp.data.Pet2
import com.hsr2024.mungmungdoctortp.data.PetList
import com.hsr2024.mungmungdoctortp.data.UserDelete
import com.hsr2024.mungmungdoctortp.databinding.FragmentMypageBinding
import com.hsr2024.mungmungdoctortp.login.LoginActivity
import com.hsr2024.mungmungdoctortp.main.MainActivity
import com.hsr2024.mungmungdoctortp.network.RetrofitCallback
import com.hsr2024.mungmungdoctortp.network.RetrofitProcess

class MypageFragment : Fragment() {

    private val binding by lazy { FragmentMypageBinding.inflate(layoutInflater) }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogout.paintFlags = Paint.UNDERLINE_TEXT_FLAG // 밑줄

        binding.btnLogout.setOnClickListener { clickLogout() }
        binding.userDelete.setOnClickListener { userDelete() }
        binding.changeProfile.setOnClickListener { startActivity(Intent(requireContext(),ChangeProfileActivity::class.java)) }
        binding.btnAddPet.setOnClickListener { startActivity(Intent(requireContext(),DogAddActivity::class.java)) }
        binding.personRule.setOnClickListener { startActivity(Intent(requireContext(),PersonRuleActivity::class.java)) }

        load()

        binding.mypageUserNickname.text = G.user_nickname
        if (G.user_imageUrl.isNotEmpty()) {
            Glide.with(requireContext()).load("http://43.200.163.153/img/${G.user_imageUrl.trim()}").into(binding.mypageUserImage)
        }else binding.mypageUserImage.setImageResource(R.drawable.bnv_care)



    }// onView...

    private fun load(){

        val params= Individual("${G.user_email}", "${G.user_providerId}", "${G.loginType}")
            RetrofitProcess(requireContext(),params=params, callback = object : RetrofitCallback {
                override fun onResponseListSuccess(response: List<Any>?) {}

                override fun onResponseSuccess(response: Any?) {
                    val data = (response as PetList)
                    Log.d("signup code", "$data")

                    when (data.code) {  // - 5500 펫 목록 성공, 5501 펫 목록 실패, 4204 서비스 회원 아님
                        "5501" -> Toast.makeText(requireContext(), "펫 목록 실패", Toast.LENGTH_SHORT)
                            .show()

                        "4204" -> Toast.makeText(requireContext(), "회원 정보 확인필요", Toast.LENGTH_SHORT)
                            .show()

                        "5500" -> {
                            Toast.makeText(requireContext(), "리스트 가져오기 성공", Toast.LENGTH_SHORT)
                                .show()

                            val pets:List<Pet> = data.petList
                            val mypageAdapter = MypageDogAdapter(requireContext(),pets)
                            binding.recyclerMypagePet.adapter = mypageAdapter
                            mypageAdapter.notifyDataSetChanged()

                        }
                        else -> Toast.makeText(requireContext(), "관리자문의", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onResponseFailure(errorMsg: String?) {
                    Log.d("signup fail",errorMsg!!) // 에러 메시지
                }

            }).petListRequest()



    }

    private fun clickLogout(){

        val dialog = AlertDialog.Builder(requireContext())
        dialog.setTitle("로그아웃 하시겠습니까?")
        dialog.setPositiveButton("네",object : DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                val preferences = (activity as MainActivity).getSharedPreferences("UserData",AppCompatActivity.MODE_PRIVATE)
                val editor = preferences.edit()
                editor.clear()
                editor.apply()

                startActivity(Intent(requireContext(),LoginActivity::class.java))
                (activity as MainActivity).finish()
            }

        }) // setPositive

        dialog.setNegativeButton("아니요", object : DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                dialog?.dismiss()
            }

        })// setNegative

        dialog.create().show()
    }


    lateinit var alertDialog: AlertDialog
    @SuppressLint("MissingInflatedId")
    private fun userDelete() {
        val dialogV = layoutInflater.inflate(R.layout.dialog_user_delete, null)
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(dialogV)
        alertDialog = builder.create()

        if (G.user_providerId != null || G.user_providerId != "" ){
            dialogV.findViewById<TextInputLayout>(R.id.input_password_delete1).visibility = View.VISIBLE

        }else dialogV.findViewById<TextInputLayout>(R.id.input_password_delete1).visibility = View.INVISIBLE

        dialogV.findViewById<TextView>(R.id.btn_close).setOnClickListener { alertDialog.dismiss() }
        dialogV.findViewById<TextView>(R.id.btn_user_delete).setOnClickListener {
            var password = dialogV.findViewById<TextInputLayout>(R.id.input_password_delete1).editText!!.text.toString()
            val params= UserDelete("${G.user_email}","$password","${G.user_providerId}", "${G.loginType}")
            RetrofitProcess(requireContext(),params=params, callback = object : RetrofitCallback {
            override fun onResponseListSuccess(response: List<Any>?) {}
                @SuppressLint("SuspiciousIndentation")
                override fun onResponseSuccess(response: Any?) {
                val code=(response as String)
                    when(code){
                        "1220" -> {
                            AlertDialog.Builder(requireContext()).setMessage("이용해주셔서 감사합니다").create().show()
                            startActivity(Intent(requireContext(),LoginActivity::class.java))
                            (activity as MainActivity).finish()
                        }
                        "1230" -> AlertDialog.Builder(requireContext()).setMessage("탈퇴 실패").create().show()
                        "4202" -> AlertDialog.Builder(requireContext()).setMessage("관리자에게 문의해주세요1").create().show()
                        "4203" -> AlertDialog.Builder(requireContext()).setMessage("관리자에게 문의해주세요2").create().show()
                    }
                Log.d("User withdraw code","$code") //1220 회원탈퇴 성공, 1230 회원탈퇴 실패, 4204 서비스 회원 아님, 4203 이메일 로그인 시 입력 정보 잘못되어 로그인 실패
            }

            override fun onResponseFailure(errorMsg: String?) {
                Log.d("User withdraw fail",errorMsg!!) // 에러 메시지
            }

            }).userWithDrawRequest()

            alertDialog.dismiss()

        }
        alertDialog.show()
    }

}