package com.example.projectOOP.viewobject

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.projectOOP.R

// GameFragment의 높이 값을 입력 받아 처리함.
class BackgroundImage(context: Context, start: Int) {
    // 이미지의 시작 위치 설정
    val x = 0
    var y = 0

    private val backGroundNum = 6
    var count = 0
    // 배경화면 이미지 배열
    private val bgAry = ArrayList<Bitmap>(backGroundNum)
    var nowImageNum = 0
    var nowImage: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.bg_spring)

    // 이미지의 이동 속도
    private val velocity = 30

    init{
        bgAry.add(BitmapFactory.decodeResource(context.resources, R.drawable.bg_spring))
        bgAry.add(BitmapFactory.decodeResource(context.resources, R.drawable.bg_sea))
        bgAry.add(BitmapFactory.decodeResource(context.resources, R.drawable.bg_fall))
        bgAry.add(BitmapFactory.decodeResource(context.resources, R.drawable.bg_winter))
        bgAry.add(BitmapFactory.decodeResource(context.resources, R.drawable.bg_high))
        bgAry.add(BitmapFactory.decodeResource(context.resources, R.drawable.bg_desert))

        y = -(nowImage.height - start)
    }

    fun move(){
        y += velocity
    }

    fun reGen(newY: Int){
        y = newY
    }

    fun changeImage(){
        nowImageNum += 1
        nowImage = bgAry[nowImageNum]
        if(nowImageNum == 6){
            nowImageNum = 0
        }
    }
}