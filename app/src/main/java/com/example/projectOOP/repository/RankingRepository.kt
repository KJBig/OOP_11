package com.example.projectOOP.repository


import androidx.lifecycle.MutableLiveData

import com.example.projectOOP.rank.Rank
import com.example.projectOOP.viewmodel.DEFAULT_RANK

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class RankingRepository {
    private val database = Firebase.database
    private val rankRef = database.getReference("Ranking")
    private var count = 0
    private val dbDataList = ArrayList<Rank>()
    private var nowLocation : String = " "
    
    fun observeRank(newRank: MutableLiveData<List<Rank>>, currentRank: MutableLiveData<String>, lastRank: MutableLiveData<String> ) {
        // FireBase 의 데이터를 score 기준으로 오름차순 정렬
        rankRef.orderByChild("score").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                count = 0
                dbDataList.clear()

                snapshot.children.forEach {
                    //DB의 모든 Rank 데이터를 dbList 에 넣음.
                    dbDataList.add(it.getValue<Rank>() ?: DEFAULT_RANK)
                }

                //dbList의 데이터들의 rank 값을 설정
                for(i in (dbDataList.size - 1) downTo 0 ){
                    dbDataList[count].rank = (i+1).toString()
                    count += 1
                }
                
                // 오름차순 -> 내림차순
                newRank.postValue(dbDataList.reversed())
                
                // lastRank와 currentRank를 구하는 과정
                for (rank in dbDataList) {
                    if("10" == rank.rank){
                        lastRank.value = rank.score.toString()
                    }
                    if(nowLocation == rank.location){
                        currentRank.value= rank.rank+"등"
                        break
                    }
                }

            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }



    // FireBase의 값들이 10개 이상인지 아닌지
    fun dbIsTen(newScore: Rank){
        // DB에 Rank 가 10개 미만 newScore 를 DB에 등록
        if(dbDataList.size < 10){
            postRank((dbDataList.size+1).toString(), newScore)
        }else{ // DB의 Rank 가 10개면 DB의 데이터들의 score 와 비교
            checkScore(dbDataList, newScore)
        }

    }
    
    // db의 데이터 중 score가 가장 낮은 데이터와 비교
    private fun checkScore(list: ArrayList<Rank>, newScore: Rank){
        val count = list.reversed()[list.size-1]
        if(count.score < newScore.score){
            postRank(count.location, newScore)
        }
    }


    // score 가장 낮은 데이터 -> newRank 변경
    private fun postRank(location: String, newScore: Rank) {
        newScore.location = location
        nowLocation = location
        rankRef.child(location).setValue(newScore)
    }


}