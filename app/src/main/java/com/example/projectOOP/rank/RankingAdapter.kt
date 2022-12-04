package com.example.projectOOP.rank

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.projectOOP.R
import com.example.projectOOP.databinding.ListRankBinding
import com.example.projectOOP.viewmodel.DEFAULT_RANK

//FireBase 에서 가져온 Rank 리스트를 리사이클러 뷰에 출력
class RankingAdapter(private val ranks: LiveData<List<Rank>>)
                                                        : RecyclerView.Adapter<RankingAdapter.Holder>(){

    //binding 생성 후 Holder 호출
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ListRankBinding.inflate(LayoutInflater.from(parent.context))
        return Holder(binding)
    }

    // 답은 Rank 리스트 하나하나를 holder.bind 를 통해 값을 지정.
    override fun onBindViewHolder(holder: Holder, position: Int){
        holder.bind(ranks.value?.get(position) ?: DEFAULT_RANK)
    }

    // onBindViewHolder 가 실행될 횟수 지정
    override fun getItemCount() = ranks.value?.size ?: 1

    //Holder Inner Class
    class Holder(private val binding: ListRankBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(rank: Rank){
            // player 이미지에 해당하는 이미지로 bind
            binding.imgPlayer.setImageResource(when( rank.image ){
                "1" -> R.drawable.player1
                "2" -> R.drawable.player2
                else -> {
                    R.drawable.player3
                }
            })
            // player 데이터에 맞는 값으로 설정
            binding.textRank.text = rank.rank
            binding.textScore.text = rank.score.toString()
            binding.textName.text = rank.name
            binding.textDate.text = rank.date
        }
    }

    }