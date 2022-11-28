package com.example.projectOOP.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectOOP.R
import com.example.projectOOP.rank.Rank
import com.example.projectOOP.rank.RankingAdapter
import com.example.projectOOP.databinding.FragmentGameoverBinding
import com.example.projectOOP.viewmodel.RankViewModel

class GameOverFragment : Fragment() {

    private var binding : FragmentGameoverBinding? = null
    private val viewModel: RankViewModel by activityViewModels()

    private var score: Int? = 0 // -> 이거 왜 널 가능 임?
//    private var health: Int = 0
    private var playerImage: Int = 0

    private lateinit var dbData: LiveData<List<Rank>>
    private lateinit var currentRank : LiveData<String>
    private lateinit var lastRank : LiveData<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            score = it.getInt("score")  // 게임 프래그먼트에서 보낸 값이 여기로 전달된다
//            health = it.getInt("health")
            playerImage = it.getInt("playerImage")
            dbData = viewModel.dbData
            currentRank = viewModel.nowRank
            lastRank = viewModel.lastRank

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
        viewModel.dbData.observe(viewLifecycleOwner){
            binding?.pageRanking?.layoutManager = LinearLayoutManager(this.context)
            binding?.pageRanking?.adapter = RankingAdapter(dbData)

            if(lastRank.value != "-1") {
                if ((score ?: 0) <= (lastRank.value?.toInt() ?: 0)) {
                    postRank(false)
                    binding?.gameOverName?.setText("랭킹 밖입니다.")
                }
            }

        }
        //입력창 실시간 업데이트
        viewModel.nowRank.observe(viewLifecycleOwner){
            if(currentRank.value != "") {
                binding?.gameOverName?.setText(currentRank.value+"입니다.")
            }else{
                binding?.gameOverName?.setText("랭킹권 점수!! 이름을 입력하세요")
            }
            if((score ?: 0) <= (lastRank.value?.toInt() ?: 0)){
                postRank(false)
                binding?.gameOverName?.setText("랭킹 밖입니다.")
            }
        }

        // GameFragment에서 받은 값으로 지정
        binding?.txtScore?.text = "점수 : ${score}"

        // 현재 score 랭킹에 등록
        binding?.btnPostRank?.setOnClickListener {
            val name = binding?.gameOverName?.text.toString()
            viewModel.tryRank(score, name, playerImage)
            postRank(false)

        }

        // ready 화면으로 이동.
        binding?.btnBackReady?.setOnClickListener {
            viewModel.reSetNowRank()
            findNavController().navigate(R.id.action_gameoverFragment_to_readyFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        binding = null
    }

    private fun postRank(value: Boolean) {
        binding?.btnPostRank?.isEnabled = value
        binding?.gameOverName?.isEnabled = value
    }

}