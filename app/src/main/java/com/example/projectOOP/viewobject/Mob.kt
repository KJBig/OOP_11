package com.example.projectOOP.viewobject

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.projectOOP.R
import java.util.*

class Mob(context: Context, private val dWidth: Int) {
    // 한번에 존재할 수 있는 몬스터의 최대 개수
    private val mobNum = 8
    // 몬스터 이미지 배열
    private var mob = arrayOfNulls<Bitmap>(mobNum)
    // 몬스터 이미지 순서
    var mobFrame = 0
    // 몬스터 위치
    var mobX = 0
    var mobY = 0
    // 몬스터가 움직이는 속도
    var mobVelocity = 0
    // 몬스터의 체력
    var mobHp : Int

    private var random: Random

    // 몬스터의 크기(이미지 프레임)
    val mobWidth: Int get() = mob[0]!!.width
    val mobHeight: Int get() = mob[0]!!.height

    // 배열에 몬스터 이미지 삽입
    init {
        mob[0] = BitmapFactory.decodeResource(context.resources, R.drawable.mob0)
        mob[1] = BitmapFactory.decodeResource(context.resources, R.drawable.mob1)
        mob[2] = BitmapFactory.decodeResource(context.resources, R.drawable.mob2)
        mob[3] = BitmapFactory.decodeResource(context.resources, R.drawable.mob3)
        mob[4] = BitmapFactory.decodeResource(context.resources, R.drawable.mob4)
        mob[5] = BitmapFactory.decodeResource(context.resources, R.drawable.mob5)
        mob[6] = BitmapFactory.decodeResource(context.resources, R.drawable.mob6)
        mob[7] = BitmapFactory.decodeResource(context.resources, R.drawable.mob7)

        // 호출 될 때마다 랜덤 값 변경
        random = Random()

        //
        for(i in 0 until mobNum) {
            mob[i] = mob[i]?.let {
                Bitmap.createScaledBitmap(it, 400, 400, true)
            }
            reProduce()
        }

        //몬스터 HP 설정
        mobHp = 3
    }

    // 순서에 맞는 몬스터 이미지 호출
    fun getMob(mobFrame: Int): Bitmap? {
        return mob[mobFrame]
    }

    //
    fun reProduce() {
        mobX = random.nextInt(dWidth - mobWidth)
        mobY = -200 + random.nextInt(600) * -1
        mobVelocity = 35 - random.nextInt(16)
        mobHp = 3
    }
}