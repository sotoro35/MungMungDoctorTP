package com.hsr2024.mungmungdoctortp.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.hsr2024.mungmungdoctortp.R
import com.hsr2024.mungmungdoctortp.bnv1care.CareFragment
import com.hsr2024.mungmungdoctortp.bnv2map.MapFragment
import com.hsr2024.mungmungdoctortp.bnv3community.CommunityFragment
import com.hsr2024.mungmungdoctortp.data.NaverSearchPlaceResponse
import com.hsr2024.mungmungdoctortp.databinding.ActivityMainBinding
import com.naver.maps.map.util.FusedLocationSource

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    //유리맵프레에있던거 메인으로옮김
    //네이버클라우드플랫폼 - naver map api
    //클라이언트id :  glca44kwj8
    //클라이언트 secret : gwZDIk1qECJwwJN1MbIR1guPuGyYz7XRERVBW02O

    //네이버디벨로퍼  - naver 지역검색 api
    //클라이언트id : GOd0jRtXpZfnd0bO9C3k
    //클라이언트 sectret : ItBhARmmRV

//
//    private var LOCATION_PERMISSION = 1004
//   //private var mapFragment: com.naver.maps.map.MapFragment? =null
//
//    // [Google Fused Location API 사용] 라이브러리이름:play-services-location 추가하기
//    private var locationProviderClient: FusedLocationProviderClient? = null
//    private var locationSource: FusedLocationSource? = null
//
//    //naver search API응답결과 객체 참조변수
//    var searchPlaceResponse: NaverSearchPlaceResponse? = null
//









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


//        //유리맵프레그먼트에있던거 메인으로옮김
//        locationProviderClient = LocationServices.getFusedLocationProviderClient(this)
//        locationSource = FusedLocationSource(this, LOCATION_PERMISSION )


    } // onCreate...















//유리 맵프레그먼트에있던거 메인으로옮김
//    private fun permissionCheck(){
//
//        //사용자가 코어스,파인 허용안했으면 !!
//        if (ActivityCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            //허가되어있지 않으니, 다시 퍼미션 요청 다이알로그+대행사
//            //Toast.makeText(requireContext(), "위치정보허용 안하셨죠?", Toast.LENGTH_SHORT).show()
//            permissionResultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
//        }else{
//            //위치정보수집에 동의.허가받았으니 사용자현위치받아오자
//            requestMyLocation()
//        }
//    }// fetchLocation()
//
//
//
//    //퍼미션 요청 및 결과받아오는 작업을 대신하는 대행사 등록
//    val permissionResultLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){
//        if (it) requestMyLocation()
//        else Toast.makeText(requireContext(), "내 위치정보를 허용하지 않아 검색기능 제한됩니다.", Toast.LENGTH_SHORT).show()
//    }//대행사












} //main...
