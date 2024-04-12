package com.hsr2024.mungmungdoctortp.bnv4mypage

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.hsr2024.mungmungdoctortp.data.Pet2
import com.hsr2024.mungmungdoctortp.databinding.FragmentMypageBinding
import com.hsr2024.mungmungdoctortp.login.LoginActivity
import com.hsr2024.mungmungdoctortp.main.MainActivity

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


        binding.mypageUserNickname.text = G.user_nickname
        if (G.user_imageUrl.isNotEmpty()) {
            binding.mypageUserImage.setImageResource(R.drawable.bnv_care)
            Glide.with(requireContext()).load("https://43.200.163.153/img/${G.user_imageUrl}").into(binding.mypageUserImage)
        }

        load()



    }// onView...

    private fun load(){

        var pets:List<Pet2> = listOf(
            Pet2("aa", "aa", "aa", "aa", "aa", "aa", "aa", "aa"),
            Pet2("bb", "bb", "bb", "bb", "bb", "bb", "bb", "bb")

            )

        val mypageAdapter = MypageDogAdapter(requireContext(),pets)
        binding.recyclerMypagePet.adapter = mypageAdapter
        mypageAdapter.notifyDataSetChanged()

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
    private fun userDelete() {
        val dialogV = layoutInflater.inflate(R.layout.dialog_user_delete, null)
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(dialogV)
        alertDialog = builder.create()

        if (G.user_providerId != null || G.user_providerId != "" ){
            dialogV.findViewById<TextInputLayout>(R.id.input_password_delete).visibility = View.INVISIBLE

        }
        dialogV.findViewById<TextView>(R.id.btn_close).setOnClickListener { alertDialog.dismiss() }
        dialogV.findViewById<TextView>(R.id.btn_user_delete).setOnClickListener {
            alertDialog.dismiss()
            //var password = dialogV.findViewById<EditText>(R.id.input_password_delete).text.toString()
            //서버에서 비교
        }
        alertDialog.show()
    }

}