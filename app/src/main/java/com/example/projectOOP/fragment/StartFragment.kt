package com.example.projectOOP.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.projectOOP.R
import com.example.projectOOP.databinding.FragmentStartBinding

class StartFragment : Fragment() {

    private var binding: FragmentStartBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStartBinding.inflate(inflater)

        return binding?.root
    }

    // 연결이 다 된후에 출력하기 때문에 좀 더 안전하다
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 시작 버튼을 누르면 navaigate를 기반으로한 readyFragment로 이동.
        binding?.btnStart?.setOnClickListener {
            findNavController().navigate(R.id.readyFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}