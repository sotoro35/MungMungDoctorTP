package com.hsr2024.mungmungdoctortp.data


data class NoteData(val hospicalRecordData : HospitalRecordData, val aiRecordData: AIRecordData)
data class HospitalRecordData(
    val hospital_name:String,
    val disease_name:String,
    val price:String,
    val date:String
)

data class AIRecordData(
    val category:String,
    val disease_name1:String,
    val disease_name2:String,
    val date:String
)
