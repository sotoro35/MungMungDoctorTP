package com.hsr2024.mungmungdoctortp.main

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.hsr2024.mungmungdoctortp.R
import com.hsr2024.mungmungdoctortp.bnv1care.CareFragment
import com.hsr2024.mungmungdoctortp.bnv2map.MapFragment
import com.hsr2024.mungmungdoctortp.bnv3community.CommunityFragment
import com.hsr2024.mungmungdoctortp.databinding.ActivityMainBinding
import com.hsr2024.mungmungdoctortp.login.ChangeProfileActivity
import com.hsr2024.mungmungdoctortp.login.DogAddActivity
import com.hsr2024.mungmungdoctortp.login.PersonRuleActivity

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
            }
           true
            }


        //drawerNavigation 설정

        binding.drawerNav.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.menu_person_rule -> startActivity(Intent(this@MainActivity,PersonRuleActivity::class.java))
                R.id.menu_change_profile -> startActivity(Intent(this@MainActivity,ChangeProfileActivity::class.java))
                R.id.menu_logout -> Toast.makeText(this@MainActivity, "로그아웃", Toast.LENGTH_SHORT).show()
                R.id.menu_user_delete -> userDelete()

            }

            binding.drawerNavLayou.closeDrawer(binding.drawerNav)
            return@setNavigationItemSelectedListener false
        }


        val drawableToggle = ActionBarDrawerToggle(this,binding.drawerNavLayou,binding.toolbar,R.string.open,R.string.close)
        drawableToggle.syncState() // 토글버튼 동기화
        drawableToggle.isDrawerIndicatorEnabled = true
        binding.drawerNavLayou.addDrawerListener(drawableToggle)

        binding.drawerNav.getHeaderView(0).findViewById<RelativeLayout>(R.id.btn_add_pet).setOnClickListener {
            startActivity(Intent(this@MainActivity,DogAddActivity::class.java))
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
