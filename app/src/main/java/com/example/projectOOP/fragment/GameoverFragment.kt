package com.example.projectOOP.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectOOP.R
import com.example.projectOOP.Rank
import com.example.projectOOP.RankingAdapter
import com.example.projectOOP.databinding.FragmentGameoverBinding
import com.example.projectOOP.viewmodel.RankViewModel

class GameoverFragment : Fragment() {

    var binding : FragmentGameoverBinding? = null
    val viewModel: RankViewModel by activityViewModels()

    var score: Int? = 0
    var health: Int = 0
    var playerImage: Int = 0

    lateinit var ranks : LiveData<List<Rank>>
    lateinit var nowRank : LiveData<String>
    lateinit var lastRank : LiveData<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            score = it.getInt("score")  // 게임 프래그먼트에서 보낸 값이 여기로 전달된다
            health = it.getInt("health")
            playerImage = it.getInt("playerImage")
            ranks = viewModel.rank
            nowRank = viewModel.nowRank
            lastRank = viewModel.last

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

        //Ranking 실시간 업데이트
        viewModel.rank.observe(viewLifecycleOwner){
            binding?.pageRanking?.layoutManager = LinearLayoutManager(this.context)
            binding?.pageRanking?.adapter = RankingAdapter(ranks)

            if((score ?: 0) <= (lastRank.value?.toInt() ?: 0)){
                postRank(false)
                binding?.gameOverName?.setText("랭킹 밖입니다.")
            }


        }
        //입력창 실시간 업데이트
        viewModel.nowRank.observe(viewLifecycleOwner){
            if(nowRank.value != "") {
                binding?.gameOverName?.setText(nowRank.value+"입니다.")
            }else{
                binding?.gameOverName?.setText("랭킹권 점수!! 이름을 입력하세요")
            }
            if((score ?: 0) <= (lastRank.value?.toInt() ?: 0)){
                postRank(false)
                binding?.gameOverName?.setText("랭킹 밖입니다.")
            }
        }


        binding?.txtScore?.text = "점수 : ${score}"   // 위에서 전달받은 값을 할당한다


        binding?.btnPostRank?.setOnClickListener {
            val name = binding?.gameOverName?.text.toString()
            viewModel.tryRank(score, name, playerImage)
            postRank(false)

        }

        binding?.btnBackReady?.setOnClickListener {
            viewModel.reSetNowRank()
            findNavController().navigate(R.id.action_gameoverFragment_to_readyFragment)
        }
    }

    private fun postRank(value: Boolean) {
        binding?.btnPostRank?.isEnabled = value
        binding?.gameOverName?.isEnabled = value
    }

    override fun onDestroyView() {
        super.onDestroyView()

        binding = null
    }

}