package com.example.projectOOP.viewobject

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.projectOOP.R

class Missile(context: Context, playerX: Float, playerY: Float) {
    // 미사일 배열 -> 최대 출력 가능한 수
    private var missile = arrayOfNulls<Bitmap>(10)
    // 미사일 위치 초기화
    var missileX : Float = 0F
    var missileY : Float = 0F
    // 미사일 속도 초기화
    var missileSpeed = 0

    init {
        // 배열에 미사일 이미지 삽입
        for (i in 0..9) {
            missile[i] = BitmapFactory.decodeResource(context.resources, R.drawable.missile)
        }
        // 현재 플레이어의 위치를 기준으로 미사일 출력
        readyMissile(playerX, playerY)
    }

    // 현재 플레이어의 위치를 입력 받아 미사일의 위치를 설정
     // ** 이거 근데 왜 Y +50이 아니라 X + 50임?
    private fun readyMissile(playerX: Float, playerY: Float) {
        missileX = playerX + 50
        missileY = playerY
        missileSpeed = 30
    }
}