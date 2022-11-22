package com.example.projectOOP

enum class PlayerImage{
    ONE, TWO, THREE
}

data class Rank(var rank: String="",
                var location: String="",
                var image: String="",
                var score: Int=0,
                var date: String="",
                var name: String="")