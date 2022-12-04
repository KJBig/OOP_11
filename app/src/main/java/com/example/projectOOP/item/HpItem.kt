package com.example.projectOOP.item

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.projectOOP.R

class HpItem(context: Context, x: Int, y: Int) {
    var itemImg : Bitmap
    var itemX : Int = 0
    var itemY : Int = 0
    private val itemSpeed = 15

    init {
        itemImg = BitmapFactory.decodeResource(context.resources, R.drawable.hpitem)
        makeItem(x, y)
    }

    val itemWidth: Int get() = itemImg.width
    val itemHeight: Int get() = itemImg.height

    // x, y값을 받아서 아이템의 좌표값을 설정해준다.
    private fun makeItem(x: Int, y: Int) {
        itemX = x
        itemY = y
    }

    // itemSpeed에 따라 아이템 움직임
    fun move(){
        itemY += itemSpeed
    }
}