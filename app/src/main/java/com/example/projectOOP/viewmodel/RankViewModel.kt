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
    //DB의 모든 rank Data
    private val _dbData = MutableLiveData<List<Rank>>()
    val dbData: LiveData<List<Rank>> get() = _dbData

    //DB의 10등 데이터와 비교 했을 때 어떤지\
    private val _lastRank = MutableLiveData<String>("")
    val lastRank: LiveData<String> get() = _lastRank

    //현재 score 의 등수
    private val _nowRank = MutableLiveData<String>()
    val nowRank: LiveData<String> get() = _nowRank

    private val repository = RankingRepository()

    init{
        repository.observeRank(_dbData, _nowRank, _lastRank)
    }


    private fun checkRank(score: Int?, name: String, playerImage: String) {
        val mFormat = SimpleDateFormat("yyyy-MM-dd")
        val newScore = DEFAULT_RANK
        newScore.score = score ?: 0
        newScore.name = name
        newScore.date = Date(System.currentTimeMillis()).toString().format(mFormat)
        newScore.image = playerImage

        repository.dbIsTen(newScore)
    }


    fun tryRank(newScore: Int?, name: String, playerImage: Int){
        checkRank(newScore, name, playerImage.toString())
    }

    // GameoverFragment에서 다시하기 눌렀을 시 초기화.
    fun reSetNowRank(){
        _nowRank.postValue("")
    }
}