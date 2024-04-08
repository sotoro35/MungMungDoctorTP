package com.hsr2024.mungmungdoctortp.bnv3community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hsr2024.mungmungdoctortp.databinding.FragmentCommunityBinding

class CommunityFragment:Fragment() {

    private val binding by lazy { FragmentCommunityBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }
}