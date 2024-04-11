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
data class UserChange(val email:String, val userImgUrl:String, val password:String)
//반려견 추가
data class addDog(val email:String, val pet_name:String,val pet_imageUrl:String,val pet_birthDate:String, val pet_gender:String, val pet_neutering:String, val pet_breed:String)
//반려견 정보
data class Pet(val email:String, val pet_id:String, val pet_name:String,val pet_imageUrl:String,val pet_birthDate:String, val pet_gender:String, val pet_neutering:String, val pet_breed:String)
//반려견 정보 리스트
data class PetList(val petList:List<Pet>)