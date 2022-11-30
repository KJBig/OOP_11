package com.example.projectOOP.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectOOP.rank.Rank
import com.example.projectOOP.repository.RankingRepository
import java.sql.Date
import java.text.SimpleDateFormat

val DEFAULT_RANK = Rank("-1","1","1",0, "2022-07-03"," ")
class RankViewModel: ViewModel() {
    //DB의 모든 Rank Data
    private val _dbData = MutableLiveData<List<Rank>>()
    val dbData: LiveData<List<Rank>> get() = _dbData

    //DB의 10등 데이터와 비교 했을 때 어떤지 판단
    private val _lastRank = MutableLiveData<String>("-1")
    val lastRank: LiveData<String> get() = _lastRank

    //현재 score 의 등수
    private val _currentRank = MutableLiveData<String>("")
    val currentRank: LiveData<String> get() = _currentRank

    private val repository = RankingRepository()

    init{
        repository.observeRank(_dbData, _currentRank, _lastRank)
    }

    // DB 접촉이기 때문에 private로 설정
    private fun checkRank(score: Int?, name: String, playerImage: String) {
        // date format
        val mFormat = SimpleDateFormat("yyyy-MM-dd")
        // Default 값에서 필요 값만 변경
        val newScore = DEFAULT_RANK
        newScore.score = score ?: 0
        newScore.name = name
        newScore.date = Date(System.currentTimeMillis()).toString().format(mFormat)
        newScore.image = playerImage

        repository.dbIsTen(newScore)
    }

    // GameOverFragment 에서 호출됨
    fun tryRank(newScore: Int?, name: String, playerImage: Int){
        checkRank(newScore, name, playerImage.toString())
    }

    // GameOverFragment 에서 다시하기 눌렀을 시 초기화.
    fun reSetNowRank(){
        _currentRank.postValue("")
    }
}