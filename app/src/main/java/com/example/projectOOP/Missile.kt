package com.example.projectOOP

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.util.*

class Missile(context: Context, playerX: Float, playerY: Float) {
    var missile = arrayOfNulls<Bitmap>(10)
    var missileX : Float = 0F
    var missileY : Float = 0F
    var missileSpeed = 0

    init {
        for (i in 0..9) {
            missile[i] = BitmapFactory.decodeResource(context.resources, R.drawable.missile)
        }
        readyMissile(playerX, playerY)
    }

    fun readyMissile(playerX: Float, playerY: Float) {
        missileX = playerX + 50
        missileY = playerY
        missileSpeed = 30
    }
}