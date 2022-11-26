package com.example.projectOOP.fragment


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.projectOOP.*
import com.example.projectOOP.databinding.FragmentGameBinding


class GameFragment : Fragment(), GameOverController {
//class GameFragment : Fragment() {

    private var binding : FragmentGameBinding? = null

    private var health = 0
    private var damage = 0
    private var missileReload = 0
    private var playerImage = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            health = it.getInt("health")  // 레디 프래그먼트에서 보낸 체력 값
            damage = it.getInt("damage")
            missileReload = it.getInt("missileReload")
            playerImage = it.getInt("playerImage")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // GameView 시작
        return context?.let { GameView(it, health, damage, missileReload, playerImage, this) }
    }

//    override fun gameOver(bundle: Bundle) {
//        super.gameOver(bundle)
//        requireActivity().runOnUiThread{
//            findNavController().navigate(R.id.gameoverFragment, bundle)
//        }
//    }

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