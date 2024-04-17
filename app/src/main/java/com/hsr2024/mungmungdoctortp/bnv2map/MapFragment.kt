package com.hsr2024.mungmungdoctortp.bnv2map

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
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
import kotlin.properties.Delegates


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

    private var myLocation: Location? = null//gps가안되거나 네트워크가안될수도있으니..
    private lateinit var nearbyOR24Location: LatLng

    private var is24on by Delegates.notNull<Boolean>()

    private lateinit var itemList : List<Place>


        override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        locationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        locationSource = FusedLocationSource(requireActivity(), LOCATION_PERMISSION )

        //사용자위치 허가받았나체크
        permissionCheck()

        //프레그먼트매니저한테 트렌젝션시작.
        val fragmentManager : FragmentManager = childFragmentManager
        mapFragment = fragmentManager.findFragmentById(R.id.map_fragment) as MapFragment?
            ?: MapFragment.newInstance().also {
                fragmentManager.beginTransaction().add(R.id.map_fragment, it).commit()
            }


        return binding.root

    }//oncreateView


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //onCreate에서 naver의 map_fragment로 트렌젝션시켰으니 그 이후에 LatLng(naver map꺼) 해야함
        nearbyOR24Location = LatLng(0.0, 0.0)
        is24on = false

        binding.civMyLocation.setOnClickListener { showMeOnMap() }
        binding.cvSearch.setOnClickListener { searchNearbyPlace() }
        binding.civ24.setOnClickListener {
            Log.d("ccccc", "찍힌냐")
            if(is24on){ //이미 켜져 있는 상태..
                //마커들 지우기..

                markerList24.forEach {
                    Log.d("ffff", "마커리스트가 널이되나")
                    it.map = null
                }
                markerList24.clear()
                //리사클러 지우기..
                searchNearbyPlace()
                is24on=false
            }else{
                //24 장소 찾아줘..
                search24Place()
            }
            //search24Place()
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



                //naver 지역검색 api호출- map이 준비가됬을때
                kakaoPlaceSearch("동물병원", myLocation!!.longitude, myLocation!!.latitude, R.drawable.icon_hospital_marker)


                naverMap = map
                naverMap.maxZoom = 18.0
                naverMap.minZoom = 5.0
                naverMap.locationSource = locationSource


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
                    14.0, //줌레벨
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
                    marker.captionText = "유저id"
                    marker.captionTextSize = 20f
                    marker.setCaptionAligns(Align.Top)
                    marker.captionOffset = 20
                    marker.captionColor = requireContext().resources.getColor(R.color.my_caption_color)

                    naverMap.locationSource = locationSource
                    ActivityCompat.requestPermissions(requireActivity(), permissions,LOCATION_PERMISSION)

            }//fun onMapReady(map: NaverMap)
        })// OnMapReadyCallback


    }//showMeOnMap()



    private fun searchNearbyPlace(){
       // naverMap.addOnCameraIdleListener { // 지도 이동이 멈추면 호출

            val cameraPosition = naverMap.cameraPosition
            val target = cameraPosition.target // 지도의 중심 좌표
            nearbyOR24Location = target
            //Toast.makeText(requireContext(), "${nearbyLocation.latitude}", Toast.LENGTH_SHORT).show()

            // 여기에서 얻은 latitude와 longitude 값을 사용하여 kakao 지역 api
             kakaoPlaceSearch("동물 병원",nearbyOR24Location.longitude, nearbyOR24Location.latitude, R.drawable.icon_hospital_marker)
       // }
    }//fun searchNearbyPlace()



    private fun search24Place(){
        Log.d("ffff", "서치24플레이스 메소드는 실행은됬나")
        val cameraPosition = naverMap.cameraPosition
        val target = cameraPosition.target // 지도의 중심 좌표
        nearbyOR24Location = target
        Log.d("ffff", "서치24.. 지도좌표는 받아오는가")
        kakaoPlaceSearch("24시 동물", nearbyOR24Location.longitude, nearbyOR24Location.latitude, R.drawable.icon_24_marker)

    }





    private fun kakaoPlaceSearch(query: String, longitude: Double, latitude: Double, markerIcon:Int ) {
        Log.d("ffff", "카카오서치는 하는가")
        //레트로핏
        val builder = Retrofit.Builder()
            .baseUrl("https://dapi.kakao.com")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())

        val retrofit = builder.build()
        val retrofitService = retrofit.create(RetrofitService::class.java)

        retrofitService.searchPlace(query, longitude, latitude).enqueue(object : Callback<KakaoSearchPlaceResponse>{
            override fun onResponse(
                p0: Call<KakaoSearchPlaceResponse>,
                p1: Response<KakaoSearchPlaceResponse>
            ) {
                Log.d("aaa", p1.body().toString())
                searchPlaceResponse = p1.body()
                //val itemList : List<Place> = searchPlaceResponse!!.documents
                itemList = searchPlaceResponse!!.documents
                Log.d("aaa1", searchPlaceResponse!!.documents[0].place_name)


                Log.d("ffff", "쇼플레이스온맵하기전")
                showPlaceOnMap(markerIcon)

                binding.recyclerView.adapter = MapRecycelrAdapter(requireContext(), itemList)
            }

            override fun onFailure(p0: Call<KakaoSearchPlaceResponse>, p1: Throwable) {
                TODO("Not yet implemented")
            }

        })

    }//kakaoPlaceSearch()





    var markerList : MutableList<Marker> = mutableListOf()
    var markerList24 : MutableList<Marker> = mutableListOf()


    private fun showPlaceOnMap(markerIcon : Int){

        Log.d("ffff", "쇼플레이스온맵되나")

        searchPlaceResponse?.documents?.forEach {place->//기차 한칸.
            val title =place.place_name
            var longitude = place.x.toDouble()
            var latitude = place.y.toDouble()


            val marker:Marker= Marker()
            marker.position = LatLng(latitude,longitude)
            marker.map = naverMap
            marker.icon = OverlayImage.fromResource(markerIcon)
            marker.width =100
            marker.height =100
            marker.setCaptionAligns(Align.Top)
            marker.captionOffset = 30
            //marker.captionColor = Color.RED


            //생성자로 받은 마커아이콘이 24시그림이면.
            if (markerIcon == R.drawable.icon_24_marker){
                //마커잘찍힘. 찍힌상태니까 true로바꿔줌
                markerList24.add(marker)
                Log.d("ffff", "나오나dd")

                is24on = true
            }else{
                markerList.add(marker)
            }






//            if (is24on==true){
//                //is24on이 true인 상황에서 버튼누르면 false로 바뀜. is24on이 false이면 마커리스트에 기차 0 개 만들기
//                binding.civ24.setOnClickListener {
//                    markerList24.forEach {
//                        Log.d("ffff", "마커리스트가 널이되나")
//                        it.map = null
//                    }
//                    markerList24.clear()
//                    is24on = false
//                }
//            }else{//is24on이 false다?
//                Toast.makeText(requireContext(), "dd", Toast.LENGTH_SHORT).show()
//                binding.civ24.setOnClickListener {
//                    is24on = true
//                    search24Place()
//                }
//            }



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
                    binding.recyclerView.adapter = MapRecycelrAdapter(requireContext(), listOf(place))
                } else {
                    //이미 현재 마커에 정보 창이 열러있을 경우 닫음
                    infoWindow.close()
                }
                true
            }//onclickListener

            marker.onClickListener = listener

        }//foreach


    }// showPlaceOnMap()







}//fragment

