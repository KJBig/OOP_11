package com.example.projectOOP.repository


import androidx.lifecycle.MutableLiveData

import com.example.projectOOP.Rank
import com.example.projectOOP.viewmodel.DEFAULT_RANK

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

//var nowRank:String =""
class RankingRepository {
    val database = Firebase.database
    val rankRef = database.getReference("Ranking")
    var count = 0
    val dbList = ArrayList<Rank>()
    var nowLocation : String = " "



    fun observeRank(newRank: MutableLiveData<List<Rank>>, nowRank: MutableLiveData<String>, lastRank: MutableLiveData<String> ) {
        rankRef.orderByChild("score").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                count = 0
                dbList.clear()


                snapshot.children.forEach {
                    //DB의 모든 Rank 데이터를 dbList 에 넣음.
                        //DB의 Rank는 Score로 오름차순
                    dbList.add(it.getValue<Rank>() ?: DEFAULT_RANK)
                }

                //dbList의 Rank 값을 설정
                for(i in (dbList.size - 1) downTo 0 ){
                    dbList[count].rank = (i+1).toString()
                    count += 1
                }
                // 오름차순 -> 내림차순
                newRank.postValue(dbList.reversed())

                for (rank in dbList) {

                    if("10" == rank.rank){
                        lastRank.value = rank.score.toString()
                    }
                    if(nowLocation == rank.location){
                        nowRank.value= rank.rank+"등"
                        break
                    }
                }


            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }




    fun getTopTen(newScore: Rank){
        // DB에 Rank 가 10개 미만 newScore 를 DB에 등록
        if(dbList.size < 10){
            postRank((dbList.size+1).toString(), newScore)
        }else{ // DB의 Rank 가 10개면 DB의 데이터들의 score 와 비교
            checkScore(dbList, newScore )
        }

    }


    fun checkScore(list: ArrayList<Rank>, newScore: Rank){
        val count = list.reversed()[list.size-1]
        if(count.score < newScore.score){
            postRank(count.location, newScore)
        }
    }



    fun postRank(location: String, newScore: Rank) {
        val pushScore = newScore
        pushScore.location = location
        nowLocation = location
        rankRef.child(location).setValue(pushScore)
    }


}