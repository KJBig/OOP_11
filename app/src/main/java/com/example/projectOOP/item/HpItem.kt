package com.example.projectOOP.item

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.projectOOP.R

class HpItem(context: Context, x: Int, y: Int) {
    var items = arrayOfNulls<Bitmap>(10)
    var itemX : Int = 0
    var itemY : Int = 0
    var itemSpeed = 0

    init {
        for (i in 0..9) {
            items[i] = BitmapFactory.decodeResource(context.resources, R.drawable.hpitem)
        }
        makeItem(x, y)
    }

    val itemWidth: Int
        get() = items[0]!!.width
    val itemHeight: Int
        get() = items[0]!!.height

    fun makeItem(x: Int, y: Int) {
        itemX = x
        itemY = y
        itemSpeed = 15
    }
}