package com.example.projectOOP.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectOOP.Rank
import com.example.projectOOP.fragment.GameoverFragment
import com.example.projectOOP.repository.RankingRepository
import java.sql.Date
import java.text.SimpleDateFormat

val DEFAULT_RANK = Rank("-1","1","1",0, "2022-07-03"," ")
class RankViewModel: ViewModel() {
    private val _rank = MutableLiveData<List<Rank>>()
    val rank: LiveData<List<Rank>> get() = _rank

    private val _last = MutableLiveData<String>("")
    val last: LiveData<String> get() = _last


    private val _nowRank = MutableLiveData<String>()
    val nowRank: LiveData<String> get() = _nowRank


    private val repository = RankingRepository()


    init{
        repository.observeRank(_rank, _nowRank, _last)
    }


    private fun checkRank(score: Int?, name: String, playerImage: String) {
        val mFormat = SimpleDateFormat("yyyy-MM-dd")
        val newScore = DEFAULT_RANK
        newScore.score = score ?: 0
        newScore.name = name
        newScore.date = Date(System.currentTimeMillis()).toString().format(mFormat)
        newScore.image = playerImage

        repository.getTopTen(newScore)
    }


    fun tryRank(newScore: Int?, name: String, playerImage: Int){
        checkRank(newScore, name, playerImage.toString())
    }

    fun reSetNowRank(){
        _nowRank.postValue("")
    }
}