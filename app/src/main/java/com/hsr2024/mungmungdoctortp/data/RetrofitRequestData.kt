package com.hsr2024.mungmungdoctortp.data

//login 시 보낼 데이터
data class LoginData(val email:String="", val password:String="", val access_token:String="", val login_type:String)
//회원가입 시 보낼 데이터
data class SignUpData(val email:String, val password:String, val nickname:String)
// type 종류
//  - view : 안구, 피부 ai 진단결과 정보 요청
//  - del_eye : 안구 ai 진단결과 삭제
//  - del_skin : 피부 ai 진단결과 삭제
//  - add_eye : 안구 진단결과 추가
//  - add_skin : 피부 진단결과 추가
//  diagnosis_result : 진단결과(ex 결막염 80% , 유루증 70%)
data class AiRequest(val pet_id:String, val email:String="", val provider_id:String="", val diagnosis_result:String="", val type:String)
//탈퇴
data class UserDelete(val email:String="", val password:String="", val provider_id:String="", val login_type:String)
//회원 정보 수정
data class UserChange(val email:String="", val password:String="", val provider_id:String="", val userImgUrl:String="", val login_type:String)
//반려견 추가
data class AddDog(val email:String="", val provider_id:String="", val pet_name:String,val pet_imageUrl:String,val pet_birthDate:String, val pet_gender:String, val pet_neutering:String, val pet_breed:String, val login_type:String)
//반려견 수정
data class ModifyDog(val email:String="", val provider_id:String="", val pet_id:String, val pet_name:String,val pet_imageUrl:String,val pet_birthDate:String, val pet_gender:String, val pet_neutering:String, val pet_breed:String, val login_type:String)
//반려견 삭제
data class DeleteDog(val email:String="", val provider_id:String="", val pet_id:String, val login_type:String)
// 개인 서비스 요청 - 펫 목록
data class Individual(val email:String="", val provider_id:String="", val login_type:String)

//feed 댓글 목록
data class FeedCommentList(val feed_id:String, val email:String="", val provider_id:String="", val login_type:String="")

//feed 추가(feed_id 값 안넣어도 됨), 수정, 삭제(imgurl, content 값 안 넣어도 됨)
data class AddorModifyorDeleteFeed(val email:String="", val provider_id:String="", val login_type:String, val feed_id: String="", val imgurl:String="", val content:String="")

//qa 추가(qa_id 값 안넣어도 됨), 수정, 삭제(imgurl, content 값 안 넣어도 됨)
data class AddorModifyorDeleteQA(val email:String="", val provider_id:String="", val login_type:String, val qa_id: String="", val imgurl:String="", val title:String="", val content:String="")

//댓글 추가(comment_id 값 안넣어도 됨), 수정, 삭제(content 값 안 넣어도 됨). board_id(feed_id or qa_id)
data class AddorModifyorDeleteComment(val email:String="", val provider_id:String="", val login_type:String, val board_id: String, val comment_id:String="", val content:String="")

// feed favor 요청
data class FeedFavor(val email:String="", val provider_id:String="", val login_type:String, val feed_id: String, val favor_add:String) // favor True or False

// qa view 요청
data class QAView(val email:String="", val provider_id:String="", val login_type:String, val qa_id: String)

//qa 댓글 목록
data class QACommentList(val qa_id:String, val email:String="", val provider_id:String="", val login_type:String)

//병원, ai 기록 목록
data class HospitalorAiRecordList(val email:String="", val provider_id:String="", val login_type:String, val pet_id:String, val date:String="")

//병원 기록 추가하기
data class HospitalAdd(
    val email:String="", val provider_id:String="", val login_type:String, val pet_id:String,
    val name: String,                       // 병원명
    val price: String,                      // 진단가격
    val diagnosis: String,                  // 진단명
    val visit_date: String,                 // 진료일
    val description: String,                // 진료내용
    val receipt_img_url: String,            // 영수증 이미지 url
    val clinical_img_url: String            // 진료사진 이미지 url
    )

//ai 기록 추가하기
//병원 기록 추가하기
data class AIAdd(
    val email:String="", val provider_id:String="", val login_type:String, val pet_id:String,
    val diagnosis_type: String,             // 진단한 ai type (eype or skin)
    val diagnostic_img_url: String,         // ai 진단한 반려견 이미지 url
    val diagnosis_result: String             // ai 진단결과 리스트(결막염 80%, 유루증 70%..)
)