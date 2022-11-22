package com.example.projectOOP

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.projectOOP.databinding.ListRankBinding
import com.example.projectOOP.viewmodel.DEFAULT_RANK

class RankingAdapter(val ranks: LiveData<List<Rank>>)
: RecyclerView.Adapter<RankingAdapter.Holder>(){



        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {

            val binding = ListRankBinding.inflate(LayoutInflater.from(parent.context))
            return Holder(binding)
        }

        override fun onBindViewHolder(holder: Holder, position: Int){
            holder.bind(ranks.value?.get(position) ?: DEFAULT_RANK)
        }

        override fun getItemCount() = ranks.value?.size ?: 0


        class Holder(private val binding: ListRankBinding): RecyclerView.ViewHolder(binding.root){
            fun bind(rank: Rank){
                binding.imgPlayer.setImageResource(when( rank.image ){
                    "1" -> R.drawable.player1
                    "2" -> R.drawable.player2
                    else -> {R.drawable.player3}
                })
                binding.textRank.text = rank.rank
                binding.textScore.text = rank.score.toString()
                binding.textName.text = rank.name
                binding.textDate.text = rank.date
            }
        }

    }