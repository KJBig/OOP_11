package com.example.projectOOP.viewobject

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.projectOOP.R

class Missile(context: Context, playerX: Float, playerY: Float) {
    var missileImg : Bitmap
    // 미사일 위치 초기화
    var missileX : Float = 0F
    var missileY : Float = 0F
    // 미사일 속도 초기화
    private var missileSpeed = 30

    init {
        // 미사일 이미지 설정 및 크기 조정
        missileImg = BitmapFactory.decodeResource(context.resources, R.drawable.missile)
        missileImg = Bitmap.createScaledBitmap(missileImg, 300, 300, false)
        // 현재 플레이어의 위치를 기준으로 미사일 출력
        readyMissile(playerX, playerY)
    }

    // 현재 플레이어의 위치를 입력 받아 미사일의 위치를 설정
    private fun readyMissile(playerX: Float, playerY: Float) {
        missileX = playerX + 50
        missileY = playerY
    }

    fun move() {
        missileY -= missileSpeed
    }
}