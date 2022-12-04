package com.example.projectOOP.player

import com.example.projectOOP.R

// 번들을 통해 객체를 전달하기위해 Serializable 상속
open class Player:java.io.Serializable {
    open var playerImage = 0 // 플레이어의 이미지를 결정
    open var damage = 0 // 공격력
    open var health = 0 // 플레이어의 체력
    open var reload = 0 // 미사일 연사력
    open var image = R.drawable.player2
}