package com.example.projectOOP.viewobject

// GameFragment로 부터 화면읜 높이를 받음.
data class BackgroundImage(private val dHeight: Int) {
    val x = 0
    var y = -dHeight
    val velocity = 30 // 화면이 움직이는 속도
}