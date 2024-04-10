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
import com.hsr2024.mungmungdoctortp.adapter.MapRecycelrAdapter
import com.hsr2024.mungmungdoctortp.data.KakaoSearchPlaceResponse
import com.hsr2024.mungmungdoctortp.data.Place
import com.hsr2024.mungmungdoctortp.databinding.FragmentMapBinding
import com.hsr2024.mungmungdoctortp.network.RetrofitService
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Align
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
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



    private var LOCATION_PERMISSION = 1004
    private var mapFragment: MapFragment? =null

    // [Google Fused Location API 사용] 라이브러리이름:play-services-location 추가하기
    private var locationProviderClient: FusedLocationProviderClient? = null
    private var locationSource: FusedLocationSource? = null

    //kakao search API응답결과 객체 참조변수
    var searchPlaceResponse:KakaoSearchPlaceResponse? = null




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        locationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        locationSource = FusedLocationSource(requireActivity(), LOCATION_PERMISSION )
        return binding.root

    }//oncreateView


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //사용자위치 허가받았나체크
        permissionCheck()

        //프레그먼트매니저한테 트렌젝션시작.
        val fragmentManager : FragmentManager = childFragmentManager
        mapFragment = fragmentManager.findFragmentById(R.id.map_fragment) as MapFragment?
            ?: MapFragment.newInstance().also {
                fragmentManager.beginTransaction().add(R.id.map_fragment, it).commit()
            }

    }//onViewCreated





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
            permissionResultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }else{
            //위치정보수집에 동의.허가받았으니 사용자현위치받아오자
            requestMyLocation()
        }
    }//permissionCheck()



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






    private var myLocation: Location? = null//gps가안되거나 네트워크가안될수도있으니..


    //위치정보 새로 갱신할때마다 발동하는 콜백 메소드. 추상클래스.
    private val locationCallback= object : LocationCallback(){
        override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)
            myLocation = p0.lastLocation

            //위치탐색 종료됬으니 내 위치정보 업데이트 그만...
            locationProviderClient?.removeLocationUpdates(this)//this:Location Callback

            //내위치 얻어왔으니 이제!!! 네이버map에 내위치 보여줘
            showMeOnMap()
        }
    }







    private lateinit var naverMap: NaverMap
    private val marker = Marker()
    private var permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)




    private fun showMeOnMap(){

        mapFragment!!.getMapAsync(object : OnMapReadyCallback {
            override fun onMapReady(map: NaverMap) {

                naverMap = map
                naverMap.maxZoom = 18.0
                naverMap.minZoom = 5.0
                naverMap.locationSource = locationSource

                //naver 지역검색 api호출- map이 준비가됬을때
                kakaoPlaceSearch()



                //현재 내 위치를 지도의 중심위치로 설정 /내위치-위도,경도,등..
                var latitude : Double = myLocation?.latitude ?: 37.5666
                var longitude : Double =  myLocation?.longitude ?: 126.9782
                val myPos = LatLng(latitude, longitude)

                //내 위치로 지도 카메라 이동하기
                val cameraUpdate:CameraUpdate= CameraUpdate.scrollTo(myPos)
                naverMap.moveCamera(cameraUpdate)
                //위치추적 모드 활성화???
                naverMap.locationTrackingMode = LocationTrackingMode.Follow



                //카메라 설정
                val cameraPosition = CameraPosition(
                    myPos,
                    16.0, //줌레벨
                    20.0, //기울임 각도
                    180.0 //베어링 각도
                )

                naverMap.cameraPosition = cameraPosition

                    //마커 포지션
                    marker.position = LatLng(myLocation!!.latitude, myLocation!!.longitude)
                    marker.map = naverMap
                    marker.icon = OverlayImage.fromResource(R.drawable.my_marker)
                    marker.width =120
                    marker.height =120
                    marker.captionText = "나 여깄어용"
                    marker.captionTextSize = 20f
                    marker.setCaptionAligns(Align.Top)
                    marker.captionOffset = 30
                    marker.captionColor = Color.RED


                    naverMap.locationSource = locationSource
                    ActivityCompat.requestPermissions(requireActivity(), permissions,LOCATION_PERMISSION)

            }//fun onMapReady(map: NaverMap)
        })// OnMapReadyCallback


    }//naverMapAPI()





    var markerList : MutableList<Marker> = mutableListOf()


    private fun showPlaceOnMap(){

        searchPlaceResponse?.documents?.forEach {//기차 한칸.
            val title =it.place_name
            var longitude = it.x.toDouble()
            var latitude = it.y.toDouble()


            val marker:Marker= Marker()
            marker.position = LatLng(latitude,longitude)
            marker.map = naverMap
            marker.icon = OverlayImage.fromResource(R.drawable.hospital_marker)
            marker.width =120
            marker.height =120
            marker.setCaptionAligns(Align.Top)
            marker.captionOffset = 30
            marker.captionColor = Color.RED

            markerList.add(marker)

            val infoWindow = InfoWindow()
            infoWindow.adapter = object : InfoWindow.DefaultTextAdapter(requireContext()){
                override fun getText(p0: InfoWindow): CharSequence {
                    return "$title"
                }
            }

            //마커 클릭 시
            val listener = Overlay.OnClickListener { overlay ->

                markerList.forEach {
                    it.infoWindow?.close()
                }

                val marker = overlay as Marker
                if (marker.infoWindow == null){
                    //현재 마커에 정보 창이 열려있지 않을 경우 엶
                    infoWindow.open(marker)
                } else {
                    //이미 현재 마커에 정보 창이 열러있을 경우 닫음
                    infoWindow.close()
                }
                true
            }//onclickListener

            marker.onClickListener = listener

        }//foreach


    }// showPlaceOnMap()







    private fun kakaoPlaceSearch() {
        //레트로핏
        val builder = Retrofit.Builder()
            .baseUrl("https://dapi.kakao.com")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())

        val retrofit = builder.build()
        val retrofitService = retrofit.create(RetrofitService::class.java)

        retrofitService.searchPlace("동물병원", myLocation?.longitude.toString(), myLocation?.latitude.toString()).enqueue(object : Callback<KakaoSearchPlaceResponse>{
            override fun onResponse(
                p0: Call<KakaoSearchPlaceResponse>,
                p1: Response<KakaoSearchPlaceResponse>
            ) {
                Log.d("aaa", p1.body().toString())
                searchPlaceResponse = p1.body()
                val itemList : List<Place> = searchPlaceResponse!!.documents
                Log.d("aaa1", searchPlaceResponse!!.documents[0].place_name)

                showPlaceOnMap()

                binding.recyclerView.adapter = MapRecycelrAdapter(requireContext(), itemList)
            }

            override fun onFailure(p0: Call<KakaoSearchPlaceResponse>, p1: Throwable) {
                TODO("Not yet implemented")
            }

        })

    }//naverPlaceSearch()




}//fragment