package com.hsr2024.mungmungdoctortp.bnv3community

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.tabs.TabLayout
import com.hsr2024.mungmungdoctortp.G
import com.hsr2024.mungmungdoctortp.R
import com.hsr2024.mungmungdoctortp.data.AddorModifyorDeleteFeed
import com.hsr2024.mungmungdoctortp.data.FeedAddOrUpdate
import com.hsr2024.mungmungdoctortp.data.FeedData
import com.hsr2024.mungmungdoctortp.data.FeedDataList
import com.hsr2024.mungmungdoctortp.data.FeedFavor
import com.hsr2024.mungmungdoctortp.data.Individual
import com.hsr2024.mungmungdoctortp.data.QAData
import com.hsr2024.mungmungdoctortp.data.QADataList
import com.hsr2024.mungmungdoctortp.databinding.FragmentCommunityBinding
import com.hsr2024.mungmungdoctortp.network.RetrofitCallback
import com.hsr2024.mungmungdoctortp.network.RetrofitProcess

class CommunityFragment:Fragment() {
    private val binding by lazy { FragmentCommunityBinding.inflate(layoutInflater) }
    //private var feedAdapter: FeedListAdapter? = null
    //private var qaAdapter: QAListAdapter? = null
    private val tab1Items = getItemListforTab1()
    private val tab2Items = getItemListforTab2()
    private var fabstate = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_insert -> {
                return true
            }

            R.id.menu_delete -> {
//                feedDelete()
                return true

            }
            else -> return super.onContextItemSelected(item)
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tab1Items = getItemListforTab1()
        val tab2Items = getItemListforTab2()

        // itemList 초기화
        binding.tablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        fabstate = 0
                        setupTab1()
                    }

                    1 -> {
                        fabstate = 1
                        setupTab2()
                    }
                }
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {}

            override fun onTabReselected(p0: TabLayout.Tab?) {}

        })
        // 초기 설정
        setupTab1()
        binding.fabFavor.setOnClickListener {
            if (fabstate == 0) {
                startActivity(Intent(context, FeedActivity::class.java))
            } else {
                startActivity(Intent(context, QAAddActivity::class.java))
            }

        }

    }//onViewCreated

    override fun onResume() {
        super.onResume()
        if (binding.tablayout.selectedTabPosition == 0){
            setupTab1()

        }else if (binding.tablayout.selectedTabPosition == 1){
            setupTab2()
        }
    }

    // 아이템 리스트 생성
    private fun getItemListforTab1() = mutableListOf<FeedData>()
    private fun getItemListforTab2() = mutableListOf<QAData>()

    //아이템 리스트 업데이트
    private fun setupTab1() {
        val params = Individual("${G.user_email}", "${G.user_providerId}", "${G.loginType}") // 비 로그인 상태일 경우 Individual()으로 생성가능
        RetrofitProcess(requireContext(), params = params, callback = object : RetrofitCallback {
            override fun onResponseListSuccess(response: List<Any>?) {}

            override fun onResponseSuccess(response: Any?) {
                val data = (response as FeedDataList)
                Log.d("feed list code", "$data")
                // - 6200 feed 목록 성공, 6201 feed 목록 실패, 4204 서비스 회원 아님
                if (data.code == "6200"){
                    //if (data.feedDatas != null){
                        val feed: List<FeedData> = data.feedDatas.sortedByDescending { it.create_date }
                        val feedAdapter = FeedListAdapter(requireContext(), feed)
                        binding.recyclerView.adapter = feedAdapter
                        feedAdapter.notifyDataSetChanged()
                   // }
                }
            }

            override fun onResponseFailure(errorMsg: String?) {
                Log.d("feed list fail", errorMsg!!) // 에러 메시지
            }

        }).feedListRequest()
    }

    private fun setupTab2() {
        val params =
            Individual("${G.user_email}", "${G.user_providerId}", "${G.loginType}") // 비 로그인 상태일 경우 Individual()으로 생성가능
        RetrofitProcess(requireContext(), params = params, callback = object : RetrofitCallback {
            override fun onResponseListSuccess(response: List<Any>?) {}

            override fun onResponseSuccess(response: Any?) {
                val data = (response as QADataList)
                Log.d("qa list code", "$data")

                if (data.code == "7200"){

                    val qaA: List<QAData> = data.qaDatas.sortedByDescending { it.qa_id }

                    if (qaA != null){
                        // - 7200 qa 목록 성공, 7201 qa 목록 실패, 4204 서비스 회원 아님
                        val qaAdapter = QAListAdapter(requireContext(), qaA)
                        binding.recyclerView.adapter = qaAdapter
                        qaAdapter.notifyDataSetChanged()
                    }
                }

            }

            override fun onResponseFailure(errorMsg: String?) {
                Log.d("qa list fail", errorMsg!!) // 에러 메시지
            }

        }).qaListRequest()
    }
}