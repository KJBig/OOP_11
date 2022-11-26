package com.example.projectOOP.viewobject

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.projectOOP.R
import java.util.*

class Mob(context: Context, private val dWidth: Int) {
    private val mobNum = 8
    private var mob = arrayOfNulls<Bitmap>(mobNum)
    var mobFrame = 0
    var mobX = 0
    var mobY = 0
    var mobVelocity = 0
    var mobHp : Int

    private var random: Random

    val mobWidth: Int get() = mob[0]!!.width
    val mobHeight: Int get() = mob[0]!!.height

    init {
        mob[0] = BitmapFactory.decodeResource(context.resources, R.drawable.mob0)
        mob[1] = BitmapFactory.decodeResource(context.resources, R.drawable.mob1)
        mob[2] = BitmapFactory.decodeResource(context.resources, R.drawable.mob2)
        mob[3] = BitmapFactory.decodeResource(context.resources, R.drawable.mob3)
        mob[4] = BitmapFactory.decodeResource(context.resources, R.drawable.mob4)
        mob[5] = BitmapFactory.decodeResource(context.resources, R.drawable.mob5)
        mob[6] = BitmapFactory.decodeResource(context.resources, R.drawable.mob6)
        mob[7] = BitmapFactory.decodeResource(context.resources, R.drawable.mob7)


        random = Random()

        for(i in 0 until mobNum) {
            mob[i] = mob[i]?.let {
                Bitmap.createScaledBitmap(it, 400, 400, true)
            }
            reProduce()
        }
        mobHp = 3
    }

    fun getMob(mobFrame: Int): Bitmap? {
        return mob[mobFrame]
    }

    fun reProduce() {
        mobX = random.nextInt(dWidth - mobWidth)
        mobY = -200 + random.nextInt(600) * -1
        mobVelocity = 35 - random.nextInt(16)
        mobHp = 3
    }
}