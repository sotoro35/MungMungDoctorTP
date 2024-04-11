package com.hsr2024.mungmungdoctortp.data

import com.hsr2024.mungmungdoctortp.R

data class HealthDetailData(var eye1: Eye1, var eye2: Eye2,  var eye3: Eye3,  var eye4: Eye4,  var eye5: Eye5,
var skin1: Skin1, var skin2: Skin2,var skin3: Skin3,var skin4: Skin4,var skin5: Skin5,var skin6: Skin6

                            )




data class Eye1(
    val name:String = "안검염/각막염/결막염",
    val image: Int = R.drawable.iv_eye1,
    val category :String = "안구",
    val definition:String = "개의 눈 흰자를 보호하는 부위로 이 부위에 염증이 발생하는 것.",
    val cause :String = "물리·화학적 자극,  세균 또는 바이러스 감염,  알레르기 반응 등이 원인이 됨.",
    val symptom:String = "눈꺼풀을 뒤집으면 결막이 충혈되고 부어 있는 것을 알 수 있음. 눈물이나 눈곱이 많이 나오고 가렵기 때문에 개는 " +
            "앞발로 눈을 비비거나 바닥에 얼굴을 비벼댐.",
    val method:String = "물리, 화학적 자극일 경우 안약이나 연고를 발라 염증을 억제함. 알레르기성 결막염일 경우 대증치료에 더해 알레르겐을 찾아 제거해야함. 병원에 갈 수 없는 상황이라면 임시방편으로 냉찜질으로 가려움을 완화함. 예방을 위해서는 반려견의 알레르겐이 무엇인지를" +
            " 미리 알아두고, 가급적 정해진 식사만 하도록 해야함."
)

data class Eye2(
    val name:String = "안검종양",
    val image: Int = R.drawable.iv_eye1,
    val category :String = "안구",
    val definition:String = "눈꺼풀에 발생하는 종양",
    val cause :String = "각막질환, 알레르기로 인한 염증이나 눈물의 지방 성분을 분비하는 마이봄샘이 막히는 것을 대표적인 원인으로 봄.",
    val symptom:String = "각막을 지속적으로 자극하여 심한 통증을 유발하고 크기가 커지면 눈이 잘 감기지 않아 만성 안구건조증이 발생할 수 있음.",
    val method:String = "눈꺼풀에는 여러 분비 기관이 있어 각 기관들로부터 종양이 발생할 수 있음 "
)

data class Eye3(
    val name:String = "유루증",
    val image: Int = R.drawable.iv_eye1,
    val category :String = "안구",
    val definition:String = "눈물이 결막낭 안에 괴어 눈꺼풀을 넘어 흘러나오는 증세",
    val cause :String = "눈물이 많이 분비되거나 눈자위에 누선에서 코로 이어지는 비루관으로 배출되지 못해서 발생함. 선천적으로 누점이나 비루관에 이상이 있는 경우나 각막염이나 결막염의 영향으로 발생",
    val symptom:String = "눈물이 지나치게 많이 흘러나옴. 눈물은 햇빛을 받아 공기와 접촉하면 산화되어 검붉은 갈색으로 보임. 남은 눈물은 균이 번식하여 냄새가 나거나 피부염이 생길 수 있음.",
    val method:String = "염증인 경우에는 항생물질을 투여한다. 누점이나 누관을 세정하는 경우도 있다."
)

data class Eye4(
    val name:String = "각막궤양",
    val image: Int = R.drawable.iv_eye1,
    val category :String = "안구",
    val definition:String = "염증이 각막의 깊은 부위까지 퍼져 궤양으로 변하는 단계, 각막궤양의 초기증상.",
    val cause :String = "외상성- 외부 물질이 각막을 자극해서 생기는 염증 대표적인 예로는 눈썹이 눈을 찌르거나, 샴푸 등의 약품이 눈을 자극해서 발생하는 경우가 있음\n비외상성- 곰팡이, 세균, 바이러스 등에 의한 감염, 대사장애, 알레르기 반응 등이 있다.  ",
    val symptom:String = "각막에는 많은 신경이 분포돼 있어 염증이 생기면 큰 통증을 느낌, 눈을 계속 감고 있거나 깜빡거릴 수 있음, 앞발로 눈을 문지르고 있어 한눈에 봐도 눈에 이상이 있다고 짐작 가능, 통증이 심하면 얼굴을 바닥에 문지르며 아파하고, 눈물을 많이 흘리기도 함.",
    val method:String = "감염병의 경우 세균, 바이러스를 치료하고 안약을 투여하는 내과치료를 진행, 개가 통증 때문에 눈을 비벼 각막염이 악화될 수 있으니 목에 엘리자베스 칼라를 씌워 눈을 보호하도록 함."
)

data class Eye5(
    val name:String = "백내장",
    val image: Int = R.drawable.iv_eye1,
    val category :String = "안구",
    val definition:String = "수정체가 뿌옇게 변하거나 불투명해지는 병.",
    val cause :String = "대게 노화에 의해 평균 6세를 넘긴 시점부터 서서히 증상이 진행되며 외상이나 당뇨병, 내분비 이상 등이 원인이 되는 경우도 있음.",
    val symptom:String = "시력이 저하되어 여기저기 부딪치거나 움직이는 것에 반응하지 않게 되고 계단을 내려가기 무서워하는 등 개의 행동 변화 때문에 주인이 눈치채는 일이 많음. 눈을 관찰하면 수정체가 하얗게 탁하거나 동공이 항상 확장되어 있음. 특히 어둑한 곳에서는 시력이 현저히 떨어지고, 질병이 진행되면 실명에 이를 수 있음.",
    val method:String = "백내장이 극적으로 개선되는 경우는 희박하지만 수술적으로 백내장 물질을 제거하고 제거한 자리에 수정체를 대신하는 인공렌즈를 삽입할 수 있지만 합병증으로 인해 수술이 불가능 할 수 있음. 예방법은 따로 없지만 주기적인 검진이 필요함."
)















data class Skin1(
    val name:String = "구진/플라크",
    val image: Int = R.drawable.iv_eye1,
    val category :String = "피부",
    val definition:String = "알레르기성 접촉성 피부염인 피부사상균으로 인한 질환 중 하나임.",
    val cause :String = "작은 화학물질, 일명 항원과의 지속적인 피부 접촉으로 발생함.",
    val symptom:String = "과각화증, 두꺼운 피부, 소양감, 가려움, 탈모, 무모증, 피부 구진, 피부 균열, 열구, 피부 농포, 부종, 색소 과잉 침착, 수포, 대수포, 물집, 피부 인설, 비듬, 비늘, 피부 통증, 피부 플라크, 축축한 피부, 건조한 피부, 가피, 딱지" +
            ", 궤양, 미란, 찰과상, 화농성 분비물, 홍반, 염증, 발적 등이 포함.",
    val method:String = "치료 및 예방으로 알레르기 유발물질을 피하고, 글루코코티코이드" +
            " 함유 국소제를 사용하거나, 경구 투여할 때는 수의사의 처방을 따름."
)



data class Skin2(
    val name:String = "비듬/각질/상피성잔고리",
    val image: Int = R.drawable.iv_eye1,
    val category :String = "피부",
    val definition:String = "인설의 원형 테두리 및 가장자리가 벗겨지는 원형 병변.",
    val cause :String = "알러지나 아토피가 주요 원인이며, 곰팡이나 세균 감염증 및 면역력 저하도 원인이 됨.",
    val symptom:String = "지속적인 가려움증, 탈모, 고약한 냄새",
    val method:String = " 경구 및 국소 항생제 치료가 포함되며 약용 샴푸를 통해 목욕을 자주 해야 함\n"
)


data class Skin3(
    val name:String = "태선화/과다색소침착",
    val image: Int = R.drawable.iv_eye1,
    val category :String = "피부",
    val definition:String = "환경 알러젠에 대해 면역이 과항진되는 개의 알러지성 피부염",
    val cause :String = "알레르기 반응 유발 물질은 크게 환경, 식이로 구분되며, 이 중 아토피는 주로 꽃가루나 먼지 등의 환경 알러젠으로 유발됨",
    val symptom:String = "발적이나 부종이 생기면서 만성으로 이어지면 피부가 두꺼워지는 태선화 또는 색소침착이 나타남",
    val method:String = "꽃가루가 많은 봄철에 증상이 심하게 나타난다면 해당 계절에는 산책을 자제하기. 먼지가 없도록 집 안 환경을 청결히 유지. 지속적인 소양감으로 씹고, 긁고, 비비는 증상 등을" +
            " 보이면 2차 감염의 위험이 있으므로 필요에 따라 넥카라 착용, 행동제한을 실시."
)


data class Skin4(
    val name:String = "농포/여드름",
    val image: Int = R.drawable.iv_eye1,
    val category :String = "피부",
    val definition:String = "세균감염으로 인해서 피부 표면에 농이 발생하는 피부 질환",
    val cause :String = "피부의 면역력이 약한 나이든 강아지에게서 많이 생김. 면역력이 약한 틈을 타 세균이 감염됨.",
    val symptom:String = "주로 털이 없는 부위(턱, 배, 겨드랑이와 사타구니 등)에 발생함. 자주 부풀어 오르고 붉어짐. 가운데는 하얀 고름으로 가득 차 있음",
    val method:String = "에어컨, 제습기 등을 통해 적절한 온도와 습도 유지. 털과 피부를 청결하게 유지해 세균 감염 예방. 증상이 경미한 경우 약용샴푸와 연고를 이용함." +
            " 증상이 심한 경우 항생제가 처방되며 3-4주 혹은 그 이상 처방되기도 함."
)


data class Skin5(
    val name:String = "미란/궤양",
    val image: Int = R.drawable.iv_eye1,
    val category :String = "피부",
    val definition:String = "점막근육판 결손 상태를 미란이라 하며, 이를 넘어 조직이 결손되거나 함몰된 상태를 궤양이라 함.",
    val cause :String = "조직의 염증이 진행되어 발생하거나 조직으로의 산소 및 영양분의 공급이 원활하게 이루어지지 못해 발생하게 됨. " +
            "또한 신체 외상, 스트레스, 수면 부족, 면역 체계의 변화가 원인이 될 수 있음.",
    val symptom:String = "피부를 비롯하여 소화기, 호흡기, 심혈관, 비뇨생식기 등에 발생함.",
    val method:String = "산소와 혈류가 차단되지 않게 하고, 피부가 한 곳에 오래도록 압력이 가해지지 않도록 해야 함. " +
            "치료법은 염증의 감소, 면역계의 통제에 초점을 두어야 함. "
)


data class Skin6(
    val name:String = "결절/종괴",
    val image: Int = R.drawable.iv_eye1,
    val category :String = "피부",
    val definition:String = "결절은 증상을 유발하지 않을 수 있는 작고 단단하며 둥근 모양의 종양. 종괴는 종종 통증 및 부기와 같은 증상과 관련된 더 큰 종양임.",
    val cause :String = "유전적 요소뿐 아니라 오염된 공기 등에 함유된 화학물질, 자외선, 방사선, 바이러스 등이 원인이 됨.",
    val symptom:String = "비정상적인 체중 감소, 피로, 피부색의 변화, 가려움, 또는 비정상적인 상처, 소화불량 등.",
    val method:String = "정기적인 건강 검진이 필요함. 양성 종양의 치료는 주로 수술적 제거가 주를 이룸. 악성 종양은 빠른 전이 때문에 조기 발견과 적극적 치료가 " +
            "생존율을 높임. 주로 방사선 치료, 화학 요법, 수술 등이 있음."
)


