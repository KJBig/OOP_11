package com.example.projectOOP.viewobject

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.projectOOP.R

class Explosion(context: Context) {
    // 폭발 이미지들을 저장할 배열
    private var explosion = arrayOfNulls<Bitmap>(4)
    // 폭발 이미지 순서
    var explosionFrame = 0
    // 폭발 이미지가 발생할 좌표
    var explosionX = 0
    var explosionY = 0

    // 배열에 폭발 이미지들 삽임
    init {
        explosion[0] = BitmapFactory.decodeResource(context.resources, R.drawable.explode0)
        explosion[1] = BitmapFactory.decodeResource(context.resources, R.drawable.explode1)
        explosion[2] = BitmapFactory.decodeResource(context.resources, R.drawable.explode2)
        explosion[3] = BitmapFactory.decodeResource(context.resources, R.drawable.explode3)
    }

    // 순서에 맞는 폭발 이미지 호출
    fun getExplosion(explosionFrame: Int): Bitmap? {
        return explosion[explosionFrame]
    }
}