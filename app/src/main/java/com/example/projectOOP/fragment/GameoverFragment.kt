package com.example.projectOOP.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.projectOOP.R
import com.example.projectOOP.databinding.FragmentGameoverBinding

class GameoverFragment : Fragment() {

    var binding : FragmentGameoverBinding? = null
    var score: Int? = 0
    var health: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            score = it.getInt("score")  // 게임 프래그먼트에서 보낸 값이 여기로 전달된다
            health = it.getInt("health")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGameoverBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.txtScore?.text = "점수 : ${score}"   // 위에서 전달받은 값을 할당한다

        binding?.btnRestart?.setOnClickListener {   // 다시하기
            var bundle = bundleOf("health" to health)
            findNavController().navigate(R.id.action_gameoverFragment_to_gameFragment, bundle)
        }

        binding?.btnBackReady?.setOnClickListener {
            findNavController().navigate(R.id.action_gameoverFragment_to_readyFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        binding = null
    }
}