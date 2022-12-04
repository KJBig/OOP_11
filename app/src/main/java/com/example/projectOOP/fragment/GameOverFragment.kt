package com.example.projectOOP.fragment

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectOOP.MainActivity
import com.example.projectOOP.R
import com.example.projectOOP.rank.Rank
import com.example.projectOOP.rank.RankingAdapter
import com.example.projectOOP.databinding.FragmentGameoverBinding
import com.example.projectOOP.viewmodel.RankViewModel

private const val NOTIFICATION_ID = 1
private const val CHANNEL_ID = "ranking"

class GameOverFragment : Fragment() {
    private var notificationManager: NotificationManager? = null
    private var binding : FragmentGameoverBinding? = null
    private val viewModel: RankViewModel by activityViewModels()

    private var score: Int = 0

    // 랭킹 등록을 위한 player Image
    private var playerImage: Int = 0

    // Livedata 설정
    // 현재 DB의 데이터들
    private lateinit var dbData: LiveData<List<Rank>>
    // 랭킹 등록 후 현재 플레이어의 등수
    private lateinit var currentRank : LiveData<String>
    // 랭킹 등록 전 랭킹권인지 확인을 위한 lastRank
    private lateinit var lastRank : LiveData<String>
    private lateinit var mainActivity : MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)                 // onAttach()의 매개변수로 context가 들어온다

        mainActivity = context as MainActivity  // context를 activity로 형변환하여 사용한다
    }                                           // getSystemService를 통해 notificationManager를 사용하려면 activity가 필요하다

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

        createNotificationChannel(CHANNEL_ID, "ranking", "ranking alert")   // 채널 생성
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
            // DB의 데이터를 RecyclerView로 출력
            binding?.pageRanking?.layoutManager = LinearLayoutManager(this.context)
            binding?.pageRanking?.adapter = RankingAdapter(dbData)

            // lastRank가 -1이 아닐 때. 즉,DB의 데이터가 10개 이상일 경우
            if(lastRank.value != "-1") {
                //현재 플레이어와 lastRank의 점수를 비교.
                if ((score) <= (lastRank.value?.toInt() ?: 0)) {
                    // lastRank가 높다면
                    postEnabled(false)
                    binding?.gameOverName?.setText("랭킹 밖입니다.")
                }else{
                    // 현재 플레이어가 높다면
                    binding?.gameOverName?.setText("랭킹권 점수!! 이름을 입력하세요")
                }
            }else{ // Firebase 에 등록된 랭킹이 10개 미만일 경우
                binding?.gameOverName?.setText("랭킹권 점수!! 이름을 입력하세요")
            }

            // 랭킹 등록 시 현재 플레이어의 랭킹 표시
            if(currentRank.value != "") {
                binding?.gameOverName?.setText(currentRank.value+"입니다.")
            }
        }

        // GameView 에서 받은 값으로 지정
        binding?.txtScore?.text = "점수 : ${score}"

        // 버튼 클릭 시 현재 score 랭킹에 등록
        binding?.btnPostRank?.setOnClickListener {
            displayNotification()   // 알림 띄우기
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

    // 이름 입력칸, 랭킹 post 버튼 활/비활성화
    private fun postEnabled(value: Boolean) {
        binding?.btnPostRank?.isEnabled = value
        binding?.gameOverName?.isEnabled = value
    }

    // 알림의 세부정보를 설정하고 띄우는 메소드
    private fun displayNotification() {
        val notification = Notification.Builder(mainActivity, CHANNEL_ID)
            .setSmallIcon(R.drawable.player2)
            .setContentTitle("랭킹 변동")
            .setContentText("새로운 랭커가 등장했습니다")
            .setAutoCancel(true)
            .build()

        notificationManager?.notify(NOTIFICATION_ID, notification)
    }

    // android 8.0 (api26) 부터는 알람을 띄우려면 채널을 생성해야 한다
    // 채널을 생성해준다
    private fun createNotificationChannel(channelId: String, name: String, channelDescription: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH     // 중요도 설정
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = channelDescription
            }

            notificationManager = mainActivity.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager?.createNotificationChannel(channel)
        }
    }
}