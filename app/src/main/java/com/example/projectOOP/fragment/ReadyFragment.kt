package com.example.projectOOP.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.projectOOP.R
import com.example.projectOOP.databinding.FragmentReadyBinding
import kotlin.concurrent.thread

class ReadyFragment : Fragment() {

    var binding : FragmentReadyBinding? = null
    var health : Int = 1
    var totalTime : Int = 0
    var started = false

    val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            val minute = String.format("%d", totalTime / 60)
            val second = String.format("%d", totalTime % 60)
            binding?.txtThread?.text = "쓰레드 : $second"
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReadyBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.btnThreadStart?.setOnClickListener {  // 그냥 쓰레드 예시 코드
            if (started == false) {
                started = true
                totalTime = 0
                binding?.txtThread?.text = "쓰레드 : ${totalTime}"

                thread(start = true) {
                    while (started) {
                        Thread.sleep(1000)
                        if (started) {
                            totalTime += 1
                            handler?.sendEmptyMessage(0)
                        }
                    }
                }
            }
        }

        binding?.btnThreadStop?.setOnClickListener {
            started = false
        }

        binding?.btnPlusHealth?.setOnClickListener {
            health++;
            binding?.txtHealth?.text = "체력 : ${health}"
        }

        binding?.btnReady?.setOnClickListener {
            val bundle = bundleOf("health" to health)
            findNavController().navigate(R.id.action_readyFragment_to_gameFragment, bundle)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}