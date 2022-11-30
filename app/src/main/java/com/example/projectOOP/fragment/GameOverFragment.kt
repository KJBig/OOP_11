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
    private var playerImage: Int = 0

    private lateinit var dbData: LiveData<List<Rank>>
    private lateinit var currentRank : LiveData<String>
    private lateinit var lastRank : LiveData<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            // GameView 에서 보낸 값을 GameOver Fragment 의 값으로 설정
            score = it.getInt("score")
            playerImage = it.getInt("playerImage")

            // LiveData 설정.
            dbData = viewModel.dbData
            currentRank = viewModel.currentRank
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
                    postEnabled(false)
                    binding?.gameOverName?.setText("랭킹 밖입니다.")
                }else{
                    binding?.gameOverName?.setText("랭킹권 점수!! 이름을 입력하세요")
                }
            }else{ // Firebase 에 등록된 랭킹이 10개 미만일 경우
                binding?.gameOverName?.setText("랭킹권 점수!! 이름을 입력하세요")
            }

            // 랭킹 등록 시 등록된 랭킹 표시
            if(currentRank.value != "") {
                binding?.gameOverName?.setText(currentRank.value+"입니다.")
            }

        }

        // GameView 에서 받은 값으로 지정
        binding?.txtScore?.text = "점수 : ${score}"

        // 현재 score 랭킹에 등록
        binding?.btnPostRank?.setOnClickListener {
            val name = binding?.gameOverName?.text.toString()
            viewModel.tryRank(score, name, playerImage)
            postEnabled(false)
        }

        // 다시하기 버튼 ReadyFragment 로 이동.
        binding?.btnBackReady?.setOnClickListener {
            viewModel.reSetNowRank()
            findNavController().navigate(R.id.action_gameoverFragment_to_readyFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        binding = null
    }

    private fun postEnabled(value: Boolean) {
        binding?.btnPostRank?.isEnabled = value
        binding?.gameOverName?.isEnabled = value
    }

}