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
    private var health : Int = 0
    private var damage : Int = 0
    private var missileReload : Int = 0
    private var playerImage: Int = 0
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
        setPlayer(player)
        bind(player)

        //객체 지향의 중요한 다형성 이용.
        // 1번 비행기 선택시
        binding?.imagePlayer1?.setOnClickListener {
            player = Player1()
            setPlayer(player)
            bind(player)
        }
        // 2번 비행기 선택시
        binding?.imagePlayer2?.setOnClickListener {
            player = Player2()
            setPlayer(player)
            bind(player)
        }
        // 3번 비행기 선택시
        binding?.imagePlayer3?.setOnClickListener {
            player = Player3()
            setPlayer(player)
            bind(player)
        }

        // 최종 선택 시 GameFragment 로 player의 값을 전달.
        binding?.btnReady?.setOnClickListener {
            val bundle = bundleOf("health" to health, "damage" to damage,
                "missileReload" to missileReload, "playerImage" to playerImage)
            findNavController().navigate(R.id.action_readyFragment_to_gameFragment, bundle)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    // 선택된 Player 타입에 따라 이미지, 체력, 공격력을 보여주기 위함
    private fun bind(player: Player){
        binding?.showPlayer?.setImageResource(when( player.playerImage ){
            1-> R.drawable.player1
            2 -> R.drawable.player2
            else -> {R.drawable.player3}
        })

        binding?.txtHealth?.text = "체력 : ${health}"
        binding?.txtDamage?.text = "공격력 : ${damage}"
        binding?.txtReload?.text = "연사 딜레이 : ${missileReload}"
    }

    // 선택된 Player 타입의 값을 player 의 값으로 설정
    private fun setPlayer(player: Player) {
        health = player.health
        damage = player.damage
        missileReload = player.reload
        playerImage = player.playerImage
    }
}