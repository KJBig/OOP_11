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
    // Firebase db와 연결.
    private val database = Firebase.database
    private val rankRef = database.getReference("Ranking")

    // Firebase 에서 가져온 데이터를 Rank 타입으로 저장할 리스트
    private val dbDataList = ArrayList<Rank>()
    // 랭킹을 부여하기 위한 count
    private var count = 0
    // 랭킹 변경 시 Firebase 에서 변경될 데이터의 위치
    private var changedLocation : String = " "
    
    fun observeRank(newRank: MutableLiveData<List<Rank>>, currentRank: MutableLiveData<String>, lastRank: MutableLiveData<String> ) {
        // FireBase 의 데이터를 score 기준으로 오름차순 정렬
            // Firebase 는 내림차순 정렬이 없음 by 공식 문서

        rankRef.orderByChild("score").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                // 리스트와 count 값 초기화
                dbDataList.clear()
                count = 0

                // snapshot 의 children 을 반복하여 리스트에 Rank 타입으로 저장.
                snapshot.children.forEach {
                    //DB의 모든 Rank 데이터를 dbList 에 넣음.
                    dbDataList.add(it.getValue<Rank>() ?: DEFAULT_RANK)
                }

                //dbList 의 데이터들의 rank 값을 설정
                for(i in (dbDataList.size - 1) downTo 0 ){
                    dbDataList[count].rank = (i+1).toString()
                    count += 1
                }

                // 오름차순 -> 내림차순
                newRank.postValue(dbDataList.reversed())
                
                // lastRank 와 currentRank 를 구하는 과정
                for (rank in dbDataList) {
                    // Firebase 에 데이터가 10개 미만이면 lastRank 는 디폴트 값인 "-1"
                    if("10" == rank.rank){
                        lastRank.value = rank.score.toString()
                    }
                    if(changedLocation == rank.location){
                        currentRank.value= rank.rank+"등"
                        break
                    }
                }

            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    // FireBase 의 값들이 10개인지 아닌지
    fun dbIsTen(newScore: Rank){
        // DB에 Rank 가 10개 미만 newScore 를 DB에 등록
        if(dbDataList.size < 10){
            postRank((dbDataList.size+1).toString(), newScore)
        }else{ // DB의 Rank 가 10개면 DB의 10번째 score 와 score 와 비교
            checkScore(dbDataList, newScore)
        }
    }
    
    // newScore 와 Firebase 의 10번째 값과 비교
    private fun checkScore(list: ArrayList<Rank>, newScore: Rank){
        val count = list.reversed()[list.size-1]
        // newScore 가 더 높을 시 10번째 값과 바꾸기
        if(count.score < newScore.score){
            postRank(count.location, newScore)
        }
    }

    // score 가장 낮은 데이터 -> newRank 변경
    private fun postRank(location: String, newScore: Rank) {
        newScore.location = location
        changedLocation = location
        rankRef.child(location).setValue(newScore)
    }

}