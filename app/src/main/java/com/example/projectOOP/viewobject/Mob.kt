package com.example.projectOOP.viewobject

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.projectOOP.R
import java.util.*
import kotlin.collections.ArrayList

class Mob(context: Context, private val dWidth: Int) {
    // 한번에 출력 가능한 몬스터의 최대 개수
    private val mobNum = 8
    // 몬스터 이미지 배열
    private var mob = ArrayList<Bitmap>(mobNum)
    // 몬스터 이미지 순서
    var mobFrame = 0
    // 몬스터 위치 초기화
    var mobX = 0
    var mobY = 0
    // 몬스터 이동 속도
    var mobVelocity = 0
    // 몬스터 체력
    var mobHp = 3
    // 움직이는 방법
    var howMove = 0
    // 움직이는 방향
    var direction = 0
    // 아이템이 생성될 확률
    var itemDrop = 0

    // 몬스터 위치 초기화를 위한 랜덤
    private var random: Random

    // 몬스터 이미지 프레임의 넓이와 높이
    val mobWidth: Int get() = mob[0].width
    val mobHeight: Int get() = mob[0].height

    init {
        //  배열에 몬스터 이미지 삽입
        mob.add(BitmapFactory.decodeResource(context.resources, R.drawable.mob0))
        mob.add(BitmapFactory.decodeResource(context.resources, R.drawable.mob1))
        mob.add(BitmapFactory.decodeResource(context.resources, R.drawable.mob2))
        mob.add(BitmapFactory.decodeResource(context.resources, R.drawable.mob3))
        mob.add(BitmapFactory.decodeResource(context.resources, R.drawable.mob4))
        mob.add(BitmapFactory.decodeResource(context.resources, R.drawable.mob5))
        mob.add(BitmapFactory.decodeResource(context.resources, R.drawable.mob6))
        mob.add(BitmapFactory.decodeResource(context.resources, R.drawable.mob7))

        // 랜덤 값 변경
        random = Random()

        for(i in 0 until mobNum) {
            reGen()
        }
    }

    // 순서에 맞는 몬스터 이미지 반환
    fun getMob(mobFrame: Int): Bitmap? {
        return mob[mobFrame]
    }

    // 미사일과 충돌 혹은 화면 밖으로 사라지면 몬스터 재생성
    fun reGen() {
        mobX = random.nextInt(dWidth - mobWidth)
        mobY = -200 + random.nextInt(600) * -1
        mobVelocity = 35 - random.nextInt(16)
        mobHp = 3
        howMove = random.nextInt(2)
        itemDrop = random.nextInt(10)   // 아이템 드랍율 약 10%
    }

    // 몬스터의 움직임 분류
    fun movement(){
        if(howMove == 0){
            straightMovement()
        } else {
            floorMovement()
        }
    }

    // 직선 움직임
    private fun straightMovement(){
        mobY += mobVelocity
    }

    // 계단처럼 움직임
    private fun floorMovement(){
        if(direction != 0) { // 오른쪽으로
            mobY += mobVelocity - 10
            mobX += mobVelocity
        }else{ // 왼쪽으로
            mobX -= mobVelocity
            mobY += mobVelocity - 10
        }
    }

}