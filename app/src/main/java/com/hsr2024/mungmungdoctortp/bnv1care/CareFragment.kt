package com.hsr2024.mungmungdoctortp.bnv1care

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Note
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param
import com.hsr2024.mungmungdoctortp.G
import com.hsr2024.mungmungdoctortp.R
import com.hsr2024.mungmungdoctortp.adapter.CareSelectDogAdapter
import com.hsr2024.mungmungdoctortp.adapter.MypageDogAdapter
import com.hsr2024.mungmungdoctortp.data.DeleteDog
import com.hsr2024.mungmungdoctortp.data.Individual
import com.hsr2024.mungmungdoctortp.data.LoginResponse
import com.hsr2024.mungmungdoctortp.data.Pet
import com.hsr2024.mungmungdoctortp.data.Pet2
import com.hsr2024.mungmungdoctortp.data.PetList
import com.hsr2024.mungmungdoctortp.databinding.FragmentCareBinding
import com.hsr2024.mungmungdoctortp.network.RetrofitCallback
import com.hsr2024.mungmungdoctortp.network.RetrofitProcess
import okhttp3.internal.format
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class CareFragment:Fragment() {

    private val binding by lazy { FragmentCareBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.selectPet.setOnClickListener { selectPet() }
        binding.note.setOnClickListener {//노트액티비티 이동
             startActivity( Intent(requireContext(), NoteActivity::class.java) )    }
        binding.eyeAi.setOnClickListener { startActivity(Intent(requireContext(),EyeAiActivity::class.java)) }
        binding.skinAi.setOnClickListener { startActivity(Intent(requireContext(),SkinAiActivity::class.java)) }
        binding.btnHealth.setOnClickListener { //헬쓰인포액티비티 이동
            startActivity( Intent(requireContext(), HealthInfoActivity::class.java) )
        }

        //binding.btnVaccine.setOnClickListener { startActivity(Intent(requireContext(),VaccineActivity::class.java)) }

        load()


    }//onView...

    private fun changePetProfile(){

        if (G.pet_name != null && G.pet_name != ""){
            binding.petBreed.visibility = View.VISIBLE
            binding.petBirthDate.visibility = View.VISIBLE
            binding.petGender.visibility = View.VISIBLE
            binding.petNeutering.visibility = View.VISIBLE
            binding.line1.visibility = View.VISIBLE
            binding.line2.visibility = View.VISIBLE

            var age = calculateAge(G.pet_birthDate)
            binding.petName.text = G.pet_name
            binding.petBreed.text = G.pet_breed
            binding.petBirthDate.text = "만 ${age}세"
            binding.petGender.text = G.pet_gender
            binding.petNeutering.text = "중성화 ${G.pet_neutering}"
            if (G.pet_imageUrl == null || G.pet_imageUrl == "") {
                binding.petImageUrl.setImageResource(R.drawable.pet_image)
            }else Glide.with(requireContext()).load("http://43.200.163.153/img/${G.pet_imageUrl}").into(binding.petImageUrl)
        }
    }

    private fun load(){

        val params= Individual("${G.user_email}", "${G.user_providerId}", "${G.loginType}")
        RetrofitProcess(requireContext(),params=params, callback = object : RetrofitCallback {
            override fun onResponseListSuccess(response: List<Any>?) {}

            override fun onResponseSuccess(response: Any?) {
                val data=(response as LoginResponse)
                Log.d("login data","$data")
                // LoginResponse 데이터 출력(email, provider_id, nickname, userImgUrl, pet_id, pet_name, petImgUrl, pet_birth_date, pet_gender, pet_neutered, code)
                //  - 4204 서비스 회원 아님, 1240 회원 정보 조회 성공, 1250 회원 정보 조회 실패
                when(data.code){
                    "4204" -> Toast.makeText(requireContext(), "관리자에게 문의하세요", Toast.LENGTH_SHORT).show()
                    "1250" -> Toast.makeText(requireContext(), "관리자에게 문의하세요", Toast.LENGTH_SHORT).show()
                    "1240" -> {

                        var neutering = when(data.pet_neutered){
                            "1" -> "O"
                            "0" -> "X"
                            else -> data.pet_neutered
                        }

                        //G.user_email = data.email ?: ""
                       // G.user_providerId = data.provider_id ?: ""
                        G.user_nickname = data.nickname ?: ""
                        G.user_imageUrl = data.userImgUrl ?: ""
                        G.pet_id = data.pet_id ?: ""
                        G.pet_name = data.pet_name ?: ""
                        G.pet_imageUrl = data.petImgUrl ?: ""
                        G.pet_birthDate = data.pet_birth_date ?: ""
                        G.pet_gender = data.pet_gender ?: ""
                        G.pet_neutering = neutering ?: ""
                        G.pet_breed = data.pet_breed ?: ""

                        Log.d("펫정보1","${data.pet_name}")
                        Log.d("펫정보2","${data.petImgUrl}")

                        if (G.pet_name != null && G.pet_name != ""){
                            binding.petBreed.visibility = View.VISIBLE
                            binding.petBirthDate.visibility = View.VISIBLE
                            binding.petGender.visibility = View.VISIBLE
                            binding.petNeutering.visibility = View.VISIBLE
                            binding.line1.visibility = View.VISIBLE
                            binding.line2.visibility = View.VISIBLE

                            Log.d("펫정보3","${G.pet_name}")

                            var age = calculateAge(G.pet_birthDate)
                            binding.petName.text = G.pet_name
                            binding.petBreed.text = G.pet_breed
                            binding.petBirthDate.text = "만 ${age}세"
                            binding.petGender.text = G.pet_gender
                            binding.petNeutering.text = "중성화 ${neutering}"
                        }else{
                            binding.petName.text = "마이페이지에서\n반려견을 등록해주세요"
                            binding.petBreed.visibility = View.INVISIBLE
                            binding.petBirthDate.visibility = View.INVISIBLE
                            binding.petGender.visibility = View.INVISIBLE
                            binding.petNeutering.visibility = View.INVISIBLE
                            binding.line1.visibility = View.INVISIBLE
                            binding.line2.visibility = View.INVISIBLE
                        }

                        if (data.petImgUrl == null || data.petImgUrl == "") {
                            binding.petImageUrl.setImageResource(R.drawable.pet_image)
                        }else Glide.with(requireContext()).load("http://43.200.163.153/img/${data.petImgUrl}").into(binding.petImageUrl)
                    }
                }
            }

            override fun onResponseFailure(errorMsg: String?) {
                Log.d("login fail",errorMsg!!) // 에러 메시지
            }

        }).userLoadRequest()
    }


    lateinit var alertDialog: AlertDialog
    @SuppressLint("MissingInflatedId")
    private fun selectPet(){
        val dialogV = layoutInflater.inflate(R.layout.dialog_dog_select,null)
        val recyclerView = dialogV.findViewById<RecyclerView>(R.id.recycler_select_pet)
        val builder = AlertDialog.Builder(requireContext())
        dialogV.findViewById<TextView>(R.id.dog_empty).visibility = View.VISIBLE
        builder.setView(dialogV)
        alertDialog = builder.create()

        val params= Individual("${G.user_email}", "${G.user_providerId}", "${G.loginType}")
        RetrofitProcess(requireContext(),params=params, callback = object : RetrofitCallback {
            override fun onResponseListSuccess(response: List<Any>?) {}

            override fun onResponseSuccess(response: Any?) {
                val data=(response as PetList)
                Log.d("signup code","$data")

                when (data.code) {  // - 5500 펫 목록 성공, 5501 펫 목록 실패, 4204 서비스 회원 아님
                    "5501" -> Toast.makeText(requireContext(), "펫 목록 실패", Toast.LENGTH_SHORT)
                        .show()

                    "4204" -> Toast.makeText(requireContext(), "회원 정보 확인필요", Toast.LENGTH_SHORT)
                        .show()

                    "5500" -> {
//                        Toast.makeText(requireContext(), "리스트 가져오기 성공", Toast.LENGTH_SHORT)
//                            .show()

                        val pets: List<Pet> = data.petList.sortedByDescending {it.pet_id}

                        if (pets != null){
                            dialogV.findViewById<TextView>(R.id.dog_empty).visibility = View.INVISIBLE
                            val mypageAdapter = CareSelectDogAdapter(requireContext(),pets)
                            recyclerView.adapter = mypageAdapter
                            mypageAdapter.notifyDataSetChanged()
                        }
                    }
                    else -> Toast.makeText(requireContext(), "관리자문의", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponseFailure(errorMsg: String?) {
                Log.d("signup fail",errorMsg!!) // 에러 메시지
            }

        }).petListRequest()

        dialogV.findViewById<TextView>(R.id.btn_select_pet).setOnClickListener {
            changePetProfile()
            petSelect()
            alertDialog.dismiss() }
        dialogV.findViewById<TextView>(R.id.btn_close).setOnClickListener { alertDialog.dismiss() }
        alertDialog.show()

    }

    private fun petSelect(){

        val params= DeleteDog("${G.user_email}", "${G.user_providerId}", "${G.pet_id}", "${G.loginType}")
        RetrofitProcess(requireContext(),params=params, callback = object : RetrofitCallback {
            override fun onResponseListSuccess(response: List<Any>?) {}

            override fun onResponseSuccess(response: Any?) {
                val data=(response as LoginResponse)
                Log.d("login data","$data") // LoginResponse 데이터 출력(email, provider_id, nickname, userImgUrl, pet_id, pet_name, petImgUrl, pet_birth_date, pet_gender, pet_neutered, code)
            }                               //  - 4204 서비스 회원 아님, 5600 펫 선택 성공, 5601 펫 선택 실패

            override fun onResponseFailure(errorMsg: String?) {
                Log.d("login fail",errorMsg!!) // 에러 메시지
            }

        }).petSelectRequest()
    }

    /**
     * 생년월일을 기준으로 현재 나이 계산
     * @param unix unixtimestamp
     */

    fun calculateAge(s: String?): String {
        val dateString = s ?: return "생일 정보가 없습니다." // s가 null인 경우 예외 처리
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        // 현재 날짜 가져오기
        val currentDate = Calendar.getInstance()

        // 생일 정보 파싱
        val dateOfBirth = format.parse(dateString) ?: return "올바르지 않은 생일 형식입니다." // 생일 정보가 null이면 예외 처리

        // 생일 정보를 Calendar 객체로 변환
        val birthCalendar = Calendar.getInstance()
        birthCalendar.time = dateOfBirth

        // 나이 계산
        var age = currentDate.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR)

        // 생일이 지났는지 확인
        if (birthCalendar.get(Calendar.MONTH) > currentDate.get(Calendar.MONTH) ||
            (birthCalendar.get(Calendar.MONTH) == currentDate.get(Calendar.MONTH) &&
                    birthCalendar.get(Calendar.DAY_OF_MONTH) > currentDate.get(Calendar.DAY_OF_MONTH))
        ) {
            age-- // 아직 생일이 지나지 않았다면 나이에서 1을 뺍니다.
        }

        return "$age"
    }

}