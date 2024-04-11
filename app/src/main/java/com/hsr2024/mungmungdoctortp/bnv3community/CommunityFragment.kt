package com.hsr2024.mungmungdoctortp.bnv3community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.hsr2024.mungmungdoctortp.databinding.FragmentCommunityBinding

class CommunityFragment:Fragment() {
    private val binding by lazy { FragmentCommunityBinding.inflate(layoutInflater) }
    private var feedAdapter:FeedListAdapter? = null
    private var qaAdapter: QAListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tab1Items=getItemListforTab1()
        val tab2Items=getItemListforTab2()

        // itemList 초기화
        val feedData=FeedData("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ_bPVLRowjzhD-ZGGFR4030vnxuvqueINKSNcbtg5Lpg&s",
            "feednickname","https://cdn-icons-png.flaticon.com/512/1361/1361876.png","2","3","Feed 내용","2024-01-02")
        val qaData=QAData("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ_bPVLRowjzhD-ZGGFR4030vnxuvqueINKSNcbtg5Lpg&s","title",
            "qanickname","3","6")
        tab1Items.add(feedData)
        tab2Items.add(qaData)

        binding.tablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab?.position){
                    0 -> setupTab1()
                    1 -> setupTab2()
                }
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {}

            override fun onTabReselected(p0: TabLayout.Tab?) {}

        })

        // 초기 설정
        setupTab1()
    }

    // 아이템 리스트 생성
    private fun getItemListforTab1()=mutableListOf<FeedData>()
    private fun getItemListforTab2()=mutableListOf<QAData>()

    //아이템 리스트 업데이트
    private fun setupTab1() {
        if (feedAdapter == null) {
            val tab1Items = getItemListforTab1()
            feedAdapter = FeedListAdapter(requireContext(), tab1Items)
            binding.recyclerView.adapter = feedAdapter
        } else {
            val tab1Items=getItemListforTab1()
            feedAdapter?.setData(tab1Items)
            feedAdapter?.notifyDataSetChanged()
        }
    }
    private fun setupTab2() {
        if (qaAdapter == null) {
            val tab2Items = getItemListforTab2()
            qaAdapter = QAListAdapter(requireContext(), tab2Items)
            binding.recyclerView.adapter = qaAdapter
        } else {
            val tab2Items = getItemListforTab2()
            qaAdapter?.setData(tab2Items)
            qaAdapter?.notifyDataSetChanged()
        }
    }
}