package com.hsr2024.mungmungdoctortp.data

// code 종류
//  - 200 : 작업성공
// diagnosis_result 진단결과
// diagnostic_img_url 진단에 사용된 이미지
data class AiResponse(
    val diagnostic_img_url:String="",
    val diagnosis_result:String="",
    val code:String
)
data class AiResponses(
    val eye:AiResponse,
    val skin:AiResponse
)

data class LoginResponse(
    val email:String="",                // 회원 이메일
    val provider_id:String="",          // 회원 간편로그인 아이디
    val nickname:String="",             // 회원닉네임
    val userImgUrl:String="",           // 회원프로필이미지
    val pet_id:String="",               // 회원 선택된 펫 식별값
    val pet_name:String="",             // 회원 선택된 펫 이름
    val petImgUrl:String="",            // 회원 선택된 펫 프로필이미지
    val pet_birth_date:String="",       // 회원 선택된 펫 생일
    val pet_gender:String="",           // 회원 선택된 펫 생년월일
    val pet_neutered:String="",         // 회원 선택된 펫 중성화여부
    val pet_breed:String="",            // 회원 선택된 펫 견종
    val code:String=""                  // 4200 회원, 4204 회원 아님, 4203 이메일 로그인 정보 틀림, 1200 회원 추가 성공, 1201 회원 추가 실패
)

data class KakaoSearchPlaceResponse(var meta: PlaceMeta, var documents: List<Place>)

data class PlaceMeta(var total_count:Int,var pageable_count:Int, var is_end:Boolean)

data class Place(
    var id:String,
    var place_name:String,
    var category_name:String,
    var phone:String,
    var address_name:String,
    var road_address_name:String,
    var x:String,
    var y:String,
    var place_url:String,
    var distance:String
)

