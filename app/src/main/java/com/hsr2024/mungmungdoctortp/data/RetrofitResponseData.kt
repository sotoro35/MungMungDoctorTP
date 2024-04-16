package com.hsr2024.mungmungdoctortp.data

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

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

//qa 및 feed 데이터
data class QAData(val qa_id:String, val profile_imgurl: String, val nickname :String, val imgurl:String, val title:String, val content:String, val view_count:String, val comment_count:String, val myQA:String)
data class QADataList(val qaDatas: List<QAData>, val code:String)
data class FeedData(val feed_id:String, val profile_imgurl: String, val nickname: String, val imgurl:String, val favorite:String, var isFavorite:String, val comment: String, val content:String, val create_date:String, val myFeed:String )
data class FeedDataList(val feedDatas: List<FeedData>, val code:String)
data class CommentData(val comment_id:String, val profile_imgurl: String, val nickname: String, val content: String, val create_date: String, val myComment:String)
data class CommentDataList(val commentDatas: List<CommentData>, val code:String)

//반려견 정보
data class Pet(val pet_id:String, val pet_name:String,val pet_imageUrl:String,val pet_birthDate:String, val pet_gender:String, val pet_neutering:String, val pet_breed:String)
//반려견 정보 리스트
data class PetList(val petList:List<Pet>, val code:String)

data class HospitalRecordData(
    val id: String,                         // 병원 기록 식별 값
    val name: String,                       // 병원명
    val price: String,                      // 진단가격
    val diagnosis: String,                  // 진단명
    val visit_date: String,                 // 진료일
    val description: String,                // 진료내용
    val receipt_img_url: String,            // 영수증 이미지 url
    val clinical_img_url: String,           // 진료사진 이미지 url
)

data class HospitalRecordList(val hospitalRecordList:List<HospitalRecordData>, val code:String)

data class AIRecordData(
    val id: String,                         // ai 기록 식별 값
    val diagnosis_type: String,             // 진단한 ai type (eype or skin)
    val diagnostic_img_url: String,         // ai 진단한 반려견 이미지 url
    val diagnosis_result: String,           // ai 진단결과 리스트(결막염 80%, 유루증 70%..)
)

data class AIRecordList(val aiRecordList:List<AIRecordData>, val code:String)

data class AdditionVaccination(
    val id: String="",                         // 접종 기록 식별 값
    val heartworm: String="FALSE",             // 심상사상충 접종여부 TRUE OR FALSE
    val external_parasites: String="FALSE",    // 외부기생충 접종여부 TRUE OR FALSE
    val vaccine: String="",                    // 기타 접종 이름
    val date: String="",                       // 접종날짜
    val hospital: String="",                   // 접종할 병원 이름
    val memo: String=""                        // 접종 시 메모정보
)

data class EssentialVaccination(
    val id: String="",                         // 접종 기록 식별 값
    val shot_number: String="",                // 몇회차 접종(1 ~ 6)
    val comprehensive: String="",              // 종합백신 1차, 종합백신 2차, 종합백신 3차, 종합백신 4차, 종합백신 5차, ""
    val corona_enteritis: String="",           // 코로나 장염1차, 코로나 장염 2차, ""
    val kennel_cough: String="",               // 켄넬코프 1차, 켄넬코프 2차, ""
    val influenza: String="",                  // 인플루엔자 1차, 인플루엔자 2차, ""
    val antibody_titer: String="",             // 항체가검사, ""
    val rabies: String="",                     // 광견병, ""
    val date: String="",                       // 접종날짜
    val hospital: String="",                   // 접종할 병원 이름
    val memo: String=""                        // 접종 시 메모정보
)

data class AdditionVaccinationList(val vaccinationList:List<AdditionVaccination>, val code:String)
data class EssentialVaccinationList(val vaccinationList:List<EssentialVaccination>, val code:String)

