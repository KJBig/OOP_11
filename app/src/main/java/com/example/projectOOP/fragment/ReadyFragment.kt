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

    var binding : FragmentReadyBinding? = null
    var health : Int = 0
    var damage : Int = 0
    var reload : Int = 0
    var playerImage: Int = 0
    private var player = Player()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReadyBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        player = Player2()
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

        binding?.btnReady?.setOnClickListener {
            val bundle = bundleOf("health" to health, "damage" to damage,
                "reload" to reload, "playerImage" to playerImage)
            findNavController().navigate(R.id.action_readyFragment_to_gameFragment, bundle)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun bind(player: Player){
        binding?.showPlayer?.setImageResource(when( player.playerImage ){
            1-> R.drawable.player1
            2 -> R.drawable.player2
            else -> {R.drawable.player3}
        })

        binding?.txtHealth?.text = "체력 : ${health}"
        binding?.txtDamage?.text = "공격력 : ${damage}"
    }

    private fun setPlayer(player: Player) {
        health = player.health
        damage = player.damage
        reload = player.reload
        playerImage = player.playerImage
    }
}