package com.hsr2024.mungmungdoctortp.bnv2map

import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
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

    //네이버  - naver 지역검색 api
    //클라이언트id : GOd0jRtXpZfnd0bO9C3k
    //클라이언트 sectret : ItBhARmmRV


    private var LOCATION_PERMISSION = 1004
    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource
    private val marker = Marker()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val PERMISSION = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION)




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root

    }//oncreateView


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        naverMap()
        retrofitNaverPlaceSearch()
    }





    private fun naverMap(){

        fusedLocationClient =  LocationServices.getFusedLocationProviderClient(requireActivity())
        locationSource = FusedLocationSource(requireActivity(), LOCATION_PERMISSION )
        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
        var mapFragment: MapFragment? = fragmentManager.findFragmentById(R.id.map_fragment) as MapFragment?
        if (mapFragment == null){
            mapFragment = MapFragment.newInstance()
            fragmentManager.beginTransaction().add(R.id.map_fragment, mapFragment).commit()
        }

        mapFragment!!.getMapAsync(object : OnMapReadyCallback {
            override fun onMapReady(map: NaverMap) {

                naverMap = map
                naverMap.maxZoom = 18.0
                naverMap.minZoom = 5.0


                //현재 내 위치를 지도의 중심위치로 설정 /내위치-위도,경도,등..
                var myLocation: Location?= null
                val latitude : Double = myLocation?.latitude ?: 37.5666
                val longitude : Double =  myLocation?.longitude ?: 126.9782
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

    }// fun naverMap()







    private fun retrofitNaverPlaceSearch(){
        //레트로핏
        val builder = Retrofit.Builder()
            .baseUrl("https://openapi.naver.com")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())

        val retrofit = builder.build()
        val retrofitService = retrofit.create(RetrofitService::class.java)
        retrofitService.getNaverLocal("동물병원", 5).enqueue(object : Callback<NaverSearchPlaceResponse>{
            override fun onResponse(
                p0: Call<NaverSearchPlaceResponse>,
                p1: Response<NaverSearchPlaceResponse>
            ) {
                val result = p1.body()
                if (result != null) {
                    Toast.makeText(requireContext(), "${result.items[0].title}", Toast.LENGTH_SHORT).show()
                    binding.tv.setText("${result.items[1].category}")
                }
            }

            override fun onFailure(p0: Call<NaverSearchPlaceResponse>, p1: Throwable) {
                TODO("Not yet implemented")
            }

        })

    }//retrofitHelp()



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