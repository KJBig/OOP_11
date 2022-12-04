package com.example.projectOOP.viewobject

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.projectOOP.R

class Missile(context: Context, playerX: Float, playerY: Float) {
    // 한번에 최대 10개의 미사일 장전.
    private var missile = arrayOfNulls<Bitmap>(10)
    // 미사일의 위치 초기화
    var missileX : Float = 0F
    var missileY : Float = 0F
    // 미사일 속도 초기화
    var missileSpeed = 0

    init {
        // 배열에 미사일 이미지 삽입
        for (i in 0..9) {
            missile[i] = BitmapFactory.decodeResource(context.resources, R.drawable.missile)
        }
        readyMissile(playerX, playerY)
    }

    // 미사일 위치 및 속도 할당.
    private fun readyMissile(playerX: Float, playerY: Float) {
        missileX = playerX + 50
        missileY = playerY
        missileSpeed = 30
    }
}