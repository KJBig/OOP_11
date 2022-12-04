package com.example.projectOOP.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.projectOOP.player.Player1
import com.example.projectOOP.R
import com.example.projectOOP.databinding.FragmentReadyBinding
import com.example.projectOOP.player.Player
import com.example.projectOOP.player.Player2
import com.example.projectOOP.player.Player3

class ReadyFragment : Fragment() {

    private var binding : FragmentReadyBinding? = null

    // Bundle을 통해 GameFragment로 보내기 위한 플래이어 정보들
    private var player: Player = Player2()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReadyBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //기본 지정 플레이어 설정
        bind(player)

        /* 객체 지향의 중요한 다형성 이용. */
        // 1번 비행기 선택시
        binding?.imagePlayer1?.setOnClickListener {
            // Player1 객체 사용
            player = Player1()
            bind(player)
        }
        // 2번 비행기 선택시
        binding?.imagePlayer2?.setOnClickListener {
            // Player2 객체 사용
            player = Player2()
            bind(player)
        }
        // 3번 비행기 선택시
        binding?.imagePlayer3?.setOnClickListener {
            // Player3 객체 사용
            player = Player3()
            bind(player)
        }

        // 최종 선택(준비 완료 버튼 클릭) 시 플레이어 객체를 Bundle을 통해 전달.
        binding?.btnReady?.setOnClickListener {
            val bundle = bundleOf("player" to player)

            // navigate를 기반으로한 readyFragment에서 GameFragment로 이동
            findNavController().navigate(R.id.action_readyFragment_to_gameFragment, bundle)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    // 선택된 Player 타입에 따라 이미지, 체력, 공격력, 연사력을 보여주기 위함
    private fun bind(player: Player){

        // playerImage에 따라 이미지 적용
        binding?.showPlayer?.setImageResource(player.image)

        // textView 설정.
        binding?.txtHealth?.text = "체력 : ${player.health}"
        binding?.txtDamage?.text = "공격력 : ${player.damage}"
        binding?.txtReload?.text = "연사력 : ${(1000 - player.reload)/10}/ms"
    }
}