package com.example.projectOOP.viewobject

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.projectOOP.R

class Explosion(context: Context) {
    // 폭발 이미지 배열
    private var explosion = ArrayList<Bitmap>(4)
    // 폭발 이미지 순서
    var explosionFrame = 0
    // 폭발의 위치
    var explosionX = 0
    var explosionY = 0

    // 배열에 폭발 이미지들을 삽입
    init {
        explosion.add(BitmapFactory.decodeResource(context.resources, R.drawable.explode0))
        explosion.add(BitmapFactory.decodeResource(context.resources, R.drawable.explode1))
        explosion.add(BitmapFactory.decodeResource(context.resources, R.drawable.explode2))
        explosion.add(BitmapFactory.decodeResource(context.resources, R.drawable.explode3))
    }

    // 현재 폭발 이미지를 반환
    fun getExplosion(explosionFrame: Int): Bitmap? {
        return explosion[explosionFrame]
    }
}