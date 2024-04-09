package com.hsr2024.mungmungdoctortp.bnv2map

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.hsr2024.mungmungdoctortp.R
import com.hsr2024.mungmungdoctortp.data.NaverSearchPlaceResponse
import com.hsr2024.mungmungdoctortp.databinding.FragmentMapBinding
import com.hsr2024.mungmungdoctortp.network.RetrofitService
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


class MapFragment:Fragment() {

    private val binding by lazy { FragmentMapBinding.inflate(layoutInflater) }
    //네이버클라우드플랫폼 - naver map api
    //클라이언트id :  glca44kwj8
    //클라이언트 secret : gwZDIk1qECJwwJN1MbIR1guPuGyYz7XRERVBW02O

    //네이버디벨로퍼  - naver 지역검색 api
    //클라이언트id : GOd0jRtXpZfnd0bO9C3k
    //클라이언트 sectret : ItBhARmmRV


    private var LOCATION_PERMISSION = 1004
    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource
    private val marker = Marker()

    // [Google Fused Location API 사용] 라이브러리이름:play-services-location 추가하기
    private var locationProviderClient: FusedLocationProviderClient? = null
    private var myLocation: Location? = null//gps가안되거나 네트워크가안될수도있으니..
    private var mapFragment: MapFragment? =null



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root

    }//oncreateView


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //위치 서비스 클라이언트 초기화
        //fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        //사용자위치 허가받았나체크
        permissionCheck()

        //retrofitNaverPlaceSearch()
    }





    private fun permissionCheck(){

        //사용자가 코어스,파인 허용안했으면 !!
        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            //허가되어있지 않으니, 다시 퍼미션 요청 다이알로그+대행사
            //Toast.makeText(requireContext(), "위치정보허용 안하셨죠?", Toast.LENGTH_SHORT).show()
            permissionResultLauncher
        }else{
            //위치정보수집에 동의.허가받았으니 사용자현위치받아오자
            requestMyLocation()
        }


    }// fetchLocation()



    //퍼미션 요청 및 결과받아오는 작업을 대신하는 대행사 등록
    val permissionResultLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){
        if (it) requestMyLocation()
        else Toast.makeText(requireContext(), "내 위치정보를 허용하지 않아 검색기능 제한됩니다.", Toast.LENGTH_SHORT).show()
    }




    private fun requestMyLocation(){

        //요청객체
        val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 3000).build()

        //실시간으로 계속 위치정보 갱신하기위해선 퍼미션체크 꼭해야됨.
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
             != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
             != PackageManager.PERMISSION_GRANTED ){
            //둘다 허용안했으면
            return
        }
        locationProviderClient?.requestLocationUpdates(request, locationCallback, Looper.getMainLooper())
    }//requestMyLocation()


    //위치정보 새로 갱신할때마다 발동하는 콜백 메소드. 추상클래스.
    private val locationCallback= object : LocationCallback(){
        override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)
            myLocation = p0.lastLocation

            //위치탐색 종료됬으니 내 위치정보 업데이트 그만...
            locationProviderClient?.removeLocationUpdates(this)//this:Location Callback

            //내위치 얻어왔으니 이제!!! 네이버지역검색(동물병원) 시작~~
            searchPlaces()
        }
    }






    private fun requestMyLocation1(){

        //구글의 퓨즈드한테 사용자현위치 받아오자
        locationProviderClient?.lastLocation?.addOnSuccessListener { location ->
            if (location != null){
                myLocation = location //퓨즈드에서 준 로케이션을 나의로케이션에넣기
                //네이버 맵 작업메소드
                searchPlaces()
            }else Toast.makeText(requireContext(), "위치정보가 null이네요", Toast.LENGTH_SHORT).show()
        }
            ?.addOnFailureListener {
                Toast.makeText(requireContext(), "위치정보 가져오기 실패:$it", Toast.LENGTH_SHORT).show()
            }
        locationSource = FusedLocationSource(requireActivity(), LOCATION_PERMISSION )

        val fragmentManager : FragmentManager = childFragmentManager
        mapFragment = fragmentManager.findFragmentById(R.id.map_fragment) as MapFragment?
            ?: MapFragment.newInstance().also {
                fragmentManager.beginTransaction().add(R.id.map_fragment, it).commit()
            }

    }  //requestMyLocation()





    private fun searchPlaces(){

        mapFragment!!.getMapAsync(object : OnMapReadyCallback {
            override fun onMapReady(map: NaverMap) {

                naverMap = map
                naverMap.maxZoom = 18.0
                naverMap.minZoom = 5.0


                //현재 내 위치를 지도의 중심위치로 설정 /내위치-위도,경도,등..
                var myLocation: Location?=null
                var latitude : Double = myLocation?.latitude ?: 37.5666
                var longitude : Double =  myLocation?.longitude ?: 126.9782
                val myPos = LatLng(latitude, longitude)

                //카메라 설정
                val cameraPosition = CameraPosition(
                    myPos,
                    16.0, //줌레벨
                    20.0, //기울임 각도
                    180.0 //베어링 각도
                )

                naverMap.cameraPosition = cameraPosition

                naverMap.addOnCameraChangeListener { reason, animated ->
                    //마커 포지션
                    marker.position = LatLng(naverMap.cameraPosition.target.latitude, naverMap.cameraPosition.target.longitude)

                    naverMap.addOnCameraIdleListener {
                        //현재 보이는 네이버맵의 정중앙 가운데로 마커
                        marker.map = naverMap
                        marker.icon = MarkerIcons.BLUE
                        marker.iconTintColor = Color.GREEN
                    }
                    naverMap.locationSource = locationSource
                    ActivityCompat.requestPermissions(requireActivity(),PERMISSION,LOCATION_PERMISSION)
                }//addOnCameraChangeListener
            }//fun onMapReady(map: NaverMap)
        })// OnMapReadyCallback



    }//naverMapAPI()

















//    private fun retrofitNaverPlaceSearch(){
//        //레트로핏
//        val builder = Retrofit.Builder()
//            .baseUrl("https://openapi.naver.com")
//            .addConverterFactory(ScalarsConverterFactory.create())
//            .addConverterFactory(GsonConverterFactory.create())
//
//        val retrofit = builder.build()
//        val retrofitService = retrofit.create(RetrofitService::class.java)
//        retrofitService.getNaverLocal("동물병원", 5).enqueue(object : Callback<NaverSearchPlaceResponse>{
//            override fun onResponse(
//                p0: Call<NaverSearchPlaceResponse>,
//                p1: Response<NaverSearchPlaceResponse>
//            ) {
//                val result = p1.body()
//                if (result != null) {
//                    Toast.makeText(requireContext(), "${result.items[0].title}", Toast.LENGTH_SHORT).show()
//                    binding.tv.setText("${result.items[1].category}")
//                }
//            }
//
//            override fun onFailure(p0: Call<NaverSearchPlaceResponse>, p1: Throwable) {
//                TODO("Not yet implemented")
//            }
//
//        })
//
//    }//retrofitHelp()



//retrofitService.getNaverLocal("동물병원", 5).enqueue(object : Callback<String>{
//            override fun onResponse(p0: Call<String>, p1: Response<String>) {
//
//                val a = p1.body()
//                AlertDialog.Builder(requireContext()).setMessage("$a").create().show()
//                binding.tv.setText("$a")
//            }
//
//            override fun onFailure(p0: Call<String>, p1: Throwable) {
//
//            }
//
//
//        })
















    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {


        when{
            requestCode != LOCATION_PERMISSION ->  return
            else -> {
                when{
                    locationSource.onRequestPermissionsResult(requestCode,permissions,grantResults) -> {

                        if (!locationSource.isActivated){
                            naverMap.locationTrackingMode = LocationTrackingMode.None
                        }else{
                            naverMap.locationTrackingMode = LocationTrackingMode.Follow
                        }


                    }//locationoSource가 리퀘스트퍼미션리절트일때..

                }//when
            }//else


        }//when


        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }//onRequestPermissionResult()






}//fragment