package com.example.projectOOP.viewobject

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.projectOOP.R
import java.util.*

class Mob(context: Context, private val dWidth: Int) {
    // 한번에 출력 가능한 몬스터의 최대 개수
    private val mobNum = 8
    // 몬스터 이미지 배열
    private var mob = arrayOfNulls<Bitmap>(mobNum)
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
    val mobWidth: Int get() = mob[0]!!.width
    val mobHeight: Int get() = mob[0]!!.height

    init {
        //  배열에 몬스터 이미지 삽입
        mob[0] = BitmapFactory.decodeResource(context.resources, R.drawable.mob0)
        mob[1] = BitmapFactory.decodeResource(context.resources, R.drawable.mob1)
        mob[2] = BitmapFactory.decodeResource(context.resources, R.drawable.mob2)
        mob[3] = BitmapFactory.decodeResource(context.resources, R.drawable.mob3)
        mob[4] = BitmapFactory.decodeResource(context.resources, R.drawable.mob4)
        mob[5] = BitmapFactory.decodeResource(context.resources, R.drawable.mob5)
        mob[6] = BitmapFactory.decodeResource(context.resources, R.drawable.mob6)
        mob[7] = BitmapFactory.decodeResource(context.resources, R.drawable.mob7)

        // 랜덤 값 변경
        random = Random()


        // *** 이 부분 이해가 잘 안됨 *** 비크맵 크기를 키우는건가
        // 크기를 맘대로 늘렸다 줄였다 해보려고 만들었었음 값을 바꾸면 크기 바꿀수 있긴한데 없애도 노상관이긴함 테스트용 코드임
        for(i in 0 until mobNum) {
            mob[i] = mob[i]?.let {
                Bitmap.createScaledBitmap(it, 400, 400, true)
            }
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