package com.example.projectOOP.fragment


import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.projectOOP.*
import com.example.projectOOP.databinding.FragmentGameBinding
import com.example.projectOOP.player.Player

// 게임 종료 시 GameView가 gameOver()를 호출하기 위해 GameOverController 인터페이스를 상속 받음.
class GameFragment : Fragment(), GameOverController {

    private var binding : FragmentGameBinding? = null

    // 플레이어 객체 초기화
    var player:Player = Player()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            // ReadyFragment 에서 보낸 객체를 GameFragment 의 값으로 지정
            player = it.getSerializable("player") as Player
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // GameView 시작
        // GameView에서 gameOver()를 호출하게 하기위해 인터페이스를 파라미터로 전달.
        return context?.let { GameView(it, player, this) }
    }

    // game 종료 시 GameView가 해당 메소드 호출
   override fun gameOver(bundle: Bundle) {
       super.gameOver(bundle)
        requireActivity().runOnUiThread{
            findNavController().navigate(R.id.gameoverFragment, bundle)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}