package com.hsr2024.mungmungdoctortp.data

data class AddVaccine(
    val vaccine_kind:String,
    val vaccine_date:String,
    val vaccine_hospital:String,
    val vaccine_memo:String
)
data class MandatoryVaccine(
    val mandatory_kind : String,
    val mandatory_date:String,
    val mandatory_hospital:String,
    val mandatory_memo:String
)

