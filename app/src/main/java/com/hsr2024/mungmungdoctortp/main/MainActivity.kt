package com.hsr2024.mungmungdoctortp.main
import android.content.Intent
import android.os.Bundle
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.hsr2024.mungmungdoctortp.R
import com.hsr2024.mungmungdoctortp.bnv1care.CareFragment
import com.hsr2024.mungmungdoctortp.bnv2map.MapFragment
import com.hsr2024.mungmungdoctortp.bnv3community.CommunityFragment
import com.hsr2024.mungmungdoctortp.databinding.ActivityMainBinding
import com.hsr2024.mungmungdoctortp.bnv4mypage.ChangeProfileActivity
import com.hsr2024.mungmungdoctortp.bnv4mypage.DogAddActivity
import com.hsr2024.mungmungdoctortp.bnv4mypage.MypageFragment
import com.hsr2024.mungmungdoctortp.bnv4mypage.PersonRuleActivity

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.bnvView.itemIconTintList = null // 아이콘 색 넣으려고 설정..


        // [바텀네비 별로 프래그먼트 보이도록 설정]
        supportFragmentManager.beginTransaction().add((R.id.container_fragment),CareFragment()).commit()

        binding.bnvView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_care -> supportFragmentManager.beginTransaction().replace((R.id.container_fragment),CareFragment()).commit()
                R.id.menu_map -> supportFragmentManager.beginTransaction().replace((R.id.container_fragment),MapFragment()).commit()
                R.id.menu_community -> supportFragmentManager.beginTransaction().replace((R.id.container_fragment),CommunityFragment()).commit()
                R.id.menu_mypage -> supportFragmentManager.beginTransaction().replace((R.id.container_fragment),MypageFragment()).commit()
            }
           true
            }


    } // onCreate...

    lateinit var alertDialog: AlertDialog
    private fun userDelete() {
        val dialogV = layoutInflater.inflate(R.layout.dialog_user_delete, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(dialogV)
        alertDialog = builder.create()

        dialogV.findViewById<TextView>(R.id.btn_close).setOnClickListener { alertDialog.dismiss() }
        dialogV.findViewById<TextView>(R.id.btn_user_delete).setOnClickListener {
            alertDialog.dismiss()
            //var password = dialogV.findViewById<EditText>(R.id.input_password_delete).text.toString()


            //서버에서 비교
        }

        alertDialog.show()

        }

} //main...
